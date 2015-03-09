package com.ijob.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by JackieZhuang on 2015/1/17.
 */
public class NetUtil {

	/**
	 * 连接正常，成功标志
	 */
	public static final int CONN_SUCCESS = 0;

	/**
	 * 获取服务出错
	 */
	public static final int ERROR_GET_SERVICE = -1;
	/**
	 * 无可用连接或获取失败
	 */
	public static final int ERROR_NET_UNAVAILABLE = -2;

	/**
	 * 网络类型，3G及3G+
	 */
	public static final int TYPE_NET_3G = 101;
	/**
	 * 网络类型，WIFI
	 */
	public static final int TYPE_NET_WIFI = 102;
	/**
	 * 网络类型，2G
	 */
	public static final int TYPE_NET_2G = 103;
	/**
	 * 网络类型，wap
	 */
	public static final int TYPE_NET_WAP = 104;
	/**
	 * 网络类型，未知or获取失败
	 */
	public static final int TYPE_UNKNOWN = 106;

	/**
	 * 获取当前网络状态
	 *
	 * @param contxt 传入的Context
	 * @return int类型，
	 */
	public static int getNetState(Context contxt) {
		int type = NetUtil.CONN_SUCCESS;
		ConnectivityManager conManager = (ConnectivityManager) contxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager != null) {
			NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				if (networkInfo.isAvailable() && networkInfo.isConnected()) {
				} else {
					type = NetUtil.ERROR_NET_UNAVAILABLE;
				}
			} else {
				type = NetUtil.ERROR_NET_UNAVAILABLE;
			}
		} else {
			type = NetUtil.ERROR_GET_SERVICE;
		}
		return type;
	}

	/**
	 * 获取当前网络类型
	 * 2G, 3G, WIFI, WAP
	 *
	 * @param context
	 * @return
	 */
	public static int getNetType(Context context) {
		int type = NetUtil.TYPE_UNKNOWN;

		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context
				.CONNECTIVITY_SERVICE);
		NetworkInfo actvNetInfo = connManager.getActiveNetworkInfo();
		if (actvNetInfo != null) {
			if (actvNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// WIFI
				type = NetUtil.TYPE_NET_WIFI;
			} else if (actvNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String proxyHost = android.net.Proxy.getDefaultHost();
				if (TextUtils.isEmpty(proxyHost)) {
					if (isFastNet(context)) {
						// 3G以上为高速网络
						type = NetUtil.TYPE_NET_3G;
					} else {
						type = NetUtil.TYPE_NET_2G;
					}
				} else {
					// WAP不需要设置代理
					type = NetUtil.TYPE_NET_WAP;
				}
			}
		}
		return type;
	}

	/**
	 * 判断是否为高速网络,区分2G和3G+
	 *
	 * @return
	 */
	private static boolean isFastNet(Context context) {
		boolean result = false;
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				result = false; // ~ 50-100 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				result = false; // ~ 14-64 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				result = false; // ~ 50-100 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				result = true; // ~ 400-1000 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				result = true; // ~ 600-1400 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				result = false; // ~ 100 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				result = true; // ~ 2-14 Mbps
				break;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				result = true; // ~ 700-1700 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				result = true; // ~ 1-23 Mbps
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				result = true; // ~ 400-7000 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				result = true; // ~ 1-2 Mbps
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				result = true; // ~ 5 Mbps
				break;
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				result = true; // ~ 10-20 Mbps
				break;
			case TelephonyManager.NETWORK_TYPE_IDEN:
				result = false; // ~25 kbps
				break;
			case TelephonyManager.NETWORK_TYPE_LTE:
				result = true; // ~ 10+ Mbps
				break;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				result = false;
				break;
		}
		return result;
	}
}
