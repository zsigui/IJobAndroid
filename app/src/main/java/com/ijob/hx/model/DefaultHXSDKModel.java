package com.ijob.hx.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ijob.hx.utils.HXPreferenceUtils;

/**
 * Created by JackieZhuang on 2015/3/3.
 */
public class DefaultHXSDKModel extends HXSDKModel{
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PWD = "pwd";
	protected Context context = null;

	public DefaultHXSDKModel(Context ctx){
		context = ctx;
		HXPreferenceUtils.init(context);
	}

	@Override
	public void setSettingMsgNotification(boolean paramBoolean) {
		// TODO Auto-generated method stub
		HXPreferenceUtils.getInstance().setSettingMsgNotification(paramBoolean);
	}

	@Override
	public boolean getSettingMsgNotification() {
		// TODO Auto-generated method stub
		return HXPreferenceUtils.getInstance().getSettingMsgNotification();
	}

	@Override
	public void setSettingMsgSound(boolean paramBoolean) {
		// TODO Auto-generated method stub
		HXPreferenceUtils.getInstance().setSettingMsgSound(paramBoolean);
	}

	@Override
	public boolean getSettingMsgSound() {
		// TODO Auto-generated method stub
		return HXPreferenceUtils.getInstance().getSettingMsgSound();
	}

	@Override
	public void setSettingMsgVibrate(boolean paramBoolean) {
		// TODO Auto-generated method stub
		HXPreferenceUtils.getInstance().setSettingMsgVibrate(paramBoolean);
	}

	@Override
	public boolean getSettingMsgVibrate() {
		// TODO Auto-generated method stub
		return HXPreferenceUtils.getInstance().getSettingMsgVibrate();
	}

	@Override
	public void setSettingMsgSpeaker(boolean paramBoolean) {
		// TODO Auto-generated method stub
		HXPreferenceUtils.getInstance().setSettingMsgSpeaker(paramBoolean);
	}

	@Override
	public boolean getSettingMsgSpeaker() {
		// TODO Auto-generated method stub
		return HXPreferenceUtils.getInstance().getSettingMsgSpeaker();
	}

	@Override
	public boolean getUseHXRoster() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveHXId(String hxId) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.edit().putString(PREF_USERNAME, hxId).commit();
	}

	@Override
	public String getHXId() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_USERNAME, null);
	}

	@Override
	public boolean savePassword(String pwd) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.edit().putString(PREF_PWD, pwd).commit();
	}

	@Override
	public String getPwd() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_PWD, null);
	}
}
