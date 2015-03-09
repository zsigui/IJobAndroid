package com.ijob.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JackieZhuang on 2015/1/11.
 */
public class SPUtil {
	private static final String PREFS_NAME = "IJOB_PREFS";

	public static final String DEFAULT_STRING = "";
	public static final long DEFAULT_LONG = -1;
	public static final int DEFAULT_INT = -1;
	public static final float DEFAULT_FLOAT = -1.0f;
	public static final boolean DEFAULT_BOOLEAN = false;

	public static String getString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, DEFAULT_STRING);
	}

	public static long getLong(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getLong(key, DEFAULT_LONG);
	}

	public static int getInt(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, DEFAULT_INT);
	}

	public static Float getFloat(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getFloat(key, DEFAULT_FLOAT);
	}


	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, DEFAULT_BOOLEAN);
	}


	public static void writeBoolean(Context context, String key, boolean val) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, val);
		editor.commit();
	}

	public static void writeLong(Context context, String key, long val) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putLong(key, val);
		editor.commit();
	}

	public static void writeInt(Context context, String key, int val) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, val);
		editor.commit();
	}

	public static void writeFloat(Context context, String key, float val) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putFloat(key, val);
		editor.commit();
	}

	public static void writeString(Context context, String key, String val) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, val);
		editor.commit();
	}
}
