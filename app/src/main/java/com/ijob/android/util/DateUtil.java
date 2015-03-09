package com.ijob.android.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by JackieZhuang on 2015/1/13.
 */
public class DateUtil {

	public static final String DEFAULT_FORMAT_TYPE = "hhhh/MM/dd HH:mm:ss";

	public static String getVal(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
		return sdf.format(getTime());
	}

	/**
	 * 获取起始到现在的时间戳(ms)
	 * @return
	 */
	public static long getTimeInMillis() {
		return Calendar.getInstance(Locale.CHINA).getTimeInMillis();
	}

	/**
	 * 获取Unix时间戳(ms)
	 * @return
	 */
	public static long getGMTUnixTimeInMillis() {
		return getTimeInMillis() - TimeZone.getDefault().getRawOffset();
	}

	/**
	 * 获取起始到现在的日期
	 * @return
	 */
	public static Date getTime() {
		return Calendar.getInstance(Locale.CHINA).getTime();
	}
}
