package com.ijob.android.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JackieZhuang on 2015/1/11.
 */
public class TextUtil {
	/**
	 * 将字节数组转换成十六进制字符串
	 *
	 * @param bs
	 * @return
	 */
	public static String bytes2Hex(byte[] bs) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bs) {
			int bt = b & 0xff;
			if (bt < 16) {
				builder.append(0);
			}
			builder.append(Integer.toHexString(bt));
		}
		return builder.toString();
	}

	/**
	 * 将十六进制字符串转换成字节数组
	 *
	 * @param hex
	 * @return
	 */
	public static byte[] hex2Bytes(String hex) {
		if (hex.length() % 2 == 1)
			hex += '0';
		byte[] result = new byte[hex.length()/2];
		char[] cs = hex.toLowerCase(Locale.CHINA).toCharArray();
		for (int i = 0; i < result.length; i++) {
			int pos = i * 2;
			result[i] = (byte) (char2Byte(cs[pos]) << 4 | char2Byte(cs[pos + 1]));
		}
		return result;
	}

	private static byte char2Byte(char c) {
		return (byte) "0123456789abcdefg".indexOf(c);
	}

	/**
	 * 根据匹配模式查找字符串，并返回第一次查找到的字符串，或没找到返回null
	 *
	 * @param content 待匹配的文本内容
	 * @param regex 匹配模式串
	 * @param getIndex 需要获取的匹配串下标，从1开始
	 * @return
	 */
	public static String findFirst(String content, String regex, int getIndex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group(getIndex);
		}
		return null;
	}

	/**
	 * 根据匹配模式查找字符串，并返回所有查找到的字符串，或没找到返回null
	 *
	 * @param content 待匹配的文本内容
	 * @param regex 匹配模式串
	 * @param getIndex 需要获取的匹配串下标，从1开始
	 * @return
	 */
	public static List<String> findAll(String content, String regex, int getIndex) {
		ArrayList<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			result.add(matcher.group(getIndex));
		}
		return result.isEmpty()? null : result;
	}

	/**
	 * 判断所给文本内容与模式串是否完全匹配
	 *
	 * @param content
	 * @param regex
	 * @return
	 */
	public static boolean isMatch(String content, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}

	/**
	 * 根据所给参数构造JSONObject对象
	 * <br /> 需注意参数传入格式
	 *
	 * @param params 键值相间的不定长度数组，要求格式 key1, val1, key2, val2 ...
	 */
	public static JSONObject initJSONObj(Object ... params) throws JSONException {
		JSONObject obj = new JSONObject();
		if (params != null) {
			for (int i = 0; i < params.length; i += 2) {
				obj.put((String) params[i], params[i + 1]);
			}
		}
		return obj;
	}

	/**
	 * 根据所给参数构造JSONArray对象
	 */
	public static JSONArray initJSONArray(Object ... params) throws JSONException {
		JSONArray obj = new JSONArray();
		if (params != null) {
			for (int i = 0; i < params.length; i += 2) {
				obj.put(params);
			}
		}
		return obj;
	}
}
