package com.ijob.android.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijob.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JackieZhuang on 2015/1/27.
 */
public class CommonListAdapter extends CommonBaseAdapter<String> {

	private AnimateFirstDisplayListener mFirstDisplayListener;

	public CommonListAdapter(Context context, List<String> datas) {
		super(context, datas);
		mFirstDisplayListener = new AnimateFirstDisplayListener();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mInflater, convertView, parent, R.layout.listitem_img_text);
		((TextView)holder.getView(R.id.tvDescription)).setText(mDatas.get(position));
		ImageLoader.getInstance().displayImage("", (ImageView) holder.getView(R.id.ivProfile), mFirstDisplayListener);
		return holder.getConvertView();
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
