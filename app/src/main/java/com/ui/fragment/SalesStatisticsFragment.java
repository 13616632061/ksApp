package com.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.library.base.mvp.BaseFragment;
import com.library.weight.ChartEntity;
import com.library.weight.LineChart;
import com.ui.fragment.contract.SalesStatisticsFragmentContract;
import com.ui.fragment.presenter.SalesStatisticsFragmentPresenter;
import com.ui.ks.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lyf on 2020/8/2.
 */

public class SalesStatisticsFragment extends BaseFragment implements SalesStatisticsFragmentContract.View {


    @BindView(R.id.cash_chart)
    LineChart cashChart;
    @BindView(R.id.line_chart)
    LineChart lineChart;
    private SalesStatisticsFragmentPresenter mPresenter;

    @Override
    protected int setContentViewId() {
        return R.layout.sales_statistics_layout;
    }

    @Override
    protected void loadData() {

        mPresenter = new SalesStatisticsFragmentPresenter(this);
        mPresenter.curMonthcashStatistics();


    }

    /**
     * @Description:当月现金统计图表
     * @Author:lyf
     * @Date: 2020/8/5
     */
    @Override
    public void curMonthcashStatisticsChart(List<ChartEntity> chartEntities) {
        cashChart.setData(chartEntities);
        cashChart.startAnimation(2000);
    }


}
