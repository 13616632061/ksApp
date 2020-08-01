package com.ui.ks.ReportLoss.model;

import com.MyApplication.KsApplication;
import com.api.ApiRetrofit;
import com.ui.ks.ReportLoss.contract.ReportLossGoodContract;

import rx.Observable;

/**
 * Created by lyf on 2020/7/23.
 */

public class ReportLossGoodModel implements ReportLossGoodContract.Model {

    /**
     * @Description:添加报损
     * @Author:lyf
     * @Date: 2020/7/23
     */
    @Override
    public Observable addReportLossGoods(String map) {
        return ApiRetrofit.getInstance().getApiService().addReportLossGoods(map);
    }
}
