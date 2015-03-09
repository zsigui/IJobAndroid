package com.ijob.android.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.ijob.android.R;
import com.ijob.android.ui.adapter.CommonListAdapter;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.T;
import com.ijob.android.util.TimeUtil;
import com.markmao.pulltorefresh.widget.XListView;

import java.util.ArrayList;

/**
 * Created by JackieZhuang on 2015/1/27.
 */
public class HomeFragment extends BaseDataFragment {

	private static final String LOG_TAG = HomeFragment.class.toString();
	private static final String INIT_MSG = "initMsg";

	private boolean mHasLoadOnce = false;
	private boolean mBeingRefresh = false;
	private boolean mBeingLoadMore = false;
	/**
	 * 使用静态减少内存泄露
	 */
	private static HomeFragment mThis;
	private static Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			assert (mThis != null);
			LUtil.i("msg.what = " + msg.what);
			switch (msg.what) {
				case 0:
					T.showLInCenter(AppInfo.sAppContext, "异步加载成功");
					mThis.mStubLoad.setVisibility(View.GONE);
					mThis.mlvDataView.setVisibility(View.VISIBLE);
					mThis.notifyDataChange();
					break;
				case 1:
					LUtil.i(LOG_TAG, "异步加载失败, 重新加载！");
					mThis.onInitLazyDataLoad();
					break;
				case 2:
					mThis.mlvDataView.stopRefresh();
					mThis.mlvDataView.setRefreshTime(TimeUtil.getTime());
					mThis.notifyDataChange();
					break;
				case 3:
					mThis.mlvDataView.stopLoadMore();
					mThis.mlvDataView.setRefreshTime(TimeUtil.getTime());
					mThis.notifyDataChange();
					break;
				default:
					LUtil.i(LOG_TAG, "异步加载超时异常");
					mThis.mStubLoad.setVisibility(View.GONE);
					T.showLInCenter(AppInfo.sAppContext, "异步加载超时异常");
			}
		}
	};

	// 存储数据
	private ArrayList<String> mData;

	/* View Widget */
	private View mHomeView;
	private View mStubLoad;
	private XListView mlvDataView;
	private CommonListAdapter mListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mThis = this;
		mData = new ArrayList<>();
	}

	public static HomeFragment newInstance(String msg) {
		Bundle bundle = new Bundle();
		bundle.putString(HomeFragment.INIT_MSG, msg);
		HomeFragment instance = new HomeFragment();
		instance.setArguments(bundle);
		return instance;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mHomeView == null) {
			mHomeView = inflater.inflate(R.layout.fragment_home, container, false);
			mStubLoad = ((ViewStub)mHomeView.findViewById(R.id.stubLoad)).inflate();
			mlvDataView = (XListView) mHomeView.findViewById(R.id.lvDataView);
			initListView();
		}
		mlvDataView.setVisibility(View.GONE);
		ViewGroup group = (ViewGroup) mHomeView.getParent();
		if (group != null) {
			group.removeView(mHomeView);
		}
		return mHomeView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CommonListAdapter.clearImages();
	}

	private void initListView() {
		mListAdapter = new CommonListAdapter(getActivity(), mData);
		mlvDataView.setAdapter(mListAdapter);
		// 下拉刷新回调
		mlvDataView.setPullRefreshEnable(true);
		// 加载更多
		mlvDataView.setPullRefreshEnable(true);
		mlvDataView.setAutoLoadEnable(true);
		mlvDataView.setRefreshTime(TimeUtil.getTime());
		mlvDataView.setXListViewListener(new XListView.IXListViewListener() {
			@Override
			public void onRefresh() {
				onDataRefresh();
			}

			@Override
			public void onLoadMore() {
				onDataLoadMore();
			}
		});
	}


	@Override
	protected void onInitLazyDataLoad() {
		LUtil.i("mIsVisible = " + mIsVisible + ", mHasLoadOnce = " + mHasLoadOnce);
		if (!mIsVisible || mHasLoadOnce) {
			return;
		}

		// 模拟新开线程加载东西
		new Thread(new Runnable() {
			@Override
			public void run() {
				int sleepTime = (int)(Math.random() * 10);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage();
				if (sleepTime < 3) {
					msg.what = 0;
					for (int i=0; i<50; i++) {
						mData.add(String.format("第%d条数据之测试数据%d!", i, i));
					}
					mHasLoadOnce = true;
				} else if (sleepTime < 7) {
					msg.what = 1;
					mHasLoadOnce = false;
				} else {
					msg.what = 5;
					mHasLoadOnce = false;
				}
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void notifyDataChange() {
		mListAdapter.notifyDataSetChanged();
	}

	private void onDataRefresh() {
		if (mBeingRefresh) {
			return;
		}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					mData.add(0, "Refresh TextView " + i);
				}
				mHandler.sendEmptyMessage(2);
				mBeingRefresh = false;
			}
		}, 2000);
	}

	private void onDataLoadMore() {
		if (mBeingLoadMore) {
			return;
		}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i< 10; i++) {
					mData.add("LoadMore TextView " + i);
				}
				mHandler.sendEmptyMessage(3);
				mBeingLoadMore = false;
			}
		}, 2000);
	}
}
