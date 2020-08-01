package com.ui.ks.ReportLoss;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bean.ReportLostListRespone;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.R;
import com.ui.ks.ReportLoss.adapter.ReportLossListaAdapter;
import com.ui.ks.ReportLoss.contract.ReportLossListContract;
import com.ui.ks.ReportLoss.presenter.ReportLossPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description:报损商品列表
 * @Author:lyf
 * @Date: 2020/7/19
 */

@Route(path = RouterPath.ACTIVITY_REPORT_LOSS)
public class ReportLossListActivity extends BaseActivity implements ReportLossListContract.View {


    @BindView(R.id.list)
    RecyclerView list;

    private ReportLossPresenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_report_loss;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initTabTitle(getResources().getString(R.string.str381), getResources().getString(R.string.str382));//报损
        mPresenter = new ReportLossPresenter(this);
        mPresenter.initAdapter();
        mPresenter.getReportLossList();

    }

    @OnClick({R.id.toolbar_right})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right://添加
                toAddReportLoss();
                break;
        }
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/7/28
     */
    @Override
    public ReportLossListaAdapter initAdapter() {
        ReportLossListaAdapter adapter = new ReportLossListaAdapter(R.layout.item_resport_lost_list, mPresenter.getmData());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        //列表点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toGoResportLossDetail(mPresenter.getmData().get(position));
            }
        });
        return adapter;
    }

    /**
     * @Description:添加报损
     * @Author:lyf
     * @Date: 2020/7/28
     */
    @Override
    public void toAddReportLoss() {
        ARouter.getInstance().build(RouterPath.ACTIVITY_REPORT_LOSS_ADD).navigation();

    }

    /**
     * @Description:跳转报损详情页
     * @Author:lyf
     * @Date: 2020/7/28
     */
    @Override
    public void toGoResportLossDetail(ReportLostListRespone.ResponseBean.DataBean bean) {
        ARouter.getInstance().build(RouterPath.ACTIVITY_REPORT_LOSS_DETAIL)
                .withParcelable("bean", bean)
                .navigation();

    }

    /**
     * @Description:EventBus事件处理
     * @Author:lyf
     * @Date: 2020/7/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("lossListRefresh")) {
            mPresenter.getReportLossList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
