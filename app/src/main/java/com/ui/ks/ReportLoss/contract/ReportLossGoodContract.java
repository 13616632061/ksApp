package com.ui.ks.ReportLoss.contract;

import com.bean.GoodInfoRespone;

import rx.Observable;

/**
 * Created by lyf on 2020/7/22.
 */

public interface ReportLossGoodContract {


    interface View {
        //设置事件监听
        void setListener();

        //设置商品名字
        void setGoodName();

        //获取商品名字
        String getGoodName();

        //报损商品申请数量
        String getLossNums();

        //报损商品申请金额
        void setLossMoney(double num);

        //报损商品申请问题描述
        String getProblemDescription();
    }

    interface Presenter {
        //提交添加报损
        void addReportLossGoods(GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean bean);
    }

    interface Model {
        //添加报损
        Observable addReportLossGoods(String map);
    }
}
