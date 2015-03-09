package com.ijob.android.ui.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ijob.android.R;
import com.ijob.android.model.User;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.T;

/**
 * Created by JackieZhuang on 2015/1/13.
 */
public abstract class Abs_UAAsyncTask extends AsyncTask<User, Integer, String> {

	private static final String TAG = Abs_UAAsyncTask.class.toString();

	private PopupWindow mPwProgress;
	protected Context mContext;
	private View mWhichViewToShowPW;

	private FinishCallback mFinishCallback;

	public PopupWindow getPwProgress() {
		return mPwProgress;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public View getWhichViewToShowPW() {
		return mWhichViewToShowPW;
	}

	public void setWhichViewToShowPW(View whichViewToShowPW) {
		mWhichViewToShowPW = whichViewToShowPW;
	}

	public FinishCallback getFinishCallback() {
		return mFinishCallback;
	}

	/**
	* 设置异步任务执行完毕后的回调事件
	*/
	public void setFinishCallback(FinishCallback finishCallback) {
		mFinishCallback = finishCallback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		openPWindow();
	}

	@Override
	protected void onPostExecute(String jsonResult) {
		super.onPostExecute(jsonResult);
		if (mFinishCallback != null) {
			mFinishCallback.dealResult(jsonResult);
		}
		closePWindow();
	}

	@Override
	protected String doInBackground(User... params) {
		return doInBgToImpl(params);
	}

	protected abstract String doInBgToImpl(User[] users);

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		LUtil.d(TAG, "execute " + values + "%");
	}

	@Override
	protected void onCancelled(String s) {
		super.onCancelled(s);
		closePWindow();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		closePWindow();
	}

	private void openPWindow() {
		if (mPwProgress == null) {
			View contentView = LayoutInflater.from(mContext).inflate(R.layout.pwindow_progress, null);
			contentView.setFocusable(true); // 用于响应按键事件
			contentView.setFocusableInTouchMode(true);
			mPwProgress = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			mPwProgress.setOutsideTouchable(false);
			mPwProgress.setFocusable(true);
			mPwProgress.getContentView().setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						// 后退取消任务
						cancel(true);
						closePWindow();
						T.showS(mContext, "登录取消");
						return true;
					}
					return false;
				}
			});
		}
		if (!mPwProgress.isShowing()) {
			if (mWhichViewToShowPW == null) {
				mPwProgress.showAtLocation(((Activity) mContext).findViewById(R.id.lyMainView), Gravity.CENTER, 0, 0);
			} else {
				mPwProgress.showAtLocation(mWhichViewToShowPW, Gravity.CENTER, 0, 0);
			}
		}
	}

	private void closePWindow() {
		if (mPwProgress != null && mPwProgress.isShowing()) {
			mPwProgress.dismiss();
		}
	}

	/**
	 * 异步线程执行完毕后的处理
	 */
	public interface FinishCallback {
		public void dealResult(String jsonResultStr);
	}
}
