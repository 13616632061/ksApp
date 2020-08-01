package com.ui.ks.InventoryRecord.model;

import com.MyApplication.KsApplication;
import com.api.ApiRetrofit;
import com.ui.ks.InventoryRecord.contract.InventoryRecordContract;

import rx.Observable;

/**
 * Created by lyf on 2020/7/8.
 */

public class InventoryRecordModel implements InventoryRecordContract.Model {

    /**
     * @Description:盘点记录
     * @Author:lyf
     * @Date: 2020/7/8
     */
    @Override
    public Observable getInventoryRecord() {
        return ApiRetrofit.getInstance().getApiService().getInventoryRecord();
    }
}
