package com.ijob.android.util;

import android.text.TextUtils;

import com.ijob.android.constants.GlobalConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JackieZhuang on 2015/1/10.
 */
public class HttpUtil {
	/**
	 * 请求连接超时时间
	 */
	public static final int CONN_TIMEOUT = 6000;
	/**
	 * 等待数据超时时间
	 */
	public static final int SO_TIMEOUT = 10000;

	/**
	 * 根据输入的URL和参数执行httpGet请求，并返回获取到的相应字符串
	 *
	 * @param requestUrl
	 * @param params
	 * @return
	 */
	public static String doGet(String requestUrl, String params) {
		String realUrl = fixUrl(requestUrl);
		if (!TextUtils.isEmpty(params)) {
			int idx = realUrl.lastIndexOf('?');
			if (idx == realUrl.length() - 1) {
				// 已有问号
				realUrl += params;
			} else if (idx == -1) {
				// 不存在问号
				realUrl += '?' + params;
			} else {
				// 已带参数
				realUrl += '&' + params;
			}
		}
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), CONN_TIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), SO_TIMEOUT);
		HttpGet httpGet = new HttpGet(realUrl);
		addHeader(httpGet);
		try {
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doGet(String requestUrl, List<NameValuePair> pairs) {
		StringBuilder params = new StringBuilder("");
		for (NameValuePair pair : pairs) {
			params.append(pair.getName()).append("=").append(pair.getValue()).append("&");
		}
		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
		}
		return doGet(requestUrl, params.toString());
	}

	public static String doGet(String requestUrl, Map<String, String> pairs) {
		StringBuilder params = new StringBuilder("");
		for (Map.Entry<String, String> pair : pairs.entrySet()) {
			params.append(pair.getKey()).append("=").append(pair.getValue()).append("&");
		}
		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
		}
		return doGet(requestUrl, params.toString());
	}


	/**
	 * 根据请求URL和Post参数进行HttpPost请求并获取返回的响应字符串
	 *
	 * @param requestUrl
	 * @param pairs
	 * @return
	 */
	public static String doPost(String requestUrl, List<NameValuePair> pairs) {
		try {
			String realUrl = fixUrl(requestUrl);
			HttpEntity requestEntity = new UrlEncodedFormEntity(pairs, GlobalConfig.DEFAULT_CHARSET);
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(httpParams, CONN_TIMEOUT);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(realUrl);
			httpPost.setEntity(requestEntity);
			addHeader(httpPost);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, GlobalConfig.DEFAULT_CHARSET);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String fixUrl(String requestUrl) {
		String realUrl = requestUrl;
		if (!realUrl.startsWith("http://")) {
			realUrl = "http://" + realUrl;
		}
		return realUrl;
	}

	/**
	 * 根据请求URL和Post参数进行HttpPost请求并获取返回的响应字符串
	 *
	 * @param requestUrl
	 * @param pairs
	 * @return
	 */
	public static String doPost(String requestUrl, Map<String, String> pairs) {
		List<NameValuePair> valuePairs = new ArrayList<>();
		for (Map.Entry<String, String> pair : pairs.entrySet()) {
			valuePairs.add(new BasicNameValuePair(pair.getKey(), pair.getValue()));
		}
		return doPost(requestUrl, valuePairs);
	}

	/**
	 * 根据请求URL和Post参数进行HttpPost请求并获取返回的响应字符串
	 *
	 * @param requestUrl
	 * @param feedback
	 * @param time
	 * @return
	 */
	public static String doPostUpload(String requestUrl, File feedback, String time) {
		try {
			MultipartEntity requestEntity = new MultipartEntity();
			FileBody filePart = new FileBody(feedback);
			StringBody timePart = new StringBody(time);
			requestEntity.addPart("feedback", filePart);
			requestEntity.addPart("time", timePart);
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(httpParams, CONN_TIMEOUT);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(requestUrl);
			addHeader(httpPost);
			httpPost.setEntity(requestEntity);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void addHeader(HttpUriRequest httpRequst) {
		httpRequst.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	}
}
