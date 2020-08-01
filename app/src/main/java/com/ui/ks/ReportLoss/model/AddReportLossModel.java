package com.ui.ks.ReportLoss.model;

import com.api.ApiRetrofit;
import com.ui.ks.ReportLoss.contract.AddReportLossContract;

import rx.Observable;

/**
 * Created by lyf on 2020/7/19.
 */

public class AddReportLossModel implements AddReportLossContract.Model {
    /**
     * @Description:搜索报损商品
     * @Author:lyf
     * @Date: 2020/7/19
     */
    @Override
    public Observable searchReportLossGoods(String str) {
        return ApiRetrofit.getInstance().getApiService().searchGoods(str, 1 + "");
    }
}
