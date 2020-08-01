package com.ui.ks.ReportLoss;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bean.ReportLostListRespone;
import com.blankj.utilcode.util.TimeUtils;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description:报损详情
 * @Author:lyf
 * @Date: 2020/7/28
 */
@Route(path = RouterPath.ACTIVITY_REPORT_LOSS_DETAIL)
public class ReportLossDetailActivity extends BaseActivity {


    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_loss_money)
    TextView tvLossMoney;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_loss_reason)
    TextView tvLossReason;

    @Autowired(name = "bean")
    ReportLostListRespone.ResponseBean.DataBean bean;

    @Override
    public int getContentView() {
        return R.layout.activity_report_loss_detail;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str398), "");//报损详情
        if (bean != null) {
            //报损商品
            tvGoodName.setText(bean.getName());
            //报损数量
            tvNum.setText(bean.getNums());
            //报损金额
            if (!TextUtils.isEmpty(bean.getCost()) && !TextUtils.isEmpty(bean.getNums()))
                tvLossMoney.setText("￥" + new DecimalFormat("######0.00").format(Double.parseDouble(bean.getCost()) * Integer.parseInt(bean.getNums())));
            //报损时间
            if (!TextUtils.isEmpty(bean.getAddtime())) {
                tvTime.setText(TimeUtils.millis2String(Long.parseLong(bean.getAddtime()) * 1000));
            }
            //问题藐视
            tvLossReason.setText(bean.getDesc());
        }
    }


}
