package com.ijob.hx.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.ijob.android.R;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.db.domain.HXUser;
import com.ijob.hx.ui.adapter.ContactAdapter;
import com.ijob.hx.ui.widget.Sidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JackieZhuang on 2015/3/9.
 */
public class GroupPickContactsActivity extends BaseActivity {
	private ListView listView;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hx_activity_group_pick_contacts);

		// String groupName = getIntent().getStringExtra("groupName");
		String groupId = getIntent().getStringExtra("groupId");
		if (groupId == null) {// 创建群组
			isCreatingNewGroup = true;
		} else {
			// 获取此群组的成员列表
			EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
			exitingMembers = group.getMembers();
		}
		if(exitingMembers == null)
			exitingMembers = new ArrayList<String>();
		// 获取好友列表
		final List<HXUser> alluserList = new ArrayList<HXUser>();
		for (HXUser user : AppInfo.sAppInstance.getContactList().values()) {
			if (!user.getUsername().equals(HXConstants.NEW_FRIENDS_USERNAME) & !user.getUsername().equals(HXConstants.GROUP_USERNAME))
				alluserList.add(user);
		}
		// 对list进行排序
		Collections.sort(alluserList, new Comparator<HXUser>() {
			@Override
			public int compare(HXUser lhs, HXUser rhs) {
				return (lhs.getUsername().compareTo(rhs.getUsername()));

			}
		});

		listView = (ListView) findViewById(R.id.list);
		contactAdapter = new PickContactAdapter(this, R.layout.hx_listitem_row_contact_with_checkbox, alluserList);
		listView.setAdapter(contactAdapter);
		((Sidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
	}

	/**
	 * 确认选择的members
	 *
	 * @param v
	 */
	public void save(View v) {
		setResult(RESULT_OK, new Intent().putExtra("newmembers", getToBeAddMembers().toArray(new String[0])));
		finish();
	}

	/**
	 * 获取要被添加的成员
	 *
	 * @return
	 */
	private List<String> getToBeAddMembers() {
		List<String> members = new ArrayList<String>();
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i).getUsername();
			if (contactAdapter.isCheckedArray[i] && !exitingMembers.contains(username)) {
				members.add(username);
			}
		}

		return members;
	}

	/**
	 * adapter
	 */
	private class PickContactAdapter extends ContactAdapter {

		private boolean[] isCheckedArray;

		public PickContactAdapter(Context context, int resource, List<HXUser> users) {
			super(context, resource, users);
			isCheckedArray = new boolean[users.size()];
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
//			if (position > 0) {
			final String username = getItem(position).getUsername();
			// 选择框checkbox
			final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
			if(exitingMembers != null && exitingMembers.contains(username)){
				checkBox.setButtonDrawable(R.drawable.checkbox_bg_gray_selector);
			}else{
				checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
			}
			if (checkBox != null) {
				// checkBox.setOnCheckedChangeListener(null);

				checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// 群组中原来的成员一直设为选中状态
						if (exitingMembers.contains(username)) {
							isChecked = true;
							checkBox.setChecked(true);
						}
						isCheckedArray[position] = isChecked;
						//如果是单选模式
						if (isSignleChecked && isChecked) {
							for (int i = 0; i < isCheckedArray.length; i++) {
								if (i != position) {
									isCheckedArray[i] = false;
								}
							}
							contactAdapter.notifyDataSetChanged();
						}

					}
				});
				// 群组中原来的成员一直设为选中状态
				if (exitingMembers.contains(username)) {
					checkBox.setChecked(true);
					isCheckedArray[position] = true;
				} else {
					checkBox.setChecked(isCheckedArray[position]);
				}
			}
//			}
			return view;
		}
	}

	public void back(View view){
		finish();
	}

}
