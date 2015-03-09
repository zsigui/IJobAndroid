package com.ijob.android.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijob.android.R;
import com.ijob.android.ui.adapter.PagerAdapter;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.T;
import com.nineoldandroids.view.ViewHelper;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;


/**
 * Created by JackieZhuang on 2015/1/27.
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener,
		View.OnTouchListener {

	public static final int FRAGMENT_NUM = 3;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private ImageView mivSlideStrip;
	private TextView mtvHome;
	private TextView mtvComment;
	private TextView mtvSetting;
	private MenuDrawer mLeftDrawer;

	private int mCurrentIndex = 0;
	private int mScreenWidth = 0;

	// MenuDrawer适用
	private int mPagerOffsetPixels;
	private int mPagerPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setListener();
		adjustSlideStripPos();
	}

	private void initView() {
		mLeftDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT, MenuDrawer.MENU_DRAG_CONTENT);
		mLeftDrawer.setContentView(R.layout.activity_pager_main);
		mLeftDrawer.setMenuView(R.layout.slidemenu_left);
		mLeftDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mLeftDrawer.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
			@Override
			public boolean isViewDraggable(View view, int dx, int x, int y) {
				if (view == mPager) {
					return !(mPagerPosition == 0 && mPagerOffsetPixels == 0) || dx < 0;
				}
				return false;
			}
		});
		mPager = (ViewPager) findViewById(R.id.vpMain);
		mivSlideStrip = (ImageView) findViewById(R.id.ivSlipStrip);
		mtvHome = (TextView) findViewById(R.id.tvHome);
		mtvComment = (TextView) findViewById(R.id.tvComment);
		mtvSetting = (TextView) findViewById(R.id.tvSetting);
	}

	private void setListener() {
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(this);
		mPager.setOnTouchListener(this);
		mPager.setCurrentItem(mCurrentIndex);
		mPager.setPageTransformer(true, new MyPagerTransformer());
		mtvHome.setOnClickListener(this);
		mtvComment.setOnClickListener(this);
		mtvSetting.setOnClickListener(this);
	}

	private void adjustSlideStripPos() {
		ivWidth = getResources().getDimension(R.dimen.view_tap_pic_width);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		// 计算滑动卡的偏移
		mTabOffset = (mScreenWidth / 3 - ivWidth) / 2;
		ViewHelper.setTranslationX(mivSlideStrip, mTabOffset);
		mTabSlideGap = 2 * mTabOffset + ivWidth;
		mFirstTabPos = mTabOffset;
		mSecondTabPos = mFirstTabPos + mTabSlideGap;
		mThirdTabPos = mSecondTabPos + mTabSlideGap;
	}

	/* implment start */

	/* 页面卡标滑动相关属性 */
	private float mTabOffset;
	private float ivWidth;
	private float mTabSlideGap;
	private float mFirstTabPos;
	private float mSecondTabPos;
	private float mThirdTabPos;

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		mPagerPosition = position;
		mPagerOffsetPixels = positionOffsetPixels;
		Log.d("ttt", "onPageScrolled : " + position);
	}

	@Override
	public void onPageSelected(int position) {
		Log.d("ttt", "onPageSelected");
		execAnim(mivSlideStrip.getX(), mTabOffset + mTabSlideGap * position);
		mCurrentIndex = position;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		Log.d("ttt", "onPageScrollStateChanged : " + state);
	}

	private void execAnim(float... vals) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(mivSlideStrip, "x", vals);
		animator.setDuration(300);
		animator.start();
	}

	@Override
	public void onClick(View v) {
		if (v == mtvHome) {
			mPager.setCurrentItem(0);
			T.showS(AppInfo.sAppContext, "选项卡一被选中");
		} else if (v == mtvComment) {
			mPager.setCurrentItem(1);
			T.showS(AppInfo.sAppContext, "选项卡二被选中");
		} else {
			mPager.setCurrentItem(2);
			T.showS(AppInfo.sAppContext, "选项卡三被选中");
		}
	}

	/* Touch具体拦截实现 START */
	private static final float BOUND_TAN_VAL = 0.6f;
	private static final int VELOCITY_COMPUTE_VAL = 1000;
	private float mDownX;
	private float mStartX;
	private VelocityTracker mTracker;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mTracker == null) {
			mTracker = VelocityTracker.obtain();
		}
		mTracker.addMovement(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				LUtil.i("Action.Down");
				mDownX = event.getX();
				mStartX = mDownX;
				break;
			case MotionEvent.ACTION_MOVE:
				float deltaX = (event.getX() - mStartX) / mScreenWidth * mTabSlideGap;
				Log.d("actual", "start = " + mStartX + ", end = " + event.getX() + ", down = " + mDownX);
				mStartX = event.getX();
				float afterMovedPos = mivSlideStrip.getX() - deltaX;
				Log.d("actual", "mivSlid = " + mivSlideStrip.getX() + ", actual =" + (event.getX() - mDownX) + ", afterMove = " + afterMovedPos);
				if (mCurrentIndex == 0) {
					if (afterMovedPos > mFirstTabPos && afterMovedPos < mSecondTabPos) {
						ViewHelper.setTranslationX(mivSlideStrip, afterMovedPos);
					}
				} else if (mCurrentIndex == 1) {
					ViewHelper.setTranslationX(mivSlideStrip, afterMovedPos);
				} else {
					if (afterMovedPos > mSecondTabPos && afterMovedPos < mThirdTabPos) {
						ViewHelper.setTranslationX(mivSlideStrip, afterMovedPos);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				LUtil.i("Action.Up");
				// go through
			case MotionEvent.ACTION_CANCEL:
				LUtil.i("Action.Cancel");
				float actualMoveDelta = event.getX() - mDownX;
				mTracker.computeCurrentVelocity(1000, 1500);
				Log.d("ttt", "actualMoveDelta = " + actualMoveDelta);
				if (Math.abs(actualMoveDelta) > mScreenWidth / 3 || (actualMoveDelta >= 50 &&
						Math.abs(mTracker.getXVelocity()) >= VELOCITY_COMPUTE_VAL)) {
					Log.d("ttt", "mCurrentIndex = " + mCurrentIndex + ", actualMoveDelta = " + actualMoveDelta + ", " +
							"getXVelocity = " + mTracker.getXVelocity());
					// 超过换屏限制，切换
					if ((actualMoveDelta < 0 && mCurrentIndex == 0) || (actualMoveDelta > 0 && mCurrentIndex == 2)) {
						mPager.setCurrentItem(1);
					} else if (mCurrentIndex == 1) {
						mPager.setCurrentItem(actualMoveDelta > 0 ? 0 : 2);
					} else {
						execAnim(mivSlideStrip.getX(), mTabOffset + mTabSlideGap * mCurrentIndex);
					}
				} else {
					execAnim(mivSlideStrip.getX(), mTabOffset + mTabSlideGap * mCurrentIndex);
				}
				if (mTracker != null) {
					mTracker.recycle();
					mTracker.clear();
					mTracker = null;
				}
				break;
			default:
		}
		return super.onTouchEvent(event);
	}
	/* Touch具体拦截实现 END */

	/* implment end */

	/**
	 * 自定义ViewPager页面切换
	 */
	private class MyPagerTransformer implements ViewPager.PageTransformer {

		@Override
		public void transformPage(View view, float v) {
			if(v <= -1) {
				ViewHelper.setRotation(view, 0);
				ViewHelper.setAlpha(view, 0);
			} else if (v >= 1){
				ViewHelper.setRotation(view, 0);
				ViewHelper.setAlpha(view, 0);
			} else {
				if (v < 0) {
					ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.75f);
					ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
				} else {
					ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.25f);
					ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
				}
				ViewHelper.setRotation(view, v * 180);
				ViewHelper.setScaleX(view, 1 - Math.abs(v));
				ViewHelper.setScaleY(view, 1 - Math.abs(v));
				ViewHelper.setAlpha(view, 1 - Math.abs(v));
			}
		}
	}

	/* To Override Listener Event Start */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			if (mLeftDrawer != null) {
				mLeftDrawer.toggleMenu();
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLeftDrawer != null && (mLeftDrawer.getDrawerState() == MenuDrawer.STATE_OPEN ||
					mLeftDrawer.getDrawerState() == MenuDrawer.STATE_OPENING)) {
				mLeftDrawer.closeMenu();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home && mLeftDrawer != null) {
			mLeftDrawer.toggleMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* To Override Listener Event End */
}
