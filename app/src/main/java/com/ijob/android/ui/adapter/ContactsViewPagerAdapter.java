package com.ijob.android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zsigui on 15-3-11.
 */
public class ContactsViewPagerAdapter extends FragmentPagerAdapter {


	private Fragment[] mFragments;

	public ContactsViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {
		super(fm);
		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments[position];
	}

	@Override
	public int getCount() {
		return mFragments.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view != null && view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
}
