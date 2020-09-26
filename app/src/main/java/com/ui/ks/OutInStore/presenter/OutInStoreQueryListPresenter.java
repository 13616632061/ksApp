package com.ui.ks.OutInStore.presenter;

import com.alibaba.android.arouter.utils.TextUtils;
import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BasePresenter;
import com.ui.entity.OutInStoreListResponse;
import com.ui.ks.OutInStore.OutInStoreQueryListActivity;
import com.ui.ks.OutInStore.adapter.QueryOutofStoreListAdapter;
import com.ui.ks.OutInStore.contract.OutInStoreQueryListContract;
import com.ui.ks.OutInStore.model.OutInStoreQueryListModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 出入库列表查询
 * Created by lyf on 2020/9/12.
 */

public class OutInStoreQueryListPresenter extends BasePresenter<OutInStoreQueryListActivity> implements OutInStoreQueryListContract.Presnter {
    private static final String TAG = OutInStoreQueryListPresenter.class.getSimpleName();

    private OutInStoreQueryListModel mModel = new OutInStoreQueryListModel();
    private int curPage = 1;//当前页
    private List<OutInStoreListResponse.ResponseBean.DataBean.ListBean> mData = new ArrayList<>();
    private QueryOutofStoreListAdapter mAdapter;

    private String startTime;//开始时间
    private String endTime;//结束时间

    public OutInStoreQueryListPresenter(OutInStoreQueryListActivity mView) {
        super(mView);
    }


    public List<OutInStoreListResponse.ResponseBean.DataBean.ListBean> getmData() {
        return mData;
    }

    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:查询出入库列表数据
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void queryOutInStoreListData(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        addSubscription(mModel.queryOutInStoreListData(startTime, endTime, curPage), new Subscriber<OutInStoreListResponse>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                mView.setRefreshEnable(false);
            }

            @Override
            public void onNext(OutInStoreListResponse response) {
                mView.hideLoading();
                mView.setRefreshEnable(false);
                if (response.getResponse() != null
                        && response.getResponse().getData() != null
                        && response.getResponse().getData().getList() != null) {
                    if (curPage == 1) {
                        mData.clear();
                    }
                    mData.addAll(response.getResponse().getData().getList());
                    mAdapter.notifyDataSetChanged();
                    LogUtils.i(TAG + " size " + mData.size());
                    //上拉加载
                    handleLoadMore(response);

                    mView.setOrderTotalNum(mData.size() + "");
                }
            }
        });
    }

    /**
     * @Description:下拉刷新
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void OnRefresh() {
        curPage = 1;
        queryOutInStoreListData(startTime, endTime);
    }

    /**
     * @Description:上拉加载
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void onLoadMoreRequested() {
        curPage++;
        queryOutInStoreListData(startTime, endTime);
    }

    /**
     * @Description:上拉加载数据处理
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void handleLoadMore(OutInStoreListResponse response) {
        if (response.getResponse().getData().getTotal() != null
                && response.getResponse().getData().getTotal().size() > 0) {
            String totalNumStr = response.getResponse().getData().getTotal().get(0).getNum();
            if (!TextUtils.isEmpty(totalNumStr)) {
                int totalNum = Integer.parseInt(totalNumStr);
                if (curPage >= mView.totalPage(totalNum)) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
            }

        }
    }
}
