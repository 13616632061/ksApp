package com.ui.ks.ReportLoss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bean.GoodInfoRespone;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.R;
import com.ui.ks.ReportLoss.adapter.GoodsInfoAdapter;
import com.ui.ks.ReportLoss.contract.AddReportLossContract;
import com.ui.ks.ReportLoss.presenter.AddReportLossPresenter;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description:添加破损
 * @Author:lyf
 * @Date: 2020/7/19
 */
@Route(path = RouterPath.ACTIVITY_REPORT_LOSS_ADD)
public class AddReportLossActivity extends BaseActivity implements AddReportLossContract.View {

    private static final String TAG = AddReportLossActivity.class.getSimpleName();
    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.list)
    RecyclerView list;

    private CaptureFragment captureFragment;
    private AddReportLossPresenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_add_report_loss;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str385), "");//添加破损
        initScan();
        mPresenter = new AddReportLossPresenter(this);
        mPresenter.initAdapter();

    }

    @OnClick({R.id.btn_search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search://搜索
                mPresenter.searchReportLossGoods(etSearchContent.getText().toString().trim());
                break;
        }
    }


    /**
     * @Description:二维码/条码解析回调函数
     * @Author:lyf
     * @Date: 2020/7/19
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            LogUtils.d(TAG + " scan result: " + result);
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            //扫描成功后，初始化触发再次扫描
            captureFragment.getHandler().sendEmptyMessageDelayed(com.uuzuche.lib_zxing.R.id.restart_preview, 1500);
            mPresenter.searchReportLossGoods(result);
        }

        @Override
        public void onAnalyzeFailed() {
            LogUtils.d(TAG + "  result: onAnalyzeFailed");

        }
    };

    /**
     * @Description:初始化扫码
     * @Author:lyf
     * @Date: 2020/7/19
     */
    @Override
    public void initScan() {
        /**
         * 执行扫面Fragment的初始化操作
         */
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_scan, captureFragment).commit();
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/7/21
     */
    @Override
    public GoodsInfoAdapter initAdapter() {
        GoodsInfoAdapter adapter = new GoodsInfoAdapter(R.layout.item_report_loss_good, mPresenter.getmData());
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_add_loss://添加破损
                        toGoReportLossGoodPage(mPresenter.getmData().get(position));
                        break;
                }
            }
        });
        return adapter;
    }

    /**
     * @Description:跳转破损商品页
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @Override
    public void toGoReportLossGoodPage(GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean bean) {
        ARouter.getInstance().build(RouterPath.ACTIVITY_REPORT_LOSS_GOOD)
                .withParcelable("bean", bean)
                .navigation();
    }


}
