package com.ijob.android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ijob.android.ui.activity.MainActivity;
import com.ijob.android.ui.fragment.HomeFragment;
import com.ijob.android.ui.fragment.JudgeFragment;
import com.ijob.android.ui.fragment.SettingFragment;

/**
 * Created by JackieZhuang on 2015/1/28.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return HomeFragment.newInstance("home");
		} else if (position == 1) {
			return JudgeFragment.newInstance("judge");
		}
		return SettingFragment.newInstance(position);
	}

	@Override
	public int getCount() {
		return MainActivity.FRAGMENT_NUM;
	}
}
