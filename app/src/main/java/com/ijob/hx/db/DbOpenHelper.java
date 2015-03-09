package com.ijob.hx.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ijob.hx.sdk.HXSDKHelper;

/**
 * Created by JackieZhuang on 2015/3/4.
 */
public class DbOpenHelper extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	private static DbOpenHelper instance;

	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ HXUserDao.TABLE_NAME + " ("
			+ HXUserDao.COLUMN_NAME_NICK +" TEXT, "
			+ HXUserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

	private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
			+ HXInviteMessageDao.TABLE_NAME + " ("
			+ HXInviteMessageDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ HXInviteMessageDao.COLUMN_NAME_FROM + " TEXT, "
			+ HXInviteMessageDao.COLUMN_NAME_GROUP_ID + " TEXT, "
			+ HXInviteMessageDao.COLUMN_NAME_GROUP_Name + " TEXT, "
			+ HXInviteMessageDao.COLUMN_NAME_REASON + " TEXT, "
			+ HXInviteMessageDao.COLUMN_NAME_STATUS + " INTEGER, "
			+ HXInviteMessageDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
			+ HXInviteMessageDao.COLUMN_NAME_TIME + " TEXT); ";



	private DbOpenHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}

	public static DbOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}

	private static String getUserDatabaseName() {
		return  HXSDKHelper.getInstance().getHXId() + "_demo.db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
		db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}
}
