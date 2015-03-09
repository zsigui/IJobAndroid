package com.ijob.android.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijob.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JackieZhuang on 2015/2/2.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private LayoutInflater mInflater;
	private ArrayList<String> mDatas;

	private AnimateFirstDisplayListener mFirstDisplayListener;

	public RecyclerListAdapter(Context context, ArrayList<String> datas) {
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		mFirstDisplayListener = new AnimateFirstDisplayListener();
	}

	private static class ListHolder extends RecyclerView.ViewHolder{

		private TextView tvMsg;
		private ImageView ivProfile;

		public ListHolder(View itemView) {
			super(itemView);
			tvMsg = (TextView) itemView.findViewById(R.id.tvDescription);
			ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = mInflater.inflate(R.layout.listitem_img_text, parent, false);
		ListHolder holder = new ListHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		ListHolder listHolder = (ListHolder) holder;
		ImageLoader.getInstance().displayImage("", listHolder.ivProfile, mFirstDisplayListener);
		listHolder.tvMsg.setText(mDatas.get(position));
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	public static void clearImages() {
		AnimateFirstDisplayListener.displayedImages.clear();
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
