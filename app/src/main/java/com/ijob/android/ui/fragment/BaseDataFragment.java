package com.ijob.android.ui.fragment;


import android.support.v4.app.Fragment;

/**
 * Created by JackieZhuang on 2015/1/27.
 */
public abstract class BaseDataFragment extends Fragment {

	protected boolean mIsVisible = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			mIsVisible = true;
			onVisible();
		} else {
			mIsVisible = false;
			onInvisible();
		}
	}

	private void onInvisible() {
	}

	protected void onVisible() {
		onInitLazyDataLoad();
	}

	/**
	 * <br />抽象方法，该方法用于视图第一次加载数据
	 * <br />必须实现以提供数据懒加载方式
	 */
	protected abstract void onInitLazyDataLoad();
}
