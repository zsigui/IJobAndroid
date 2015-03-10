package com.ijob.hx.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ijob.android.R;

import java.util.List;

/**
 * Created by zsigui on 15-3-10.
 */
public class ExpressionAdapter extends ArrayAdapter<String> {

	public ExpressionAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.hx_listitem_row_expression, null);
		}

		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);

		String filename = getItem(position);
		int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
		imageView.setImageResource(resId);

		return convertView;
	}

}
