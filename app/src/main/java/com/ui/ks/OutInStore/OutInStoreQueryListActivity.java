package com.ui.ks.OutInStore;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.OutInStore.adapter.QueryOutofStoreListAdapter;
import com.ui.ks.OutInStore.contract.OutInStoreQueryListContract;
import com.ui.ks.OutInStore.presenter.OutInStoreQueryListPresenter;
import com.ui.ks.Out_In_DetailActivity;
import com.ui.ks.R;

import butterknife.BindView;

/**
 * 出入库查询列表页
 * Created by admin on 2018/5/31.
 */

@Route(path = RouterPath.ACTIVITY_OUT_IN_SOTRE_QUERY)
public class OutInStoreQueryListActivity extends BaseActivity implements OutInStoreQueryListContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TAG = OutInStoreQueryListActivity.class.getSimpleName();

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_order_total_money)
    TextView tvOrderTotalMoney;
    @BindView(R.id.tv_order_bi)
    TextView tvOrderBi;
    @BindView(R.id.tv_order_total_num)
    TextView tvOrderTotalNum;
    @BindView(R.id.tv_order_gong)
    TextView tvOrderGong;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    @Autowired(name = "date_begin")
    String mStartDate;
    @Autowired(name = "date_end")
    String mEndDate;

    private OutInStoreQueryListPresenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_out_in_store_list;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.title_activity_search), "");//查询结果
        showLoading();
        mPresenter = new OutInStoreQueryListPresenter(this);
        mPresenter.queryOutInStoreListData(mStartDate + ":00", mEndDate + ":00");
        mPresenter.initAdapter();
        OnRefresh();

    }


    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public QueryOutofStoreListAdapter initAdapter() {
        QueryOutofStoreListAdapter adapter = new QueryOutofStoreListAdapter(R.layout.item_outofstorage, mPresenter.getmData());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        //item点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goToOutInStoreDetail(position);
            }
        });
        //上拉加载监听
        adapter.setOnLoadMoreListener(this, list);

        return adapter;
    }

    /**
     * @Description:跳转出入库详情
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void goToOutInStoreDetail(int position) {
        Intent intent1 = new Intent(OutInStoreQueryListActivity.this, Out_In_DetailActivity.class);
        intent1.putExtra("order_id", mPresenter.getmData().get(position).getId());
        intent1.putExtra("total", mPresenter.getmData().get(position).getMoney());
        startActivity(intent1);
    }

    /**
     * @Description:下拉刷新
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void OnRefresh() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.OnRefresh();
            }
        });
    }

    /**
     * @Description:下拉刷新状态
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void setRefreshEnable(boolean enable) {
        refresh.setRefreshing(enable);
    }

    /**
     * @Description:总数量
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void setOrderTotalNum(String totalNum) {
        tvOrderTotalNum.setText(totalNum);
    }

    /**
     * @Description:上拉加载
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMoreRequested();
    }
}
