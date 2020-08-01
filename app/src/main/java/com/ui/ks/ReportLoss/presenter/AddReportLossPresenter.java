package com.ui.ks.ReportLoss.presenter;

import android.text.TextUtils;

import com.bean.GoodInfoRespone;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BasePresenter;
import com.ui.ks.ReportLoss.AddReportLossActivity;
import com.ui.ks.ReportLoss.adapter.GoodsInfoAdapter;
import com.ui.ks.ReportLoss.contract.AddReportLossContract;
import com.ui.ks.ReportLoss.model.AddReportLossModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/7/19.
 */

public class AddReportLossPresenter extends BasePresenter<AddReportLossActivity> implements AddReportLossContract.Presenter {

    private static final String TAG = AddReportLossPresenter.class.getSimpleName();
    private AddReportLossModel mModel;

    private List<GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean> mData = new ArrayList<>();
    private GoodsInfoAdapter mAdapter;

    public AddReportLossPresenter(AddReportLossActivity mView) {
        super(mView);
        mModel = new AddReportLossModel();
    }

    public List<GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean> getmData() {
        return mData;
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/7/21
     */
    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:搜索报损商品
     * @Author:lyf
     * @Date: 2020/7/19
     */
    @Override
    public void searchReportLossGoods(String str) {
        KeyboardUtils.hideSoftInput(mView);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        addSubscription(mModel.searchReportLossGoods(str), new Subscriber<GoodInfoRespone>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GoodInfoRespone respone) {
                try {
                    mData.addAll(respone.getResponse().getData().getGoods_info());
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    LogUtils.eTag(TAG, e.toString());
                }
            }
        });
    }
}
