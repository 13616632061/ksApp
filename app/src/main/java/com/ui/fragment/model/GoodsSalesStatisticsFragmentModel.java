package com.ui.fragment.model;

import com.api.ApiRetrofit;
import com.ui.fragment.contract.GoodsSalesStatisticsFragmentContract;

import rx.Observable;

/**
 * Created by lyf on 2020/8/2.
 */

public class GoodsSalesStatisticsFragmentModel implements GoodsSalesStatisticsFragmentContract.Model {

    /**
     * @Description:商品销售统计
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @Override
    public Observable goodsSalesStatistics() {
        return ApiRetrofit.getInstance().getApiService().getCountGoods();
    }
}
