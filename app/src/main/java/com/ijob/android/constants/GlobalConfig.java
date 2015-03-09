package com.ijob.android.constants;

/**
 * Created by JackieZhuang on 2015/1/10.
 */
public class GlobalConfig {
	/**
	 * 全局默认编码设置为UTF-8
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 请求服务器地址
	 */
	public static final String DEFAULT_HOST_NAME = "http://192.168.201.71:8080/IJobWeb";
	public static final String TO_GET_SALT = DEFAULT_HOST_NAME + "/getSalt";
	public static final String TO_LOGIN = DEFAULT_HOST_NAME + "/toLogin";
	public static final String TO_REGISTER = DEFAULT_HOST_NAME + "/toRegister";
	public static final String TO_GET_USERINFO = DEFAULT_HOST_NAME + "/toGetUserInfo";
	public static final String TO_GET_ZPHINFO = DEFAULT_HOST_NAME + "/toGetZphInfo";
	public static final String TO_GET_DESE_KEY = DEFAULT_HOST_NAME + "/getDeseKey";

	/**
	 * SharedPreferences文件存放名
	 */
	public static final String SP_USER_FILENAME = "IJOB_USER";

	public static String DESE_KEY = "";
	public static String DESE_KEY_NO = "";
}
