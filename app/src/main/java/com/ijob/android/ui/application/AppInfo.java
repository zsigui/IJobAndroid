package com.ijob.android.ui.application;

import android.app.Application;
import android.content.Context;

import com.easemob.EMCallBack;
import com.ijob.android.R;
import com.ijob.hx.db.domain.HXUser;
import com.ijob.hx.sdk.impl.DemoHXSDKHelper;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Map;

/**
 * Created by JackieZhuang on 2015/1/17.
 */
public class AppInfo extends Application {
	public static Context sAppContext;
	public static AppInfo sAppInstance;
	public static DemoHXSDKHelper sHXSDKHelper = new DemoHXSDKHelper();

	public AppInfo() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sAppContext = getApplicationContext();
		sAppInstance = this;

		/* 配置异步加载图片设置 */
		DisplayImageOptions mDisplayOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.resetViewBeforeLoading(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.displayer(new RoundedBitmapDisplayer(20))
				.build();
		ImageLoaderConfiguration mLoaderConfiguration = new ImageLoaderConfiguration.Builder(sAppContext)
				.defaultDisplayImageOptions(mDisplayOptions)
				.diskCacheSize(10 * 1024 * 1024)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.writeDebugLogs()
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.diskCacheFileCount(50)
				.build();
		ImageLoader.getInstance().init(mLoaderConfiguration);

		sHXSDKHelper.onInit(sAppContext);
	}

	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, HXUser> getContactList() {
		return sHXSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, HXUser> contactList) {
		sHXSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
		return sHXSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return sHXSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param username
	 */
	public void setUserName(String username) {
		sHXSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		sHXSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		sHXSDKHelper.logout(emCallBack);
	}
}
