package com.ijob.hx.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ijob.hx.constants.HXConstants;

/**
 * Created by zsigui on 15-3-10.
 */
public class BaseFragment extends Fragment{

	protected View convertView = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.getBoolean(HXConstants.IS_APP_CONFLICT, false))
			return;
	}
}
