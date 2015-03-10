package com.ijob.hx.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EasyUtils;
import com.ijob.android.R;
import com.ijob.android.ui.activity.MainActivity;
import com.ijob.hx.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by JackieZhuang on 2015/3/9.
 */
public class BaseActivity extends FragmentActivity {
	private static final int notifiId = 11;
	protected NotificationManager notificationManager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		EMChatManager.getInstance().activityResumed();
		// umeng
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// umeng
		MobclickAgent.onPause(this);
	}

	/**
	 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
	 * 如果不需要，注释掉即可
	 * @param message
	 */
	protected void notifyNewMessage(EMMessage message) {
		//如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
		//以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
		if(!EasyUtils.isAppRunningForeground(this)){
			return;
		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(getApplicationInfo().icon)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true);

		String ticker = CommonUtils.getMessageDigest(message, this);
		String st = getResources().getString(R.string.expression);
		if(message.getType() == EMMessage.Type.TXT)
			ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
		//设置状态栏提示
		mBuilder.setTicker(message.getFrom()+": " + ticker);

		//必须设置pendingintent，否则在2.3的机器上会有bug
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
		mBuilder.setContentIntent(pendingIntent);

		Notification notification = mBuilder.build();
		notificationManager.notify(notifiId, notification);
		notificationManager.cancel(notifiId);
	}

	/**
	 * 返回
	 *
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}
