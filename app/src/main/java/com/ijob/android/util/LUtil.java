package com.ijob.android.util;

import android.util.Log;

/**
 * Created by JackieZhuang on 2015/1/10.
 */
public class LUtil {
	private static final boolean IS_DEBUG = true;
	private static final int DEFAULT_LEVEL = Log.INFO;
	private static final String DEFAULT_LOG_TAG = "IJOB_TAG";

	public static void d(String tag, String msg) {
		if (IS_DEBUG && Log.DEBUG >= DEFAULT_LEVEL) {
			Log.d(tag, msg);
		}
	}
	public static void i(String tag, String msg) {
		if (IS_DEBUG && Log.INFO >= DEFAULT_LEVEL) {
			Log.i(tag, msg);
		}
	}
	public static void w(String tag, String msg) {
		if (IS_DEBUG && Log.WARN >= DEFAULT_LEVEL) {
			Log.w(tag, msg);
		}
	}
	public static void e(String tag, String msg) {
		if (IS_DEBUG && Log.ERROR >= DEFAULT_LEVEL) {
			Log.e(tag, msg);
		}
	}

	public static void d(String msg) {
		d(DEFAULT_LOG_TAG, msg);
	}

	public static void i(String msg) {
		i(DEFAULT_LOG_TAG, msg);
	}

	public static void w(String msg) {
		w(DEFAULT_LOG_TAG, msg);
	}

	public static void e(String msg) {
		e(DEFAULT_LOG_TAG, msg);
	}
}
