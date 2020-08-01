package com.ui.ks.ReportLoss;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bean.GoodInfoRespone;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.library.utils.BigDecimalArith;
import com.ui.ks.R;
import com.ui.ks.ReportLoss.contract.ReportLossGoodContract;
import com.ui.ks.ReportLoss.presenter.ReportLossGoodPresenter;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description:报损商品
 * @Author:lyf
 * @Date: 2020/7/22
 */
@Route(path = RouterPath.ACTIVITY_REPORT_LOSS_GOOD)
public class ReportLossGoodActivity extends BaseActivity implements ReportLossGoodContract.View {


    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_loss_money)
    EditText etLossMoney;
    @BindView(R.id.et_problem_description)
    EditText etProblemDescription;

    @Autowired(name = "bean")
    GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean bean;
    private ReportLossGoodPresenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_report_loss_good;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str394), "");//报损商品
        mPresenter = new ReportLossGoodPresenter(this);
        setGoodName();
        setListener();
    }

    @OnClick({R.id.btn_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit://提交
                mPresenter.addReportLossGoods(bean);
                break;
        }
    }

    /**
     * @Description:设置事件监听
     * @Author:lyf
     * @Date: 2020/7/28
     */
    @Override
    public void setListener() {
        //申请报损数量变化监听
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    setLossMoney(Double.parseDouble(s.toString()));
                }
            }
        });
    }

    /**
     * @Description:设置商品名字
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @Override
    public void setGoodName() {
        tvGoodName.setText(bean.getName());
    }

    /**
     * @Description:获取商品名字
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @Override
    public String getGoodName() {
        return tvGoodName.getText().toString().trim();
    }

    /**
     * @Description:报损商品申请数量
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @Override
    public String getLossNums() {
        return etNum.getText().toString().trim();
    }

    /**
     * @Description:报损商品申请金额
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @Override
    public void setLossMoney(double num) {
        etLossMoney.setText(new DecimalFormat("######0.00").format(BigDecimalArith.mul(Double.parseDouble(bean.getCost()),num)));
    }

    /**
     * @Description:报损商品申请问题描述
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @Override
    public String getProblemDescription() {
        return etProblemDescription.getText().toString().trim();
    }
}
