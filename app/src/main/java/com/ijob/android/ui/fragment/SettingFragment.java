package com.ijob.android.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.ijob.android.R;
import com.ijob.android.ui.adapter.RecyclerListAdapter;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.T;

import java.util.ArrayList;

/**
 * Created by JackieZhuang on 2015/1/28.
 */
public class SettingFragment extends BaseDataFragment {

	private static final String LOG_TAG = SettingFragment.class.toString();
	public static final String EXT_POS = "extPos";

	private boolean mHasLoadOnce = false;
	private boolean mBeingRefresh = false;
	private boolean mBeingLoadMore = false;

	private View mConvertView;
	private View mStubLoad;
	private RecyclerView mlvDataView;
	private SwipeRefreshLayout srlRefreshView;
	private RecyclerListAdapter mListAdapter;

	/**
	 * 使用静态减少内存泄露
	 */
	private static SettingFragment mThis;
	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			assert (mThis != null);
			mThis.srlRefreshView.setRefreshing(false);
			switch (msg.what) {
				case 0:
					T.showLInCenter(AppInfo.sAppContext, "异步加载成功");
					mThis.mStubLoad.setVisibility(View.GONE);
					mThis.srlRefreshView.setVisibility(View.VISIBLE);
					mThis.notifyDataChange();
					break;
				case 1:
					LUtil.i(LOG_TAG, "异步加载失败, 重新加载！");
					mThis.onInitLazyDataLoad();
					break;
				case 2:
					mThis.notifyDataChange();
					break;
				case 3:
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

	public static SettingFragment newInstance(int pos) {
		Bundle bundle = new Bundle();
		bundle.putInt(SettingFragment.EXT_POS, pos);
		SettingFragment fragment = new SettingFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mThis = this;
		mData = new ArrayList<>();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mConvertView == null) {
			mConvertView = inflater.inflate(R.layout.fragment_setting, container,
					false);
			mStubLoad = ((ViewStub) mConvertView.findViewById(R.id.stubLoad)).inflate();
			mlvDataView = (RecyclerView) mConvertView.findViewById(R.id.lvDataView);
			srlRefreshView = (SwipeRefreshLayout) mConvertView.findViewById(R.id.srlRefreshView);
			initListView();
		}
		ViewGroup group = (ViewGroup) mConvertView.getParent();
		if (group != null) {
			group.removeView(mConvertView);
		}
		return mConvertView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RecyclerListAdapter.clearImages();
	}

	private void initListView() {
		srlRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				onDataRefresh();
			}
		});
		mListAdapter = new RecyclerListAdapter(getActivity(), mData);
		mlvDataView.setAdapter(mListAdapter);
		mlvDataView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mlvDataView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
				int lastVisibleItemPos = manager.findLastVisibleItemPosition();
				int totalItemCount = manager.getItemCount();
				LUtil.i(LOG_TAG, "dx = " + dx + ", dy = " + dy + ", lastVisiblePos = " + lastVisibleItemPos + ", " +
						"totalCount = " + totalItemCount);
				if (lastVisibleItemPos >= totalItemCount - 4 && dy > 0) {
					onDataLoadMore();
				}
			}
		});
	}

	@Override
	protected void onInitLazyDataLoad() {
		// doNothing
		if (!mIsVisible || mHasLoadOnce) {
			return;
		}

		// 模拟新开线程加载东西
		new Thread(new Runnable() {
			@Override
			public void run() {
				int sleepTime = (int) (Math.random() * 10);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage();
				if (sleepTime < 3) {
					msg.what = 0;
					for (int i = 0; i < 50; i++) {
						mData.add(String.format("第%d条数据之测试数据%d!", i, i));
					}
					mHasLoadOnce = true;
				} else if (sleepTime < 7) {
					msg.what = 1;
					mHasLoadOnce = false;
				} else {
					msg.what = -1;
					mHasLoadOnce = false;
				}
				mHandler.sendMessage(msg);
			}
		}).start();
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
				for (int i = 0; i < 10; i++) {
					mData.add("LoadMore TextView " + i);
				}
				mHandler.sendEmptyMessage(3);
				mBeingLoadMore = false;
			}
		}, 1000);
	}

	private void notifyDataChange() {
		mListAdapter.notifyDataSetChanged();
	}
}
