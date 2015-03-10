package com.ijob.hx.sdk.impl;

import android.content.Context;

import com.ijob.hx.db.DbOpenHelper;
import com.ijob.hx.db.HXUserDao;
import com.ijob.hx.db.domain.HXUser;
import com.ijob.hx.sdk.DefaultHXSDKModel;

import java.util.List;
import java.util.Map;

/**
 * Created by JackieZhuang on 2015/3/9.
 */
public class DemoHXSDKModel extends DefaultHXSDKModel {

	public DemoHXSDKModel(Context ctx) {
		super(ctx);
	}

	// demo will use HuanXin roster
	public boolean getUseHXRoster() {
		return true;
	}

	// demo will switch on debug mode
	public boolean isDebugMode() {
		return true;
	}

	public boolean saveContactList(List<HXUser> contactList) {
		HXUserDao dao = new HXUserDao(context);
		dao.saveContactList(contactList);
		return true;
	}

	public Map<String, HXUser> getContactList() {
		HXUserDao dao = new HXUserDao(context);
		return dao.getContactList();
	}

	public void closeDB() {
		DbOpenHelper.getInstance(context).closeDB();
	}

	@Override
	public String getAppProcessName() {
		return context.getPackageName();
	}
}
