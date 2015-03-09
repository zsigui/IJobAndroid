package com.ijob.hx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.easemob.util.HanziToPinyin;
import com.ijob.hx.constants.HXContants;
import com.ijob.hx.db.domain.HXUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JackieZhuang on 2015/3/4.
 */
public class HXUserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_IS_STRANGER = "is_stranger";

	private DbOpenHelper dbHelper;

	public HXUserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	/**
	 * 保存好友list
	 *
	 * @param contactList
	 */
	public void saveContactList(List<HXUser> contactList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			for (HXUser HXUser : contactList) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, HXUser.getUsername());
				if(HXUser.getNick() != null)
					values.put(COLUMN_NAME_NICK, HXUser.getNick());
				db.replace(TABLE_NAME, null, values);
			}
		}
	}

	/**
	 * 获取好友list
	 *
	 * @return
	 */
	public Map<String, HXUser> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, HXUser> users = new HashMap<String, HXUser>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
				HXUser HXUser = new HXUser();
				HXUser.setUsername(username);
				HXUser.setNick(nick);
				String headerName = null;
				if (!TextUtils.isEmpty(HXUser.getNick())) {
					headerName = HXUser.getNick();
				} else {
					headerName = HXUser.getUsername();
				}

				if (username.equals(HXContants.NEW_FRIENDS_USERNAME) || username.equals(HXContants.GROUP_USERNAME)) {
					HXUser.setHeader("");
				} else if (Character.isDigit(headerName.charAt(0))) {
					HXUser.setHeader("#");
				} else {
					HXUser.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
							.get(0).target.substring(0, 1).toUpperCase());
					char header = HXUser.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						HXUser.setHeader("#");
					}
				}
				users.put(username, HXUser);
			}
			cursor.close();
		}
		return users;
	}

	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String username){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{username});
		}
	}

	/**
	 * 保存一个联系人
	 * @param HXUser
	 */
	public void saveContact(HXUser HXUser){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, HXUser.getUsername());
		if(HXUser.getNick() != null)
			values.put(COLUMN_NAME_NICK, HXUser.getNick());
		if(db.isOpen()){
			db.replace(TABLE_NAME, null, values);
		}
	}
}
