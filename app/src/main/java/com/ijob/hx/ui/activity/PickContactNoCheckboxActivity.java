package com.ijob.hx.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ijob.android.R;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.db.domain.HXUser;
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
public class PickContactNoCheckboxActivity extends BaseActivity {

	private ListView listView;
	private Sidebar sidebar;
	protected ContactAdapter contactAdapter;
	private List<HXUser> contactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hx_activity_pick_contact_no_checkbox);
		listView = (ListView) findViewById(R.id.list);
		sidebar = (Sidebar) findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		contactList = new ArrayList<HXUser>();
		// 获取设置contactlist
		getContactList();
		// 设置adapter
		contactAdapter = new ContactAdapter(this, R.layout.hx_listitem_row_contact, contactList);
		listView.setAdapter(contactAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemClick(position);
			}
		});

	}

	protected void onListItemClick(int position) {
//		if (position != 0) {
		setResult(RESULT_OK, new Intent().putExtra("HXUsername", contactAdapter.getItem(position)
				.getUsername()));
		finish();
//		}
	}

	public void back(View view) {
		finish();
	}

	private void getContactList() {
		contactList.clear();
		Map<String, HXUser> HXUsers = AppInfo.sAppInstance.getContactList();
		Iterator<Map.Entry<String, HXUser>> iterator = HXUsers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, HXUser> entry = iterator.next();
			if (!entry.getKey().equals(HXConstants.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(HXConstants.GROUP_USERNAME))
				contactList.add(entry.getValue());
		}
		// 排序
		Collections.sort(contactList, new Comparator<HXUser>() {

			@Override
			public int compare(HXUser lhs, HXUser rhs) {
				return lhs.getUsername().compareTo(rhs.getUsername());
			}
		});
	}

}
