package com.ijob.hx.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.ijob.android.R;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.db.HXInviteMessageDao;
import com.ijob.hx.db.HXUserDao;
import com.ijob.hx.db.domain.HXUser;
import com.ijob.hx.ui.activity.ChatActivity;
import com.ijob.hx.ui.activity.GroupsActivity;
import com.ijob.hx.ui.activity.NewFriendsMsgActivity;
import com.ijob.hx.ui.adapter.ContactAdapter;
import com.ijob.hx.ui.widget.Sidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zsigui on 15-3-10.
 */
public class ContactsListFragment extends BaseFragment{

	private ContactAdapter mAdapter;
	private ListView mlvContacts;
	private Sidebar mSidebar;
	private EditText metSearch;
	private boolean mIsHidden;
	private ImageButton mivClearSearch;
	private List<HXUser> mContacts;
	private List<String> mBlacks;
	private InputMethodManager mInputMethodManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.hx_fragment_contacts_list, container, false);
			mlvContacts = (ListView) convertView.findViewById(R.id.list);
			mSidebar = (Sidebar) convertView.findViewById(R.id.sidebar);
		}
		return convertView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		mContacts = new ArrayList<HXUser>();
		mBlacks = EMContactManager.getInstance().getBlackListUsernames();
		getContactList();
		//搜索框
		metSearch = (EditText) getView().findViewById(R.id.query);
		metSearch.setHint(R.string.search);
		mivClearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
		metSearch.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mAdapter.getFilter().filter(s);
				if (s.length() > 0) {
					mivClearSearch.setVisibility(View.VISIBLE);
				} else {
					mivClearSearch.setVisibility(View.INVISIBLE);

				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		mivClearSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				metSearch.getText().clear();
				hideSoftKeyboard();
			}
		});

		// 设置adapter
		mAdapter = new ContactAdapter(getActivity(), R.layout.hx_listitem_row_contact, mContacts);
		mlvContacts.setAdapter(mAdapter);
		mlvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String username = mAdapter.getItem(position).getUsername();
				if (HXConstants.NEW_FRIENDS_USERNAME.equals(username)) {
					// 进入申请与通知页面
					HXUser user = AppInfo.sAppInstance.getContactList().get(HXConstants.NEW_FRIENDS_USERNAME);
					user.setUnreadMsgCount(0);
					startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
				} else if (HXConstants.GROUP_USERNAME.equals(username)) {
					// 进入群聊列表页面
					startActivity(new Intent(getActivity(), GroupsActivity.class));
				} else {
					// 直接进入聊天页面，实际一般是进入用户详情页
					startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", mAdapter.getItem(position).getUsername()));
				}
			}
		});
		mlvContacts.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						mInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

		registerForContextMenu(mlvContacts);
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 长按前两个不弹menu
		if (((AdapterView.AdapterContextMenuInfo) menuInfo).position > 1) {
			getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			HXUser tobeDeleteUser = mAdapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此联系人
			deleteContact(tobeDeleteUser);
			// 删除相关的邀请消息
			HXInviteMessageDao dao = new HXInviteMessageDao(getActivity());
			dao.deleteMessage(tobeDeleteUser.getUsername());
			return true;
		}else if(item.getItemId() == R.id.add_to_blacklist){
			HXUser user = mAdapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
			moveToBlacklist(user.getUsername());
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.mIsHidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!mIsHidden) {
			refresh();
		}
	}

	/**
	 * 删除联系人
	 *
	 * @param toDeleteUser
	 */
	public void deleteContact(final HXUser toDeleteUser) {
		String st1 = getResources().getString(R.string.deleting);
		final String st2 = getResources().getString(R.string.Delete_failed);
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().deleteContact(toDeleteUser.getUsername());
					// 删除db和内存中此用户的数据
					HXUserDao dao = new HXUserDao(getActivity());
					dao.deleteContact(toDeleteUser.getUsername());
					AppInfo.sAppInstance.getContactList().remove(toDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							mAdapter.remove(toDeleteUser);
							mAdapter.notifyDataSetChanged();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});

				}

			}
		}).start();

	}

	/**
	 * 把user移入到黑名单
	 */
	private void moveToBlacklist(final String username){
		final ProgressDialog pd = new ProgressDialog(getActivity());
		String st1 = getResources().getString(R.string.Is_moved_into_blacklist);
		final String st2 = getResources().getString(R.string.Move_into_blacklist_success);
		final String st3 = getResources().getString(R.string.Move_into_blacklist_failure);
		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					//加入到黑名单
					EMContactManager.getInstance().addUserToBlackList(username,false);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
							refresh();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), st3, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}

	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					getContactList();
					mAdapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */
	private void getContactList() {
		mContacts.clear();
		//获取本地好友列表
		Map<String, HXUser> users = AppInfo.sAppInstance.getContactList();
		Iterator<Map.Entry<String, HXUser>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, HXUser> entry = iterator.next();
			if (!entry.getKey().equals(HXConstants.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(HXConstants.GROUP_USERNAME)
					&& !mBlacks.contains(entry.getKey()))
				mContacts.add(entry.getValue());
		}
		// 排序
		Collections.sort(mContacts, new Comparator<HXUser>() {

			@Override
			public int compare(HXUser lhs, HXUser rhs) {
				return lhs.getUsername().compareTo(rhs.getUsername());
			}
		});

		// 加入"申请与通知"和"群聊"
		if(users.get(HXConstants.GROUP_USERNAME) != null)
			mContacts.add(0, users.get(HXConstants.GROUP_USERNAME));
		// 把"申请与通知"添加到首位
		if(users.get(HXConstants.NEW_FRIENDS_USERNAME) != null)
			mContacts.add(0, users.get(HXConstants.NEW_FRIENDS_USERNAME));
	}

	void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				mInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/*if(((MainActivity)getActivity()).isConflict){
			outState.putBoolean("isConflict", true);
		}else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
			outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
		}*/

	}
}
