package com.ui.ks.GoodsSearch.presenter;

import android.text.TextUtils;

import com.bean.GoodInfoRespone;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BasePresenter;
import com.ui.ks.GoodsSearch.GoodsInfoSearchActivity;
import com.ui.ks.GoodsSearch.adapter.GoodsInfoSearchAdapter;
import com.ui.ks.GoodsSearch.contract.GoodsSearchContract;
import com.ui.ks.ReportLoss.AddReportLossActivity;
import com.ui.ks.ReportLoss.adapter.GoodsInfoAdapter;
import com.ui.ks.ReportLoss.model.AddReportLossModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/8/21.
 */

public class GoodsInfoSearchPresenter extends BasePresenter<GoodsInfoSearchActivity> implements GoodsSearchContract.Presenter {

    private static final String TAG = GoodsInfoSearchPresenter.class.getSimpleName();

    private AddReportLossModel mModel;

    private List<GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean> mData = new ArrayList<>();
    private GoodsInfoSearchAdapter mAdapter;

    public GoodsInfoSearchPresenter(GoodsInfoSearchActivity mView) {
        super(mView);
        mModel = new AddReportLossModel();
    }

    public List<GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean> getmData() {
        return mData;
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/8/21
     */
    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:搜索商品
     * @Author:lyf
     * @Date: 2020/8/21
     */
    @Override
    public void searchGoodsInfo(String result) {
        KeyboardUtils.hideSoftInput(mView);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        mView.showLoading();
        addSubscription(mModel.searchReportLossGoods(result), new Subscriber<GoodInfoRespone>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                LogUtils.eTag(TAG, e.toString());
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

    /**
     * @Description:空视图
     * @Author:lyf
     * @Date: 2020/8/21
     */
    @Override
    public void setEmptyView() {
        if (mData.size() <= 0) {
            mAdapter.setEmptyView(mView.setEmptyView());
        }
        mAdapter.notifyDataSetChanged();
    }
}
