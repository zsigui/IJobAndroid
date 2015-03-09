package com.ijob.android.ui.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JackieZhuang on 2015/1/28.
 */
public class ViewHolder {
	private final SparseArray<View> mViews = new SparseArray<>();
	private View mConvertView;

	private ViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
		mConvertView = inflater.inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(LayoutInflater inflater, View convertView, ViewGroup parent, int layoutId) {
		if (convertView == null) {
			return new ViewHolder(inflater, parent, layoutId);
		}
		return (ViewHolder) convertView.getTag();
	}

	public <T extends View>T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}

	public View getConvertView() {
		return mConvertView;
	}
}
