package com.ijob.android.ui.application;

import android.app.Application;
import android.content.Context;

import com.ijob.android.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by JackieZhuang on 2015/1/17.
 */
public class AppInfo extends Application {
	public static Context sAppContext;
	public static AppInfo sAppInstance;

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


	}
}
