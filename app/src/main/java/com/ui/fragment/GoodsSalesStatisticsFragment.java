package com.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.library.base.mvp.BaseFragment;
import com.ui.adapter.GoodsSalesStatisticsAdapter;
import com.ui.fragment.contract.GoodsSalesStatisticsFragmentContract;
import com.ui.fragment.presenter.GoodsSalesStatisticsFragmentPresenter;
import com.ui.ks.R;
import com.ui.weight.SortView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Description:商品销售统计
 * @Author:lyf
 * @Date: 2020/8/2
 */
public class GoodsSalesStatisticsFragment extends BaseFragment<GoodsSalesStatisticsFragmentPresenter> implements GoodsSalesStatisticsFragmentContract.View, SortView.SortListener {


    @BindView(R.id.sort_money)
    SortView sortMoney;
    @BindView(R.id.sort_nums)
    SortView sortNums;
    @BindView(R.id.layout_sort)
    LinearLayout layoutSort;
    @BindView(R.id.list)
    RecyclerView list;

    @Override
    protected int setContentViewId() {
        return R.layout.layout_goods_sales_statistics;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        sortMoney.setText(getResources().getString(R.string.str406));
        sortNums.setText(getResources().getString(R.string.str407));

        sortMoney.setSortListener(this);
        sortNums.setSortListener(this);
    }

    @Override
    protected void loadData() {
        mPresenter = new GoodsSalesStatisticsFragmentPresenter(this);

        mPresenter.initAdapter();

        mPresenter.goodsSalesStatistics();
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
}
