package com.ijob.android.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.ijob.android.R;
import com.ijob.android.ui.adapter.ContactsViewPagerAdapter;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.ui.fragment.ContactsListFragment;
import com.ijob.hx.ui.fragment.ContactsMessageFragment;

/**
 * Created by zsigui on 15-3-10.
 */
public class ContactsFragment extends BaseDataFragment {


	public static final int REFRESH_TYPE_ADDRESS_LIST = 0;
	public static final int REFRESH_TYPE_UNREAD_MSG = 1;
	public static final int REFRESH_TYPE_BOTH = 2;

	private ViewPager mViewPager;
	private RelativeLayout mTabMsg;
	private RelativeLayout mTabAddressList;
	private ImageView ivSlidTab;
	private TextView mUnreadMsg;
	private TextView mUnreadAddressMsg;
	private ContactsViewPagerAdapter mAdapter;
	private Fragment[] mFragments;
	private int mTabOffset = 0;
	private static int mCurrentIndex = 0;

	public static ContactsFragment newInstance() {
		return new ContactsFragment();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mConvertView == null) {
			mConvertView = inflater.inflate(R.layout.hx_fragment_contacts_main, container, false);
		}
		ViewGroup group = (ViewGroup) mConvertView.getParent();
		if (group != null) {
			group.removeView(mConvertView);
		}
		return mConvertView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adjustSlideStripPos();

		mViewPager = (ViewPager) mConvertView.findViewById(R.id.vpContent);
		mUnreadAddressMsg = (TextView) mConvertView.findViewById(R.id.unread_list_number);
		mUnreadMsg = (TextView) mConvertView.findViewById(R.id.unread_msg_number);
		mFragments = new Fragment[]{new ContactsMessageFragment(), new ContactsListFragment()};
		mAdapter = new ContactsViewPagerAdapter(getFragmentManager(), mFragments);
		mTabMsg = (RelativeLayout) mConvertView.findViewById(R.id.tabMsg);
		mTabAddressList = (RelativeLayout) mConvertView.findViewById(R.id.tabAddressList);
		ivSlidTab = (ImageView) mConvertView.findViewById(R.id.ivSlipStrip);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(mCurrentIndex);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					mTabMsg.setSelected(true);
					mTabAddressList.setSelected(false);
					ObjectAnimator animator = ObjectAnimator.ofFloat(ivSlidTab, "x", mTabOffset, 0);
					animator.setDuration(300);
					animator.start();
				} else {
					mTabMsg.setSelected(false);
					mTabAddressList.setSelected(true);
					ObjectAnimator animator = ObjectAnimator.ofFloat(ivSlidTab, "x", 0, mTabOffset);
					animator.setDuration(300);
					animator.start();
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void adjustSlideStripPos() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 计算滑动卡的偏移
		mTabOffset = dm.widthPixels / 2;
	}


	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadMsg() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			mUnreadMsg.setText(String.valueOf(count));
			mUnreadMsg.setVisibility(View.VISIBLE);
		} else {
			mUnreadMsg.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressMsg() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					mUnreadAddressMsg.setText(String.valueOf(count));
					mUnreadAddressMsg.setVisibility(View.VISIBLE);
				} else {
					mUnreadAddressMsg.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 *
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (AppInfo.sAppInstance.getContactList().get(HXConstants.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = AppInfo.sAppInstance.getContactList().get(HXConstants.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	@Override
	protected void onInitLazyDataLoad() {
	}

	public void refresh(int type) {

	}
}
