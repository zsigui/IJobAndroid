package com.ijob.android.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.ijob.android.R;
import com.ijob.android.util.LUtil;

/**
 * Created by JackieZhuang on 2015/2/3.
 */
public class SplashActivity extends Activity{

	private static String TAG_LOG = SplashActivity.class.toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		ImageView iv = new ImageView(this);
		final Handler handler = new Handler();
		handler.getLooper();
		iv.setLayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		if (Build.VERSION.SDK_INT >= 16) {
			iv.setBackground(getResources().getDrawable(R.drawable.bg1));
		} else {
			iv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg1));
		}
		iv.setVisibility(View.VISIBLE);
		setContentView(iv);
		ObjectAnimator animatorX = ObjectAnimator.ofFloat(iv, "scaleX", 1.0f, 1.5f);
		ObjectAnimator animatorY = ObjectAnimator.ofFloat(iv, "scaleY", 1.0f, 1.5f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(2500);
		animatorSet.setTarget(iv);
		animatorSet.setInterpolator(new DecelerateInterpolator());
		animatorSet.playTogether(animatorX, animatorY);
		animatorSet.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				LUtil.i(TAG_LOG, "onAnimationStart");
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				LUtil.i(TAG_LOG, "onAnimationEnd");
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				}, 1500);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				LUtil.i(TAG_LOG, "onAnimationCancel");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				LUtil.i(TAG_LOG, "onAnimationRepeat");
			}
		});
		animatorSet.setStartDelay(0);
		animatorSet.start();
	}
}
