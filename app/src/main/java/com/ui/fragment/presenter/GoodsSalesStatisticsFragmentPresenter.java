package com.ui.fragment.presenter;

import com.api.SubscriberCallBack;
import com.library.base.mvp.BasePresenter;
import com.library.utils.BigDecimalArith;
import com.ui.adapter.GoodsSalesStatisticsAdapter;
import com.ui.entity.GoodsSalesStatisticsRespone;
import com.ui.fragment.GoodsSalesStatisticsFragment;
import com.ui.fragment.contract.GoodsSalesStatisticsFragmentContract;
import com.ui.fragment.model.GoodsSalesStatisticsFragmentModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/8/2.
 */

public class GoodsSalesStatisticsFragmentPresenter extends BasePresenter<GoodsSalesStatisticsFragment> implements GoodsSalesStatisticsFragmentContract.Presenter {

    private GoodsSalesStatisticsFragmentModel mModel;
    private GoodsSalesStatisticsAdapter mAdapter;
    private List<GoodsSalesStatisticsRespone.ResponseBean.DataBean> mData = new ArrayList<>();

    public GoodsSalesStatisticsFragmentPresenter(GoodsSalesStatisticsFragment mView) {
        super(mView);
        mModel = new GoodsSalesStatisticsFragmentModel();
    }

    /**
     * @Description:列表数据
     * @Author:lyf
     * @Date: 2020/8/2
     */
    public List<GoodsSalesStatisticsRespone.ResponseBean.DataBean> getmData() {
        return mData;
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:商品销售统计
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void goodsSalesStatistics() {
        mView.showLoading();
        addSubscription(mModel.goodsSalesStatistics(), new Subscriber<GoodsSalesStatisticsRespone>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();

            }

            @Override
            public void onNext(GoodsSalesStatisticsRespone respone) {
                mView.hideLoading();
                if (respone != null && respone.getResponse() != null) {
                    if ("200".equals(respone.getResponse().getStatus())) {
                        mData.addAll(respone.getResponse().getData());
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }


        });
    }

    /**
     * @Description:销售额升序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void setSaleMoneyUpSort() {
        //冒泡排序
        for (int i = 0; i < mData.size() - 1; i++) {
            for (int j = 0; j < mData.size() - 1 - i; j++) {
                if (Double.parseDouble(mData.get(j).getPrice()) > Double.parseDouble(mData.get(j + 1).getPrice())) {
                    GoodsSalesStatisticsRespone.ResponseBean.DataBean bean = mData.get(j + 1);
                    mData.set(j + 1, mData.get(j));
                    mData.set(j, bean);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @Description:销售额降序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void setSaleMoneyDownSort() {
        for (int i = 0; i < mData.size() - 1; i++) {
            for (int j = 0; j < mData.size() - 1 - i; j++) {
                if (Double.parseDouble(mData.get(j).getPrice()) < Double.parseDouble(mData.get(j + 1).getPrice())) {
                    GoodsSalesStatisticsRespone.ResponseBean.DataBean bean = mData.get(j + 1);
                    mData.set(j + 1, mData.get(j));
                    mData.set(j, bean);
                }
            }
        }

        mAdapter.notifyDataSetChanged();

    }

    /**
     * @Description:销售数量升序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void setSaleNumsUpSort() {
        for (int i = 0; i < mData.size() - 1; i++) {
            for (int j = 0; j < mData.size() - 1 - i; j++) {
                if (Double.parseDouble(mData.get(j).getNums()) > Double.parseDouble(mData.get(j + 1).getNums())) {
                    GoodsSalesStatisticsRespone.ResponseBean.DataBean bean = mData.get(j + 1);
                    mData.set(j + 1, mData.get(j));
                    mData.set(j, bean);

                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @Description:销售数量降序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void setSaleNumsDownSort() {

        for (int i = 0; i < mData.size() - 1; i++) {
            for (int j = 0; j < mData.size() - 1 - i; j++) {
                if (Double.parseDouble(mData.get(j).getNums()) < Double.parseDouble(mData.get(j + 1).getNums())) {
                    GoodsSalesStatisticsRespone.ResponseBean.DataBean bean = mData.get(j + 1);
                    mData.set(j + 1, mData.get(j));
                    mData.set(j, bean);

                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
