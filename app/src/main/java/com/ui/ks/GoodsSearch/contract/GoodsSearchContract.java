package com.ui.ks.GoodsSearch.contract;

import android.view.View;

import com.ui.ks.GoodsSearch.adapter.GoodsInfoSearchAdapter;
import com.ui.ks.ReportLoss.adapter.GoodsInfoAdapter;

/**
 * Created by lyf on 2020/8/21.
 */

public interface GoodsSearchContract {
    interface View {
        //搜索内容
        String getSearchContent();

        //初始化适配器
        GoodsInfoSearchAdapter initAdapter();

        //跳转到商品详情
        void goToGoodsDetail(String goodsId);

        //空视图
        android.view.View setEmptyView();

        //初始化扫码
        void initScan();


    }

    interface Presenter {
        //初始化适配器
        void initAdapter();

        //搜索商品
        void searchGoodsInfo(String result);

        //空视图
        void setEmptyView();
    }
}
