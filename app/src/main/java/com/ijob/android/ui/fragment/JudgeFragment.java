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

import java.util.ArrayList;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;

/**
 * Created by JackieZhuang on 2015/2/1.
 */
public class JudgeFragment extends BaseDataFragment {

	private static final String LOG_TAG = JudgeFragment.class.toString();
	private static final String INIT_MSG = "initMsg";

	private boolean mHasLoadOnce = false;
	private boolean mBeingRefresh = false;
	private boolean mBeingLoadMore = false;

	private View mConvertView;
	private View mStubLoad;
	private ZrcListView mlvDataView;
	private CommonListAdapter mListAdapter;


	/**
	 * 使用静态减少内存泄露
	 */
	private static JudgeFragment mThis;
	private static Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			assert (mThis != null);
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
					mThis.mlvDataView.setRefreshSuccess();
					mThis.notifyDataChange();
					break;
				case 3:
					mThis.mlvDataView.setLoadMoreSuccess();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mThis = this;
		mData = new ArrayList<>();
	}

	public static JudgeFragment newInstance(String msg) {
		Bundle bundle = new Bundle();
		bundle.putString(JudgeFragment.INIT_MSG, msg);
		JudgeFragment instance = new JudgeFragment();
		instance.setArguments(bundle);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
			savedInstanceState) {
		if (mConvertView == null) {
			mConvertView = inflater.inflate(R.layout.fragment_judge, container,
					false);
			mStubLoad = ((ViewStub) mConvertView.findViewById(R.id.stubLoad)).inflate();
			mlvDataView = (ZrcListView) mConvertView.findViewById(R.id.lvDataView);
			initListView();
		}
		ViewGroup group = (ViewGroup) mConvertView.getParent();
		if (group != null) {
			group.removeView(mConvertView);
		}
		return mConvertView;
	}

	private void initListView() {
		mListAdapter = new CommonListAdapter(getActivity(), mData);
		mlvDataView.setAdapter(mListAdapter);
		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		mlvDataView.setHeadable(header);
		// 设置加载更多的样式（可选，但如果缺少将不显示加载提示）
		SimpleFooter footer = new SimpleFooter(getActivity());
		footer.setCircleColor(0xff33bbee);
		mlvDataView.setFootable(footer);
		// 控制是否开启加载更多设置
		mlvDataView.startLoadMore();
		// 下拉刷新回调
		mlvDataView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
			@Override
			public void onStart() {
				onDataRefresh();
			}
		});
		// 加载更多回调
		mlvDataView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
			@Override
			public void onStart() {
				onDataLoadMore();
			}
		});
	}

	@Override
	protected void onInitLazyDataLoad() {
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
					msg.what = -1;
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
				for (int i = 0; i< 10; i++) {
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
