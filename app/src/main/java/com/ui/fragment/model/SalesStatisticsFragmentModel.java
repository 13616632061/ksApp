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
    public Observable curMonthcashStatistics(int month) {
        return ApiRetrofit.getInstance().getApiService().curMonthcashStatistics(DateUtils.getMonthDate(month),"1","31");
    }
    /**
    *@Description:当月移动统计
    *@Author:lyf
    *@Date: 2020/8/8
    */
    @Override
    public Observable curMonthLineStatistics(int month) {
        return ApiRetrofit.getInstance().getApiService().curMonthLineStatistics(DateUtils.getMonthDate(month),"1","31");
    }
    /**
    *@Description:当月会员统计
    *@Author:lyf
    *@Date: 2020/8/8
    */
    @Override
    public Observable curMonthMemberStatistics(int month) {
        return ApiRetrofit.getInstance().getApiService().curMonthMemberStatistics(DateUtils.getMonthDate(month),"1","31","member");
    }
}
