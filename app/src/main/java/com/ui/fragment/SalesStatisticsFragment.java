package com.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.library.base.mvp.BaseFragment;
import com.library.weight.ChartEntity;
import com.library.weight.LineChart;
import com.ui.fragment.contract.SalesStatisticsFragmentContract;
import com.ui.fragment.presenter.SalesStatisticsFragmentPresenter;
import com.ui.ks.R;
import com.ui.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.member_chart)
    LineChart memberChart;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private SalesStatisticsFragmentPresenter mPresenter;
    private int month = 0;


    @Override
    protected int setContentViewId() {
        return R.layout.sales_statistics_layout;
    }

    @Override
    protected void loadData() {
        if (getArguments() != null) {
            month = getArguments().getInt("month");
        }
        setTvDate();
        LogUtils.i("SalesStatisticsFragment" + month);
        mPresenter = new SalesStatisticsFragmentPresenter(this);
        mPresenter.curMonthcashStatistics(month);
        mPresenter.curMonthLineStatistics(month);
        mPresenter.curMonthMemberStatistics(month);

    }


    /**
     * @Description:统计时间
     * @Author:lyf
     * @Date: 2020/8/8
     */
    @Override
    public void setTvDate() {
        tvDate.setText(DateUtils.getMonthDate(month) + "");
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

    /**
     * @Description:当月移动统计图表
     * @Author:lyf
     * @Date: 2020/8/8
     */
    @Override
    public void curMonthLineStatisticsChart(List<ChartEntity> chartEntities) {
        lineChart.setData(chartEntities);
        lineChart.startAnimation(2000);
    }

    @Override
    public void curMonthMemberStatistics(List<ChartEntity> chartEntities) {
        memberChart.setData(chartEntities);
        memberChart.startAnimation(2000);
    }


}
