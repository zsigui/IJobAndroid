package com.ijob.hx.sdk.impl;

import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.ijob.android.ui.activity.MainActivity;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.db.domain.HXUser;
import com.ijob.hx.sdk.HXSDKHelper;
import com.ijob.hx.sdk.HXSDKModel;
import com.ijob.hx.ui.activity.ChatActivity;
import com.ijob.hx.ui.receiver.CallReceiver;
import com.ijob.hx.utils.CommonUtils;

import java.util.Map;

/**
 * Created by JackieZhuang on 2015/3/3.
 */
public class DemoHXSDKHelper extends HXSDKHelper {

	/**
	 * contact list in cache
	 */
	private Map<String, HXUser> contactList;
	private CallReceiver callReceiver;

	@Override
	protected void initHXOptions() {
		super.initHXOptions();
		// you can also get EMChatOptions to set related SDK options
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		options.setUseRoster(true);
	}

	@Override
	protected OnMessageNotifyListener getMessageNotifyListener() {
		// 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		return new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				// 设置状态栏的消息提示，可以根据message的类型做相应提示
				String ticker = CommonUtils.getMessageDigest(message, appContext);
				if (message.getType() == EMMessage.Type.TXT)
					ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
				return message.getFrom() + ": " + ticker;
			}

			/**
			 * @param message
			 * @param fromUsersNum
			 * @param messageNum
			 * @return
			 */
			@Override
			public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
				if (fromUsersNum == 1) {
					return message.getFrom() + "发来了" + String.valueOf(messageNum) + "条新消息";
				} else {
					return String.valueOf(fromUsersNum) + "个朋友，发来了" + String.valueOf(messageNum) + "条消息";
				}
			}

			@Override
			public String onSetNotificationTitle(EMMessage message) {
				//修改标题,这里使用默认
				return null;
			}

			@Override
			public int onSetSmallIcon(EMMessage message) {
				//设置小图标
				return 0;
			}
		};
	}

	@Override
	protected OnNotificationClickListener getNotificationClickListener() {
		return new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(appContext, ChatActivity.class);
				EMMessage.ChatType chatType = message.getChatType();
				if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { // 群聊信息
					// message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		};
	}

	@Override
	protected void onConnectionConflict() {
		Intent intent = new Intent(appContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("conflict", true);
		appContext.startActivity(intent);
	}

	@Override
	protected void onCurrentAccountRemoved() {
		Intent intent = new Intent(appContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(HXConstants.ACCOUNT_REMOVED, true);
		appContext.startActivity(intent);
	}


	@Override
	protected void initListener() {
		super.initListener();
		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
		if (callReceiver == null)
			callReceiver = new CallReceiver();
		appContext.registerReceiver(callReceiver, callFilter);
	}

	@Override
	protected HXSDKModel createModel() {
		return new DemoHXSDKModel(appContext);
	}

	/**
	 * get demo HX SDK Model
	 */
	public DemoHXSDKModel getModel() {
		return (DemoHXSDKModel) hxModel;
	}

	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, HXUser> getContactList() {
		if (getHXId() != null && contactList == null) {
			contactList = ((DemoHXSDKModel) getModel()).getContactList();
		}

		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, HXUser> contactList) {
		this.contactList = contactList;
	}

	@Override
	public void logout(final EMCallBack callback) {
		endCall();
		super.logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				setContactList(null);
				getModel().closeDB();
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

		});
	}

	void endCall() {
		try {
			EMChatManager.getInstance().endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
