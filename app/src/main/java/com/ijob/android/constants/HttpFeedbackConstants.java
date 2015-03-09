package com.ijob.android.constants;

/**
 * Created by JackieZhuang on 2015/1/11.
 */
public class HttpFeedbackConstants {
	// 请求成功失败状态码
	public static final int CODE_OK = 0x0;
	public static final int CODE_FAILED = 0x1;

	// 获取数据的JSON对象键名
	public static final String TAG_CODE = "code";
	public static final String TAG_ERROR_CODE = "error_code";
	public static final String TAG_DATA = "data";
	public static final String TAG_MSG = "msg";

	// 请求失败状态码
	public static final int FAILED_SERVER_ERR = 0x101;
	public static final int FAILED_GET_INFO = 0x102;
	public static final int FAILED_NETWORK_ERR = 0x201;
	public static final int FAILED_PARSE_JSON = 0x301;
	public static final int FAILED_CLIENT_ERR = 0x401;

	// 请求失败状态码通常对应的错误信息
	public static final String FAILED_STR_SERVER_ERR = "服务器出错";
	public static final String FAILED_STR_GET_INFO = "信息获取失败(请求参数错误)";
	public static final String FAILED_STR_NETWORK_ERR = "网络异常";
	public static final String FAILED_STR_PARSE_JSON = "解析JSON字符串失败";
	public static final String FAILED_STR_CLIENT_ERR = "本地操作异常";
}
