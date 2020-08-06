package com.ui.fragment.model;

import com.api.ApiRetrofit;
import com.ui.fragment.contract.SalesStatisticsFragmentContract;
import com.ui.util.DateUtils;

import rx.Observable;

/**
 * Created by lyf on 2020/8/5.
 */

public class SalesStatisticsFragmentModel implements SalesStatisticsFragmentContract.Model {
    /**
     * @Description:当月现金统计
     * @Author:lyf
     * @Date: 2020/8/5
     */
    @Override
    public Observable curMonthcashStatistics() {
        return ApiRetrofit.getInstance().getApiService().curMonthcashStatistics(DateUtils.getMonthDate(0),"1","31");
    }
}
