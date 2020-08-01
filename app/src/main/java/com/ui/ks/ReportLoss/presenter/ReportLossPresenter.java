package com.ui.ks.ReportLoss.presenter;

import com.bean.ReportLostListRespone;
import com.library.base.mvp.BasePresenter;
import com.ui.ks.ReportLoss.ReportLossListActivity;
import com.ui.ks.ReportLoss.adapter.ReportLossListaAdapter;
import com.ui.ks.ReportLoss.contract.ReportLossListContract;
import com.ui.ks.ReportLoss.model.ReportLossModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/7/19.
 */

public class ReportLossPresenter extends BasePresenter<ReportLossListActivity> implements ReportLossListContract.Presenter {

    private ReportLossModel mModel;
    private List<ReportLostListRespone.ResponseBean.DataBean> mData = new ArrayList<>();
    private ReportLossListaAdapter mAdapter;

    public ReportLossPresenter(ReportLossListActivity mView) {
        super(mView);
        mModel = new ReportLossModel();
    }

    public List<ReportLostListRespone.ResponseBean.DataBean> getmData() {
        return mData;
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/7/19
     */
    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:报损列表
     * @Author:lyf
     * @Date: 2020/7/19
     */
    @Override
    public void getReportLossList() {
        mView.showLoading();
        addSubscription(mModel.getReportLossList(), new Subscriber<ReportLostListRespone>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
            }

            @Override
            public void onNext(ReportLostListRespone respone) {
                mView.hideLoading();
                if (respone != null && respone.getResponse() != null
                        && "200".equals(respone.getResponse().getStatus())) {
                    mData.clear();
                    mData.addAll(respone.getResponse().getData());
                    mAdapter.notifyDataSetChanged();

                }
            }
        });
    }
}
