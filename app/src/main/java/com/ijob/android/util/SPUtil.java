package com.ijob.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JackieZhuang on 2015/1/11.
 */
public class SPUtil {
	private final String PREFS_NAME = "IJOB_PREFS";

	public final String DEFAULT_STRING = "";
	public final long DEFAULT_LONG = -1;
	public final int DEFAULT_INT = -1;
	public final float DEFAULT_FLOAT = -1.0f;
	public final boolean DEFAULT_BOOLEAN = false;

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static boolean isNeedRebuild = true;
	private static SPUtil mInstance;

	public static synchronized void init(Context context, String prefs_file, int mode) {
		if (isNeedRebuild) {
			mInstance = new SPUtil(context, prefs_file, mode);
			isNeedRebuild = false;
		}
	}

	private SPUtil(Context context, String prefs_file, int mode) {
		sp = context.getSharedPreferences(prefs_file, mode);
		editor = sp.edit();
	}

	/**
	 * call init() to build a SharedPreferences Instance originly
	 *
	 * @return
	 */
	public static SPUtil getInstance() {
		if (mInstance == null) {
			throw new RuntimeException("must call init() before");
		}
		return mInstance;
	}


	public String getString(String key) {
		return sp.getString(key, DEFAULT_STRING);
	}

	public long getLong(String key) {
		return sp.getLong(key, DEFAULT_LONG);
	}

	public int getInt(String key) {
		return sp.getInt(key, DEFAULT_INT);
	}

	public Float getFloat(String key) {
		return sp.getFloat(key, DEFAULT_FLOAT);
	}


	public boolean getBoolean(String key) {
		return sp.getBoolean(key, DEFAULT_BOOLEAN);
	}


	public void writeBoolean(String key, boolean val) {
		editor.putBoolean(key, val);
		editor.commit();
	}

	public void writeLong(String key, long val) {
		editor.putLong(key, val);
		editor.commit();
	}

	public void writeInt(String key, int val) {
		editor.putInt(key, val);
		editor.commit();
	}

	public void writeFloat(String key, float val) {
		editor.putFloat(key, val);
		editor.commit();
	}

	public void writeString(String key, String val) {
		editor.putString(key, val);
		editor.commit();
	}

	public void destory() {
		isNeedRebuild = true;
		mInstance = null;
	}
}
