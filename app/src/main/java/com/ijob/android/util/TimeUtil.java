package com.ijob.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JackieZhuang on 2015/2/3.
 */
public class TimeUtil {

	public static String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}
}
