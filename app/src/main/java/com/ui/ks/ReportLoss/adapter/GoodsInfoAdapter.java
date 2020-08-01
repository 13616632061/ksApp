package com.ui.ks.ReportLoss.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bean.GoodInfoRespone;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.app.LibAplication;
import com.ui.ks.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by lyf on 2020/7/19.
 */

public class GoodsInfoAdapter extends BaseQuickAdapter<GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean, BaseViewHolder> {

    public GoodsInfoAdapter(int layoutResId, @Nullable List<GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean item) {
        //商品图片
        ImageView ivGoodPicture = helper.getView(R.id.iv_goods);
        Glide.with(LibAplication.getContext()).load(item.getImg_src()).placeholder(R.drawable.product).into(ivGoodPicture);
        //商品名字
        helper.setText(R.id.tv_good_name, item.getName());
        //商品价格
        if (!TextUtils.isEmpty(item.getPrice())) {
            helper.setText(R.id.tv_good_price, "￥" + new DecimalFormat("######0.00").format(Double.parseDouble(item.getPrice())));
        }

        helper.addOnClickListener(R.id.tv_add_loss);//添加破损
    }
}
