package com.ijob.hx.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ijob.android.R;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.db.HXInviteMessageDao;
import com.ijob.hx.db.domain.HXInviteMessage;
import com.ijob.hx.ui.adapter.NewFriendsMsgAdapter;

import java.util.List;

/**
 * Created by zsigui on 15-3-10.
 */
public class NewFriendsMsgActivity extends BaseActivity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hx_activity_new_friends_msg);

		listView = (ListView) findViewById(R.id.list);
		HXInviteMessageDao dao = new HXInviteMessageDao(this);
		List<HXInviteMessage> msgs = dao.getMessagesList();
		//设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		AppInfo.sAppInstance.getContactList().get(HXConstants.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);

	}

	public void back(View view) {
		finish();
	}


}
