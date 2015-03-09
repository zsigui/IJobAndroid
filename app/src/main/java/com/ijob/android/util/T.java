package com.ijob.android.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 显示Toast类
 * Created by JackieZhuang on 2015/1/11.
 */
public class T {
	public static void show(Context context, CharSequence msg, int duration) {
		Toast.makeText(context, msg, duration).show();
}

	public static void showL(Context context, CharSequence msg) {
		T.show(context, msg, Toast.LENGTH_LONG);
	}

	public static void showS(Context context, CharSequence msg) {
		T.show(context, msg, Toast.LENGTH_SHORT);
	}

	public static void showInCenter(Context context, CharSequence msg, int duration) {
		Toast t = Toast.makeText(context, msg, duration);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}

	public static void showLInCenter(Context context, CharSequence msg) {
		showInCenter(context, msg, Toast.LENGTH_LONG);
	}

	public static void showSInCenter(Context context, CharSequence msg) {
		showInCenter(context, msg, Toast.LENGTH_SHORT);
	}
}
