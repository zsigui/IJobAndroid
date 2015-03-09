package com.ijob.android.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.dk.animation.SwitchAnimationUtil;
import com.ijob.android.R;
import com.ijob.android.ui.activity.RegActivity;

/**
 * Created by JackieZhuang on 2015/1/14.
 */
public class RegThreeFragment extends Fragment {
	private RegActivity mParentActivity;
	private View mView;

	public RegThreeFragment() {
		super();
	}

	public void setParentActivity(RegActivity parentActivity) {
		mParentActivity = parentActivity;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		assert (mParentActivity != null);
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_reg_three, container, false);
			mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					new SwitchAnimationUtil().startAnimation(mView, SwitchAnimationUtil.AnimationType.SCALE);
				}
			});
			Button btnNext = (Button) mView.findViewById(R.id.btnRegFinish);
			btnNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mParentActivity.confirmRegister();
				}
			});
		}
		return mView;
	}
}
