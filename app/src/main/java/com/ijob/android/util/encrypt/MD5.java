package com.ijob.android.util.encrypt;

import android.util.Base64;

import com.ijob.android.constants.GlobalConfig;
import com.ijob.android.util.FileUtil;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.TextUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by JackieZhuang on 2015/1/10.
 */
public class MD5 {
	/**
	 * 生成摘要字节数组
	 *
	 * @param content
	 * @return
	 */
	public static byte[] genDigest(String content) {
		return genDigest(content, GlobalConfig.DEFAULT_CHARSET);
	}

	/**
	 * 生成摘要字节数组
	 *
	 * @param content 原文本内容字符串
	 * @param charset 获取字节数组编码
	 * @return
	 */
	public static byte[] genDigest(String content, String charset) {
		try {
			return genDigest(content.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			LUtil.e("genDigest", e.getMessage());
		}
		return null;
	}

	/**
	 * 生成摘要字节数组
	 *
	 * @param content 原文本内容字节数组
	 * @return
	 */
	public static byte[] genDigest(byte[] content) {
		if (content != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(content);
				return md.digest();
			} catch (Exception e) {
				LUtil.e("genDigest", e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 使用默认编码UTF-8获取输入内容字节数组，并对其进行MD5摘要加密
	 *
	 * @param content
	 * @return 十六进制格式的字符串
	 */
	public static String digestInHex(byte[] content) {
		return TextUtil.bytes2Hex(genDigest(content));
	}

	/**
	 * 对输入字节数组内容进行MD5摘要加密
	 *
	 * @param content
	 * @return 经Base64编码后的字符串
	 */
	public static String digestInBase64(byte[] content) {
		return Base64.encodeToString(content, Base64.DEFAULT);
	}

	/**
	 * 使用默认编码UTF-8获取输入内容字节数组，并对其进行MD5摘要加密
	 *
	 * @param content
	 * @param charset
	 * @return 十六进制格式的字符串
	 */
	public static String digestInHex(String content, String charset) {
		return TextUtil.bytes2Hex(genDigest(content, charset));
	}

	/**
	 * 对输入字节数组内容进行MD5摘要加密
	 *
	 * @param content
	 * @param charset
	 * @return 经Base64编码后的字符串
	 */
	public static String digestInBase64(String content, String charset) {
		try {
			return Base64.encodeToString(content.getBytes(charset), Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			LUtil.e("digestInBase64", e.getMessage());
		}
		return null;
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static String digestFileInBase64(String filePath) {
		try {
			return digestInBase64((FileUtil.readContent(filePath)
					.getBytes(GlobalConfig.DEFAULT_CHARSET)));
		} catch (UnsupportedEncodingException e) {
			LUtil.e("digestFileInBase64", e.getMessage());
		}
		return null;
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static String digestFileInHex(String filePath) {
		try {
			return digestInHex((FileUtil.readContent(filePath)
					.getBytes(GlobalConfig.DEFAULT_CHARSET)));
		} catch (UnsupportedEncodingException e) {
			LUtil.e("digestFileInHex", e.getMessage());
		}
		return null;
	}

	/**
	 * 检验输入内容与所给MD5摘要是否想等
	 *
	 * @param content
	 * @param md5Val  十六进制MD5摘要字符串
	 * @return
	 */
	public static boolean checkValidHex(byte[] content, String md5Val) {
		return md5Val.toLowerCase().equals(digestInHex(content));
	}

	/**
	 * 检验输入内容与所给MD5摘要是否想等
	 *
	 * @param content
	 * @param md5Val  Base64编码的MD5摘要字符串
	 * @return
	 */
	public static boolean checkValidBase64(byte[] content, String md5Val) {
		return md5Val.toLowerCase().equals(digestInBase64(content));
	}

	/**
	 * 检验输入内容与所给MD5摘要是否想等
	 *
	 * @param content
	 * @param md5Val  十六进制MD5摘要字符串
	 * @return
	 */
	public static boolean checkValidHex(String content, String md5Val) {
		try {
			return md5Val.toLowerCase().equals(
					digestInHex(content.getBytes(GlobalConfig.DEFAULT_CHARSET)));
		} catch (UnsupportedEncodingException e) {
			LUtil.e("checkValidHex", e.getMessage());
		}
		return false;
	}

	/**
	 * 检验输入内容与所给MD5摘要是否想等
	 *
	 * @param content
	 * @param md5Val  Base64编码的MD5摘要字符串
	 * @return
	 */
	public static boolean checkValidBase64(String content, String md5Val) {
		try {
			return md5Val.toLowerCase().equals(
					digestInBase64(content.getBytes(GlobalConfig.DEFAULT_CHARSET)));
		} catch (UnsupportedEncodingException e) {
			LUtil.e("checkValidBase64", e.getMessage());
		}
		return false;
	}

	/**
	 * 检验文件的MD5摘要与所给MD5摘要是否想等
	 *
	 * @param fileName 文件名
	 * @param md5Val   Base64编码的MD5摘要字符串
	 * @return
	 */
	public static boolean checkFileValid(String fileName, String md5Val) {
		return md5Val.toLowerCase().equals(
				digestInHex(FileUtil.readContentBytesInStandard(fileName)));
	}
}
