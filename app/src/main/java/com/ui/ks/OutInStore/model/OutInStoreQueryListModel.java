package com.ui.ks.OutInStore.model;

import com.api.ApiRetrofit;
import com.ui.ks.OutInStore.contract.OutInStoreQueryListContract;

import rx.Observable;

/**
 * Created by lyf on 2020/9/12.
 */

public class OutInStoreQueryListModel implements OutInStoreQueryListContract.Model {

    /**
     * 查询出入库列表数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     * @return
     */
    @Override
    public Observable queryOutInStoreListData(String startTime, String endTime, int page) {
        return ApiRetrofit.getInstance().getApiService().queryOutInStoreListData(startTime, endTime, page + "");
    }
}
