package com.ijob.android.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.ijob.android.R;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.android.ui.fragment.ContactsFragment;
import com.ijob.android.ui.fragment.HomeFragment;
import com.ijob.android.ui.fragment.JudgeFragment;
import com.ijob.android.ui.fragment.SettingFragment;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.T;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.db.HXInviteMessageDao;
import com.ijob.hx.db.HXUserDao;
import com.ijob.hx.db.domain.HXInviteMessage;
import com.ijob.hx.db.domain.HXUser;
import com.ijob.hx.ui.activity.BaseActivity;
import com.ijob.hx.ui.activity.ChatActivity;
import com.ijob.hx.ui.activity.GroupsActivity;
import com.ijob.hx.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zsigui on 15-3-11.
 */
public class MainLogicActivity extends BaseActivity {


	protected static final String TAG = "MainLogicActivity";
	protected static final int INDEX_CONTACTS_IM = 3;

	// 未读消息textview
	private ImageView unreadLabel;
	private Button[] mTabs;
	private Fragment[] mFragments;
	private HomeFragment homeFragment;
	private JudgeFragment judgeFragment;
	private ContactsFragment contactsFragment;
	private SettingFragment setFragment;

	private int mCurrentTabIndex = 0;
	private int mLastIndex = mCurrentTabIndex;
	private int mIndex;
	// 账号在别处登录
	public boolean isConflict = false;
	//账号被移除
	private boolean isCurrentAccountRemoved = false;
	private boolean isAccountRemovedDialogShow = false;
	private boolean isConflictDialogShow = false;

	// 广播监听
	private NewMessageBroadcastReceiver msgReceiver;
	private AlertDialog.Builder accountRemovedBuilder;
	private AlertDialog.Builder conflictBuilder;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.getBoolean(HXConstants.ACCOUNT_REMOVED, false)){
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// fragment里加的判断同理
			AppInfo.sAppInstance.logout(null);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}else if (savedInstanceState != null && savedInstanceState.getBoolean(HXConstants.IS_APP_CONFLICT, false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// fragment里加的判断同理
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.activity_main);
		MobclickAgent.updateOnlineConfig(this);
		if (getIntent().getBooleanExtra(HXConstants.IS_APP_CONFLICT, false) && !isConflictDialogShow){
			showConflictDialog();
		}else if(getIntent().getBooleanExtra(HXConstants.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow){
			showAccountRemovedDialog();
		}
		initView();
		registerReceiver();
		setListener();


	}

	private void setListener() {
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}

	private void registerReceiver() {
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		//注册一个透传消息的BroadcastReceiver
		IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
		cmdMessageIntentFilter.setPriority(3);
		registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);

		// 注册一个离线消息的BroadcastReceiver
		// IntentFilter offlineMessageIntentFilter = new
		// IntentFilter(EMChatManager.getInstance()
		// .getOfflineMessageBroadcastAction());
		// registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);
	}

	private void unRegisterReceiver() {
		// 注销广播接收者
		try {
			if (msgReceiver != null)
				unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			if (ackMessageReceiver != null)
				unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}
		try {
			if (cmdMessageReceiver != null)
				unregisterReceiver(cmdMessageReceiver);
		} catch (Exception e) {
		}

		// try {
		// unregisterReceiver(offlineMessageReceiver);
		// } catch (Exception e) {
		// }
	}

	private void initView() {
		homeFragment = HomeFragment.newInstance("home");
		judgeFragment = JudgeFragment.newInstance("pub");
		contactsFragment = ContactsFragment.newInstance();
		setFragment = SettingFragment.newInstance(0);
		mFragments = new Fragment[]{homeFragment, judgeFragment, contactsFragment, setFragment};
		// 显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.flLayContent, homeFragment).show(homeFragment).commit();
		unreadLabel = (ImageView) findViewById(R.id.unread_msg);
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_home_index);
		mTabs[1] = (Button) findViewById(R.id.btn_home_pub);
		mTabs[2] = (Button) findViewById(R.id.btn_classmates);
		mTabs[3] = (Button) findViewById(R.id.btn_home_center);
		mTabs[mCurrentTabIndex].setSelected(true);
	}

	/**
	 * Button点击事件
	 * @param v
	 */
	public void onTabClicked(View v) {
		switch (v.getId()) {
			case R.id.btn_home_index:
				mCurrentTabIndex = 0;
				break;
			case R.id.btn_home_pub:
				mCurrentTabIndex = 1;
				break;
			case R.id.btn_classmates:
				mCurrentTabIndex = 2;
				break;
			case R.id.btn_home_center:
				mCurrentTabIndex = 3;
				break;
		}
		if (mLastIndex != mCurrentTabIndex) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.hide(mFragments[mLastIndex]);
			if (!mFragments[mCurrentTabIndex].isAdded()) {
				ft.add(R.id.flLayContent, mFragments[mCurrentTabIndex]);
			}
			ft.show(mFragments[mLastIndex]).commit();
		}
		mTabs[mLastIndex].setSelected(false);
		mTabs[mCurrentTabIndex].setSelected(true);
		mLastIndex = mCurrentTabIndex;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterReceiver();
	}

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved(){
		return isCurrentAccountRemoved;
	}

	/**
	 * 新消息广播接收者
	 *
	 *
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == EMMessage.ChatType.GroupChat) {
					if (message.getTo().equals(ChatActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
						return;
				}
			}

			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();

			notifyNewMessage(message);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (mCurrentTabIndex == 3) {
				// 当前页面如果为聊天历史页面，刷新此页面

			}

		}
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadMsgCountTotal();
				if (count > 0) {
					unreadLabel.setVisibility(View.VISIBLE);
				} else {
					unreadLabel.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	/**
	 * 获取未读申请与通知消息
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCount = EMChatManager.getInstance().getUnreadMsgsCount();
		if (AppInfo.sAppInstance.getContactList().get(HXConstants.NEW_FRIENDS_USERNAME) != null)
			unreadMsgCount += AppInfo.sAppInstance.getContactList().get(HXConstants.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadMsgCount;
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();

			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");

			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);

				if (msg != null) {

					if (ChatActivity.activityInstance != null) {
						if (msg.getChatType() == EMMessage.ChatType.Chat) {
							if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
								return;
						}
					}

					msg.isAcked = true;
				}
			}

		}
	};

	/**
	* 透传消息BroadcastReceiver
	*/
	private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			//获取cmd message对象
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = intent.getParcelableExtra("message");
			//获取消息body
			CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
			String action = cmdMsgBody.action;//获取自定义action

			//获取扩展属性 此处省略
//			message.getStringAttribute("");
			EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
			String st9 = getResources().getString(R.string.receive_the_passthrough);
			Toast.makeText(MainLogicActivity.this, st9 + action, Toast.LENGTH_SHORT).show();
		}
	};

	private HXInviteMessageDao inviteMessgeDao;
	private HXUserDao userDao;

	/***
	 * 好友变化listener
	 *
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, HXUser> localUsers = AppInfo.sAppInstance.getContactList();
			Map<String, HXUser> toAddUsers = new HashMap<String, HXUser>();
			for (String username : usernameList) {
				HXUser user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			if (mCurrentTabIndex == INDEX_CONTACTS_IM)
				contactsFragment.refresh(ContactsFragment.REFRESH_TYPE_ADDRESS_LIST);

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, HXUser> localUsers = AppInfo.sAppInstance.getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					// 如果正在与此用户的聊天页面
					String st10 = getResources().getString(R.string.have_you_removed);
					if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
						Toast.makeText(AppInfo.sAppContext, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
					// 刷新ui
					if (mCurrentTabIndex == INDEX_CONTACTS_IM) {
						contactsFragment.refresh(ContactsFragment.REFRESH_TYPE_BOTH);
					}
				}
			});

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<HXInviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (HXInviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			HXInviteMessage msg = new HXInviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(HXInviteMessage.InviteMessageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<HXInviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (HXInviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			HXInviteMessage msg = new HXInviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(HXInviteMessage.InviteMessageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			Log.d(username, username + "拒绝了你的好友请求");
		}

	}

	/**
	 * 保存提示新消息
	 *
	 * @param msg
	 */
	private void notifyNewIviteMessage(HXInviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadLabel();
		// 刷新好友页面ui
		if (mCurrentTabIndex == INDEX_CONTACTS_IM)
			contactsFragment.refresh(ContactsFragment.REFRESH_TYPE_ADDRESS_LIST);
	}

	/**
	 * 保存邀请等msg
	 *
	 * @param msg
	 */
	private void saveInviteMsg(HXInviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		HXUser user = AppInfo.sAppInstance.getContactList().get(HXConstants.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * set head
	 *
	 * @param username
	 * @return
	 */
	HXUser setUserHead(String username) {
		HXUser user = new HXUser();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(HXConstants.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}

	/**
	 * 连接监听listener
	 *
	 */
	private class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					//contactsFragment.errorItem.setVisibility(View.GONE);
					T.showS(AppInfo.sAppContext, getString(R.string.unable_to_connect));
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			final String st1 = getResources().getString(R.string.Less_than_chat_server_connection);
			final String st2 = getResources().getString(R.string.the_current_network);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if(error == EMError.USER_REMOVED){
						// 显示帐号已经被移除
						showAccountRemovedDialog();
					}else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog();
					} else {
						//chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
						if (NetUtils.hasNetwork(AppInfo.sAppContext))
							//chatHistoryFragment.errorText.setText(st1);
							T.showS(AppInfo.sAppContext, st1);
						else
							//chatHistoryFragment.errorText.setText(st2);
							T.showS(AppInfo.sAppContext, st2);

					}
				}

			});
		}
	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
			boolean hasGroup = false;
			for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
				if (group.getGroupId().equals(groupId)) {
					hasGroup = true;
					break;
				}
			}
			if (!hasGroup)
				return;

			// 被邀请
			String st3 = getResources().getString(R.string.Invite_you_to_join_a_group_chat);
			EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
			msg.setChatType(EMMessage.ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + st3));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (mCurrentTabIndex == INDEX_CONTACTS_IM)
						contactsFragment.refresh(ContactsFragment.REFRESH_TYPE_UNREAD_MSG);
					if (CommonUtils.getTopActivity(MainLogicActivity.this).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter, String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee, String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			LUtil.i("用户 " + groupName + " 被踢出群" + EMGroupManager.getInstance().getGroup(groupName).getGroupName());
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (mCurrentTabIndex == INDEX_CONTACTS_IM)
							contactsFragment.refresh(ContactsFragment.REFRESH_TYPE_UNREAD_MSG);
						if (CommonUtils.getTopActivity(AppInfo.sAppContext).equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
					} catch (Exception e) {
						EMLog.e(TAG, "refresh exception " + e.getMessage());
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (mCurrentTabIndex == INDEX_CONTACTS_IM)
						contactsFragment.refresh(ContactsFragment.REFRESH_TYPE_UNREAD_MSG);
					if (CommonUtils.getTopActivity(MainLogicActivity.this).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
			// 用户申请加入群聊
			HXInviteMessage msg = new HXInviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(HXInviteMessage.InviteMessageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName, String accepter) {
			String st4 = getResources().getString(R.string.Agreed_to_your_group_chat_application);
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
			msg.setChatType(EMMessage.ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + st4));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (mCurrentTabIndex == INDEX_CONTACTS_IM)
						contactsFragment.refresh(ContactsFragment.REFRESH_TYPE_UNREAD_MSG);
					if (CommonUtils.getTopActivity(MainLogicActivity.this).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
			// 加群申请被拒绝，demo未实现
		}

	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		AppInfo.sAppInstance.logout(null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainLogicActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(MainLogicActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						accountRemovedBuilder = null;
						finish();
						startActivity(new Intent(MainLogicActivity.this, LoginActivity.class));
					}
				});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
			}
		}
	}

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		AppInfo.sAppInstance.logout(null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainLogicActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(MainLogicActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						startActivity(new Intent(MainLogicActivity.this, LoginActivity.class));
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra(HXConstants.IS_APP_CONFLICT, false) && !isConflictDialogShow){
			showConflictDialog();
		}else if(getIntent().getBooleanExtra(HXConstants.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow){
			showAccountRemovedDialog();
		}
	}

}
