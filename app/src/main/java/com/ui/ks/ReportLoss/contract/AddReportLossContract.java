package com.ui.ks.ReportLoss.contract;

import com.bean.GoodInfoRespone;
import com.ui.ks.ReportLoss.adapter.GoodsInfoAdapter;

import rx.Observable;

/**
 * Created by lyf on 2020/7/8.
 */

public interface AddReportLossContract {

    interface View {
        //初始化扫码
        void initScan();

        //初始化适配器
        GoodsInfoAdapter initAdapter();

        //跳转破损商品页
        void toGoReportLossGoodPage(GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean bean);
    }

    interface Presenter {
        //初始化适配器
        void initAdapter();

        //搜索报损商品
        void searchReportLossGoods(String str);

    }

    interface Model {
        //搜索报损商品
        Observable searchReportLossGoods(String str);
    }
}
