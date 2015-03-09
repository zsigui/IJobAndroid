package com.ijob.android.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dk.animation.SwitchAnimationUtil;
import com.ijob.android.R;
import com.ijob.android.constants.HttpFeedbackConstants;
import com.ijob.android.model.User;
import com.ijob.android.ui.async.Abs_UAAsyncTask;
import com.ijob.android.ui.async.RegAsyncTask;
import com.ijob.android.ui.fragment.RegOneFragment;
import com.ijob.android.ui.fragment.RegThreeFragment;
import com.ijob.android.ui.fragment.RegTwoFragment;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.T;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JackieZhuang on 2015/1/13.
 */
public class RegActivity extends ActionBarActivity {

	public static final String FRAG_TAG_ONE = "reg_one";
	public static final String FRAG_TAG_TWO = "reg_two";
	public static final String FRAG_TAG_THREE = "reg_three";

	private Button btnBack;
	private TextView tvTitle;

	private FragmentManager mFragmentManager;
	private RegOneFragment mOneFragment;
	private RegTwoFragment mTwoFragment;
	private RegThreeFragment mThreeFragment;

	private User mStoreUser = new User();

	public User getUser() {
		return mStoreUser;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initUI();
	}

	private void initUI() {
		btnBack = (Button) findViewById(R.id.btnBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		initFragment();
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvTitle.setText("注册界面");
	}

	private void initFragment() {
		if (mOneFragment == null) {
			mOneFragment = new RegOneFragment();
			mOneFragment.setParentActivity(this);
		}
		if (mTwoFragment == null) {
			mTwoFragment = new RegTwoFragment();
			mTwoFragment.setParentActivity(this);
		}
		if (mThreeFragment == null) {
			mThreeFragment = new RegThreeFragment();
			mThreeFragment.setParentActivity(this);
		}
		mFragmentManager = this.getFragmentManager();
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.add(R.id.flLayContent, mOneFragment, RegActivity.FRAG_TAG_ONE);
		ft.add(R.id.flLayContent, mTwoFragment, RegActivity.FRAG_TAG_TWO);
		ft.add(R.id.flLayContent, mThreeFragment, RegActivity.FRAG_TAG_THREE);
		ft.show(mOneFragment);
		ft.hide(mTwoFragment);
		ft.hide(mThreeFragment);
		ft.commit();
	}

	public void changeFragmentView(String oldTag, String newTag) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.hide(mFragmentManager.findFragmentByTag(oldTag));
		ft.show(mFragmentManager.findFragmentByTag(newTag));
		ft.commit();
	}

	@Override
	public void onBackPressed() {
		if (mOneFragment.isVisible()) {
			super.onBackPressed();
		} else if (mTwoFragment.isVisible()) {
			changeFragmentView(FRAG_TAG_TWO, FRAG_TAG_ONE);
		} else if (mThreeFragment.isVisible()) {
			changeFragmentView(FRAG_TAG_THREE, FRAG_TAG_TWO);
		}
	}

	/**
	 * <br /> 确定注册，由Fragment调用
	 * <br /> 本步骤不对数据审核，调用前需要确保数据已经经过审核
	 */
	public void confirmRegister() {
		assert (mStoreUser != null && !TextUtils.isEmpty(mStoreUser.getName()) && !TextUtils.isEmpty(mStoreUser
				.getPassword()));
		newTaskInstance().execute(mStoreUser);
	}

	private Abs_UAAsyncTask newTaskInstance() {
		Abs_UAAsyncTask logTask = new RegAsyncTask(RegActivity.this);
		logTask.setFinishCallback(new Abs_UAAsyncTask.FinishCallback() {
			@Override
			public void dealResult(String jsonResultStr) {
				LUtil.d("tss", "dealResult=" + jsonResultStr);
				if (jsonResultStr == null) {
					T.showL(RegActivity.this, "未知异常，登录失败");
					return;
				}
				try {
					JSONObject jsonResult = new JSONObject(jsonResultStr);
					if (jsonResult.getInt(HttpFeedbackConstants.TAG_CODE) == HttpFeedbackConstants.CODE_OK) {
						T.showL(RegActivity.this, "注册成功，id=" + jsonResult.getInt(HttpFeedbackConstants.TAG_DATA));
						LUtil.d("LoginResult", String.valueOf(jsonResult.getInt(HttpFeedbackConstants.TAG_DATA)));
						finish();
					} else {
						T.showL(RegActivity.this, "登录失败，code = " + jsonResult.getInt(HttpFeedbackConstants
								.TAG_ERROR_CODE)
								+ ", msg = " + jsonResult.getString(HttpFeedbackConstants.TAG_MSG));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		logTask.setWhichViewToShowPW(this.findViewById(R.id.lyMainView));
		return logTask;
	}
}
