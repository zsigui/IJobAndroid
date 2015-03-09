package com.ijob.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by JackieZhuang on 2015/1/28.
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected List<T> mDatas;

	public CommonBaseAdapter(Context context, List<T> datas) {
		this.mDatas = datas;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
