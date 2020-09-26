package com.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.constant.RouterPath;
import com.library.base.mvp.BaseFragment;
import com.ui.adapter.GoodsSalesStatisticsAdapter;
import com.ui.fragment.contract.GoodsSalesStatisticsFragmentContract;
import com.ui.fragment.presenter.GoodsSalesStatisticsFragmentPresenter;
import com.ui.ks.R;
import com.ui.ks.TimeFilter.TimeFilterActivity;
import com.ui.weight.SortView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Description:商品销售统计
 * @Author:lyf
 * @Date: 2020/8/2
 */
public class GoodsSalesStatisticsFragment extends BaseFragment<GoodsSalesStatisticsFragmentPresenter> implements GoodsSalesStatisticsFragmentContract.View, SortView.SortListener {

    private static final String TAG = GoodsSalesStatisticsFragment.class.getSimpleName();

    @BindView(R.id.sort_money)
    SortView sortMoney;
    @BindView(R.id.sort_nums)
    SortView sortNums;
    @BindView(R.id.layout_sort)
    LinearLayout layoutSort;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private String startTime;//开始时间
    private String endTime;//结束时间

    @Override
    protected int setContentViewId() {
        return R.layout.layout_goods_sales_statistics;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        sortMoney.setText(getResources().getString(R.string.str406));
        sortNums.setText(getResources().getString(R.string.str407));
        initDateTime();
        sortMoney.setSortListener(this);
        sortNums.setSortListener(this);
    }

    @OnClick({R.id.layout_time})
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.layout_time://筛选
                goToTimeFilter();
                break;
        }
    }

    @Override
    protected void loadData() {
        mPresenter = new GoodsSalesStatisticsFragmentPresenter(this);

        mPresenter.initAdapter();

        mPresenter.goodsStatisticFilter(startTime, endTime);
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public GoodsSalesStatisticsAdapter initAdapter() {
        GoodsSalesStatisticsAdapter adapter = new GoodsSalesStatisticsAdapter(R.layout.item_goods_sales_statistics, mPresenter.getmData());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(mActivity));
        return adapter;
    }

    /**
     * @Description:空视图
     * @Author:lyf
     * @Date: 2020/8/11
     */
    @Override
    public View setEmptyView() {
        View view = View.inflate(mActivity, R.layout.layout_empty_view, null);
        return view;
    }

    /**
     * @Description:设置筛选时间
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void setTvTime(String time) {
        tvTime.setText(time);
    }

    /**
     * @Description:跳转时间筛选
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void goToTimeFilter() {
//         ARouter.getInstance().build(RouterPath.ACTIVITY_TIME_FILTER).navigation(getActivity(), 200);
        Intent intent = new Intent();
        intent.setClass(getActivity(), TimeFilterActivity.class);
        startActivityForResult(intent, 200);
    }

    /**
     * @Description:初始化日期时间
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void initDateTime() {
        Calendar mCalendar = Calendar.getInstance(Locale.CHINA);//设置为中国时间
        DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");//设置时间格式
        Date mDate = new Date(System.currentTimeMillis());//获取当前系统时间
        startTime = mDateFormat.format(mDate) + " 00:00";
        endTime = mDateFormat.format(mDate) + " " + mTimeFormat.format(mDate);
        setTvTime(startTime + "--" + endTime);
    }

    /**
     * @Description:升序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void setUpSort(View view) {
        switch (view.getId()) {
            case R.id.sort_money://销售额
                mPresenter.setSaleMoneyUpSort();
                sortNums.setDefautSort();
                break;
            case R.id.sort_nums://销售数量
                mPresenter.setSaleNumsUpSort();
                sortMoney.setDefautSort();
                break;
        }
    }

    /**
     * @Description:降序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public void setDownSort(View view) {
        switch (view.getId()) {
            case R.id.sort_money://销售额
                mPresenter.setSaleMoneyDownSort();
                sortNums.setDefautSort();
                break;
            case R.id.sort_nums://销售数量
                mPresenter.setSaleNumsDownSort();
                sortMoney.setDefautSort();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            String startTime = data.getExtras().getString("startTime");
            String endTime = data.getExtras().getString("endTime");
            LogUtils.i(TAG + "onActivityResult:  " + startTime + "--" + endTime);
            setTvTime(startTime + "--" + endTime);
            mPresenter.goodsStatisticFilter(startTime, endTime);
//            mPresenter.getReportLossList(startTime, endTime);
        }
    }


}
