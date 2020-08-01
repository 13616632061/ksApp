package com.ui.ks.InventoryRecord.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bean.InventoryRecordRespone;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ui.ks.R;

import java.util.List;

/**
 * Created by lyf on 2020/7/11.
 */

public class InventoryRecordAdapter extends BaseQuickAdapter<InventoryRecordRespone.ResponseBean.DataBean.ListBean, BaseViewHolder> {


    public InventoryRecordAdapter(int layoutResId, @Nullable List<InventoryRecordRespone.ResponseBean.DataBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryRecordRespone.ResponseBean.DataBean.ListBean item) {
        //序号
        helper.setText(R.id.tv_num, (helper.getAdapterPosition() + 1) + "");
        //盘点时间
        if (!TextUtils.isEmpty(item.getAddtime())) {
            helper.setText(R.id.tv_time, TimeUtils.millis2String(Long.parseLong(item.getAddtime())*1000));
        }
        //发送邮箱
        helper.addOnClickListener(R.id.tv_send_email);
    }
}
