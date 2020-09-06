package com.ui.ks.ReportLoss;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bean.ReportLostListRespone;
import com.blankj.utilcode.util.TimeUtils;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * @Description:报损商品列表
 * @Author:lyf
 * @Date: 2020/7/19
 */

@Route(path = RouterPath.ACTIVITY_REPORT_LOSS)
public class ReportLossListActivity extends BaseActivity implements ReportLossListContract.View {


    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.layout_time)
    RelativeLayout layoutTime;

    private ReportLossPresenter mPresenter;
    private String startTime;//开始时间
    private String endTime;//结束时间

    @Override
    public int getContentView() {
        return R.layout.activity_report_loss;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initTabTitle(getResources().getString(R.string.str381), getResources().getString(R.string.str382));//报损
        initDateTime();
        mPresenter = new ReportLossPresenter(this);
        mPresenter.initAdapter();
        mPresenter.getReportLossList(startTime, endTime);

    }

    @OnClick({R.id.toolbar_right, R.id.layout_time})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right://添加
                requestPermission();
                break;
            case R.id.layout_time://筛选
                goToTimeFilter();
                break;
        }
    }

    /**
     * @Description:初始化日期时间
     * @Author:lyf
     * @Date: 2020/8/15
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
     * @Description:设置筛选时间
     * @Author:lyf
     * @Date: 2020/8/9
     */
    @Override
    public void setTvTime(String time) {
        tvTime.setText(time);
    }

    /**
     * @Description:请求权限
     * @Author:lyf
     * @Date: 2020/8/10
     */
    @Override
    public void requestPermission() {
        PermissionGen.with(this)
                .addRequestCode(200)
                .permissions(
                        Manifest.permission.CAMERA
                )
                .request();
    }

    /**
     * @Description:跳转时间筛选
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void goToTimeFilter() {
        ARouter.getInstance().build(RouterPath.ACTIVITY_TIME_FILTER).navigation(this, 200);
    }

    /**
     * @Description:空视图
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public View setEmptyView() {
        View view = View.inflate(this, R.layout.layout_empty_view, null);
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            String startTime = data.getExtras().getString("startTime");
            String endTime = data.getExtras().getString("endTime");
            setTvTime(startTime + "--" + endTime);
            mPresenter.getReportLossList(startTime, endTime);
        }
    }

    /**
     * @Description:EventBus事件处理
     * @Author:lyf
     * @Date: 2020/7/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("lossListRefresh")) {
            initDateTime();
            mPresenter.getReportLossList(startTime, endTime);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 200)
    public void permissionSuccess() {
        toAddReportLoss();
    }

    @PermissionFail(requestCode = 200)
    public void permissionFailure() {
        Toast.makeText(this, getString(R.string.str145), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }


}
