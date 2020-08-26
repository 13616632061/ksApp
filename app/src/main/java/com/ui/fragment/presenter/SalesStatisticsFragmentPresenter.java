package com.ui.fragment.presenter;

import android.text.TextUtils;

import com.bean.SalesStatisticsRespone;
import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BasePresenter;
import com.library.weight.ChartEntity;
import com.ui.fragment.SalesStatisticsFragment;
import com.ui.fragment.contract.SalesStatisticsFragmentContract;
import com.ui.fragment.model.SalesStatisticsFragmentModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/8/5.
 */

public class SalesStatisticsFragmentPresenter extends BasePresenter<SalesStatisticsFragment> implements SalesStatisticsFragmentContract.Presenter {

    private static final String TAG = SalesStatisticsFragmentPresenter.class.getSimpleName();
    private SalesStatisticsFragmentModel mModel;
    private List<ChartEntity> curMonthcashData = new ArrayList<>();
    private List<ChartEntity> curMonthLineData = new ArrayList<>();
    private List<ChartEntity> curMonthMemberData = new ArrayList<>();

    public SalesStatisticsFragmentPresenter(SalesStatisticsFragment mView) {
        super(mView);
        mModel = new SalesStatisticsFragmentModel();
    }

    /**
     * @Description:设置图表数据
     * @Author:lyf
     * @Date: 2020/8/5
     */
    @Override
    public void setChartEntityData(SalesStatisticsRespone.ResponseBean.DataBean response, List<ChartEntity> chartEntities) {
        try {
            if (response != null && response.getOrders_info() != null && response.getOrders_info().size() > 0) {
                for (int i = 0; i < response.getOrders_info().size(); i++) {
                    String moneyDay = "0.00";
                    if (!TextUtils.isEmpty(response.getOrders_info().get(response.getOrders_info().size() - i - 1).getTotal_money_day())) {
                        moneyDay = response.getOrders_info().get(response.getOrders_info().size() - i - 1).getTotal_money_day();
                    }
                    ChartEntity chartEntity = new ChartEntity(String.valueOf(i + 1), Float.parseFloat(moneyDay));
                    chartEntities.add(chartEntity);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG + "e: " + e.toString());
        }

    }

    /**
     * @Description:当月现金统计
     * @Author:lyf
     * @Date: 2020/8/5
     */
    @Override
    public void curMonthcashStatistics(int month) {
        addSubscription(mModel.curMonthcashStatistics(month), new Subscriber<SalesStatisticsRespone>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SalesStatisticsRespone response) {
                if (response.getResponse() != null) {
                    if ("200".equals(response.getResponse().getStatus())) {
                        curMonthcashData.clear();
                        setChartEntityData(response.getResponse().getData(), curMonthcashData);
                        mView.curMonthcashStatisticsChart(curMonthcashData);
                    }
                }
            }

        });
    }

    /**
     * @Description:当月移动统计
     * @Author:lyf
     * @Date: 2020/8/8
     */
    @Override
    public void curMonthLineStatistics(int month) {
        addSubscription(mModel.curMonthLineStatistics(month), new Subscriber<SalesStatisticsRespone>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SalesStatisticsRespone response) {
                if (response.getResponse() != null) {
                    if ("200".equals(response.getResponse().getStatus())) {
                        curMonthLineData.clear();
                        setChartEntityData(response.getResponse().getData(), curMonthLineData);
                        mView.curMonthLineStatisticsChart(curMonthLineData);
                    }
                }
            }

        });
    }

    /**
     * @Description:当月会员统计
     * @Author:lyf
     * @Date: 2020/8/8
     */
    @Override
    public void curMonthMemberStatistics(int month) {
        addSubscription(mModel.curMonthMemberStatistics(month), new Subscriber<SalesStatisticsRespone>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SalesStatisticsRespone response) {
                if (response.getResponse() != null) {
                    if ("200".equals(response.getResponse().getStatus())) {
                        curMonthMemberData.clear();
                        setChartEntityData(response.getResponse().getData(), curMonthMemberData);
                        mView.curMonthMemberStatistics(curMonthMemberData);
                    }
                }
            }

        });
    }
}
