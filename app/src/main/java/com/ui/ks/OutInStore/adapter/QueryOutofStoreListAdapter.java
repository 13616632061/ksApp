package com.ui.ks.OutInStore.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.app.LibAplication;
import com.ui.entity.OutInStoreListResponse;
import com.ui.entity.outofstoragelist;
import com.ui.ks.R;
import com.ui.util.DateUtils;

import java.util.List;

/**
 * 今日所有订单适配器
 * Created by Administrator on 2017/1/3.
 */

public class QueryOutofStoreListAdapter extends BaseQuickAdapter<OutInStoreListResponse.ResponseBean.DataBean.ListBean, BaseViewHolder> {


    public QueryOutofStoreListAdapter(int layoutResId, @Nullable List<OutInStoreListResponse.ResponseBean.DataBean.ListBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OutInStoreListResponse.ResponseBean.DataBean.ListBean item) {

        helper.setText(R.id.tv_order_id, item.getOrder_id() + "");
        helper.setText(R.id.tv_orderlist_time, DateUtils.getDateTimeFromMillisecondYMD(Long.parseLong(item.getCreatetime()) * 1000));
        helper.setText(R.id.tv_payprice, Double.parseDouble(item.getMoney()) + "");
        switch (item.getType()) {
            case "0"://入库
                helper.setText(R.id.tv_type, LibAplication.getContext().getResources().getString(R.string.str124));
                break;
            default://出库
                helper.setText(R.id.tv_type, LibAplication.getContext().getResources().getString(R.string.str123));
                break;
        }
    }


}

