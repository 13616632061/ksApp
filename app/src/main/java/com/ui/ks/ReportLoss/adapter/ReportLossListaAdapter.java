package com.ui.ks.ReportLoss.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bean.ReportLostListRespone;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ui.ks.R;

import java.util.List;

/**
 * Created by lyf on 2020/7/28.
 */

public class ReportLossListaAdapter extends BaseQuickAdapter<ReportLostListRespone.ResponseBean.DataBean, BaseViewHolder> {
    public ReportLossListaAdapter(int layoutResId, @Nullable List<ReportLostListRespone.ResponseBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportLostListRespone.ResponseBean.DataBean item) {
        //序号
        helper.setText(R.id.tv_num, (helper.getAdapterPosition() + 1) + "");
        //报损商品名字
        helper.setText(R.id.tv_good_name, item.getName());
        //报损时间
        if (!TextUtils.isEmpty(item.getAddtime())) {
            helper.setText(R.id.tv_time, TimeUtils.millis2String(Long.parseLong(item.getAddtime()) * 1000));
        }
    }
}
