package com.ijob.android.ui.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.ijob.android.ui.application.AppInfo;
import com.ijob.android.util.T;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JackieZhuang on 2015/1/17.
 */
public class FilterTextWatcher implements TextWatcher {

	private static final int INTERVAL = 1000;

	private EditText mSelf;
	private Pattern mFilterPattern;
	private int mMaxLength;
	private FilterCallback mCallback;
	private boolean mNeedToast;

	private long mLastTime;

	public FilterTextWatcher(EditText self, String strFilterPattern, int maxLength) {
		this(self, strFilterPattern, maxLength, true);
	}

	public FilterTextWatcher(EditText self, String strFilterPattern, int maxLength, boolean needToast) {
		mSelf = self;
		mFilterPattern = Pattern.compile(strFilterPattern);
		mMaxLength = maxLength;
		mNeedToast = needToast;
	}

	public FilterCallback getCallback() {
		return mCallback;
	}

	public void setCallback(FilterCallback callback) {
		mCallback = callback;
	}

	public boolean isNeedToast() {
		return mNeedToast;
	}

	public void setNeedToast(boolean needToast) {
		mNeedToast = needToast;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Matcher matcher = mFilterPattern.matcher(s);
		if (s.length() == 0 || s.length() == mMaxLength) {
			return;
		} else if (s.length() > mMaxLength) {
			mSelf.setText(s.subSequence(0, mMaxLength));
			showToast("输入字符长度已达限制");
		} else {
			Log.d("test", "onTextChanged match = " + matcher.matches());
			if (!matcher.matches()) {
				mSelf.setText(s.subSequence(0, start));
				showToast("输入字符不合法");
			}
		}
		mSelf.setSelection(mSelf.length());
	}

	private void showToast(String msg) {
		if (mNeedToast && System.currentTimeMillis() - mLastTime > FilterTextWatcher.INTERVAL && AppInfo.sAppContext !=
				null) {
			T.showS(AppInfo.sAppContext, msg);
			mLastTime = System.currentTimeMillis();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (mCallback != null)
			mCallback.doInAfter();
	}

	public interface FilterCallback {
		public void doInAfter();
	}
}
