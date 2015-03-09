package com.ijob.android.ui.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ijob.android.constants.GlobalConfig;
import com.ijob.android.constants.HttpFeedbackConstants;
import com.ijob.android.constants.ParamConstants;
import com.ijob.android.model.User;
import com.ijob.android.util.DateUtil;
import com.ijob.android.util.HttpUtil;
import com.ijob.android.util.TelUtil;
import com.ijob.android.util.TextUtil;
import com.ijob.android.util.encrypt.DESede;
import com.ijob.android.util.encrypt.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by JackieZhuang on 2015/1/13.
 */
public class RegAsyncTask extends Abs_UAAsyncTask {

	public RegAsyncTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected String doInBgToImpl(User[] users) {
		assert (users.length == 1 && users[0] != null);
		User user = users[0];
		try {
			// 1.请求获取3DES密钥
			if (TextUtils.isEmpty(GlobalConfig.DESE_KEY) || TextUtils.isEmpty(GlobalConfig.DESE_KEY_NO)) {
				String keyno = TelUtil.getInfoByName(mContext, TelUtil.SUBSCRIBER_ID);
				GlobalConfig.DESE_KEY_NO = (TextUtils.isEmpty(keyno) || "null".equals(keyno)) ? String.valueOf
						(DateUtil.getGMTUnixTimeInMillis()) : keyno;
				String jsonKeyStr = HttpUtil.doGet(GlobalConfig.TO_GET_DESE_KEY, ParamConstants.PARAM_DESE_KEY_NO +
						"=" + GlobalConfig.DESE_KEY_NO);
				String tmpReturn = dealReturnJsonStr(jsonKeyStr, false);
				if (tmpReturn != null) {
					return tmpReturn;
				}
			}

			// 2.生成随机盐值
			String salt = MD5.digestInBase64(DateUtil.getVal("yyyyMMddHHmmsss"), GlobalConfig.DEFAULT_CHARSET)
					.substring(0, 20);
			user.setSalt(salt);

			try {
				// 3.使用salt值进行MD5加密，MD5(pwd + salt)
				String pwdMd5 = MD5.digestInHex(user.getPassword() + user.getSalt(), GlobalConfig.DEFAULT_CHARSET);
				user.setPassword(pwdMd5);

				// 4.对发送信息进行3DES加密，并POST请求
				JSONObject postValJson = TextUtil.initJSONObj(ParamConstants.PARAM_UNAME, user.getName(),
						ParamConstants.PARAM_UPWD, pwdMd5, ParamConstants.PARAM_UEMAIL, user.getEmail(),
						ParamConstants.PARAM_UMOBILEPHONE, user.getMobilePhone(), ParamConstants.PARAM_UNICKNAME,
						user.getNickName(), ParamConstants.PARAM_USALT, user.getSalt(), ParamConstants.PARAM_UGENDER,
						user.getGender(),
						ParamConstants.PARAM_USEC_QUESTION, user.getSecQuestion(), ParamConstants.PARAM_USEC_ANSWER,
						user.getSecAnswer());
				String postVal = DESede.encryptInHex(postValJson.toString(), GlobalConfig.DESE_KEY);
				HashMap<String, String> mapParams = new HashMap<>(2);
				mapParams.put(ParamConstants.PARAM_POST_DECRYPT_DATA, postVal);
				mapParams.put(ParamConstants.PARAM_DESE_KEY_NO, GlobalConfig.DESE_KEY_NO);
				// 执行请求进行登录
				// 成功返回结构 {"code":0x0, "data":{"uid":"1", "uemail":"", ... }}
				// 非网络异常失败返回结构 {"code":0x1, "error_code":0x101/0x102, "msg":"服务器出错/信息获取失败(请求参数错误)"}
				// 以上服务器返回均使用3DES进行加密，故需要解密
				String jsonLoginStr = HttpUtil.doPost(GlobalConfig.TO_LOGIN, mapParams);
				return dealReturnJsonStr(jsonLoginStr, true);
			} catch (JSONException expected) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return TextUtil.initJSONObj(HttpFeedbackConstants.TAG_CODE, HttpFeedbackConstants.CODE_FAILED,
						HttpFeedbackConstants.TAG_ERROR_CODE, HttpFeedbackConstants.FAILED_CLIENT_ERR,
						HttpFeedbackConstants.TAG_MSG, HttpFeedbackConstants.FAILED_STR_CLIENT_ERR).toString();
			} catch (JSONException expected) {
			}
		}
		return null;
	}

	/**
	 * <br /> 1. 处理获取3DES密钥的返回字符串：如果出错，返回错误信息JSON结构；如果返回为空，代表执行正常
	 * <br /> 2. 处理发送注册请求返回的字符串：如果正常，返回post response信息，包含id；如果返回空代表执行出错
	 *
	 * @param jsonStr   http请求后返回的字符串
	 * @param isRegBack 是否为注册获取信息部分（false时判断user）
	 * @return
	 */
	private String dealReturnJsonStr(String jsonStr, boolean isRegBack) {
		String retResult = null;
		try {
			if (!TextUtils.isEmpty(jsonStr)) {
				JSONObject jsonKey = new JSONObject(jsonStr);
				if (jsonKey.getInt(HttpFeedbackConstants.TAG_CODE) == HttpFeedbackConstants.CODE_OK) {
					if (isRegBack) {
						// 获取信息部分进行了3DES加密，需要解密返回
						retResult = DESede.decryptInHex(jsonKey.getString(HttpFeedbackConstants.TAG_DATA),
								GlobalConfig.DESE_KEY);
					} else {
						GlobalConfig.DESE_KEY = jsonKey.getString(HttpFeedbackConstants.TAG_DATA);
					}
				} else {
					retResult = jsonStr;
				}
			} else {
				// 返回网络异常字符串 {"code":0x1, "error_code":0x201, "msg":"网络异常"}
				return TextUtil.initJSONObj(HttpFeedbackConstants.TAG_CODE, HttpFeedbackConstants.CODE_FAILED,
						HttpFeedbackConstants.TAG_ERROR_CODE, HttpFeedbackConstants.FAILED_SERVER_ERR,
						HttpFeedbackConstants.TAG_MSG, HttpFeedbackConstants.FAILED_STR_NETWORK_ERR).toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				// 返回JSON解析异常字符串 {"code":0x1, "error_code":0x301, "msg":"解析JSON字符串失败"}
				return TextUtil.initJSONObj(HttpFeedbackConstants.TAG_CODE, HttpFeedbackConstants.CODE_FAILED,
						HttpFeedbackConstants.TAG_ERROR_CODE, HttpFeedbackConstants.FAILED_PARSE_JSON,
						HttpFeedbackConstants.TAG_MSG, HttpFeedbackConstants.FAILED_STR_PARSE_JSON).toString();
			} catch (JSONException excepted) {
			}
		}
		// 登录获取数据部分返回NULL表示执行异常
		return retResult;
	}
}
