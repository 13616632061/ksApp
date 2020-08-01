package com.ui.ks.ReportLoss.model;

import com.api.ApiRetrofit;
import com.ui.ks.ReportLoss.contract.ReportLossListContract;

import rx.Observable;

/**
 * Created by lyf on 2020/7/19.
 */

public class ReportLossModel implements ReportLossListContract.Model {

    /**
    *@Description:报损列表
    *@Author:lyf
    *@Date: 2020/7/19
    */
    @Override
    public Observable getReportLossList() {
        return ApiRetrofit.getInstance().getApiService().getReportLossList();
    }
}
