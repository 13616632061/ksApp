package com.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ui.entity.GoodsSalesStatisticsRespone;
import com.ui.ks.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @Description:商品销售统计
 * @Author:lyf
 * @Date: 2020/8/2
 */

public class GoodsSalesStatisticsAdapter extends BaseQuickAdapter<GoodsSalesStatisticsRespone.ResponseBean.DataBean, BaseViewHolder> {


    public GoodsSalesStatisticsAdapter(int layoutResId, @Nullable List<GoodsSalesStatisticsRespone.ResponseBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsSalesStatisticsRespone.ResponseBean.DataBean item) {
        //排序号
        helper.setText(R.id.tv_num, (helper.getAdapterPosition() + 1) + "");
        //商品名字
        helper.setText(R.id.tv_good_name, item.getGoods_name());
        //商品销售额
        if (!TextUtils.isEmpty(item.getSell_price())) {
            helper.setText(R.id.tv_money, "￥" + new DecimalFormat("######0.00").format(Double.parseDouble(item.getPrice())));
        } else {
            helper.setText(R.id.tv_money, "￥0.00");

        }
        //商品销售数量
        helper.setText(R.id.tv_sales_num, item.getNums());
    }
}
