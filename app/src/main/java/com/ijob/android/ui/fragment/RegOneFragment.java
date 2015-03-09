package com.ijob.android.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;

import com.dk.animation.SwitchAnimationUtil;
import com.ijob.android.R;
import com.ijob.android.ui.activity.RegActivity;
import com.ijob.android.ui.listener.FilterTextWatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JackieZhuang on 2015/1/14.
 */
public class RegOneFragment extends Fragment {

	private RegActivity mParentActivity;
	private View mView;
	private Button btnNext;
	private EditText etName;
	private EditText etPwd;
	private EditText etConfirmPwd;
	private EditText etEmail;
	private EditText etPhone;

	public RegOneFragment() {
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
			mView = inflater.inflate(R.layout.fragment_reg_one, container, false);
			initUI();
			initListener();
		}
		return mView;
	}

	private void initUI() {
		etName = (EditText) mView.findViewById(R.id.etName);
		etPwd = (EditText) mView.findViewById(R.id.etPwd);
		etConfirmPwd = (EditText) mView.findViewById(R.id.etConfirmPwd);
		etEmail = (EditText) mView.findViewById(R.id.etEmail);
		etPhone = (EditText) mView.findViewById(R.id.etPhone);
		btnNext = (Button) mView.findViewById(R.id.btnNext);
	}

	public String filterStr(String input) {
		String regex = "[/\\\\:*?<>|\\\"\\n\t]";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(input);
		return m.replaceAll("");
	}

	private void initListener() {
		mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				new SwitchAnimationUtil().startAnimation(mView, SwitchAnimationUtil.AnimationType.FLIP_VERTICAL);
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mParentActivity.changeFragmentView(RegActivity.FRAG_TAG_ONE, RegActivity.FRAG_TAG_TWO);
			}
		});
	}
}
