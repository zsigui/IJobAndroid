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
public class RegTwoFragment extends Fragment {

	private RegActivity mParentActivity;
	private View mView;

	public RegTwoFragment() {
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
			mView = inflater.inflate(R.layout.fragment_reg_two, container, false);
			mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					new SwitchAnimationUtil().startAnimation(mView, SwitchAnimationUtil.AnimationType.HORIZON_CROSS);
				}
			});
			Button btnNext = (Button) mView.findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mParentActivity.changeFragmentView(RegActivity.FRAG_TAG_TWO, RegActivity.FRAG_TAG_THREE);
				}
			});
		}
		return mView;
	}
}
