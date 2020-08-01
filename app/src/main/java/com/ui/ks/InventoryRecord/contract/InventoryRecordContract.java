package com.ui.ks.InventoryRecord.contract;

import com.ui.ks.InventoryRecord.adapter.InventoryRecordAdapter;

import rx.Observable;

/**
 * Created by lyf on 2020/7/8.
 */

public interface InventoryRecordContract {

    interface View {
        //初始化适配器
        InventoryRecordAdapter initAdapter();

        //发送邮箱
        void toGoSendEmail(String Batch);
    }

    interface Presenter {
        //初始化适配器
        void initAdapter();

        //盘点记录
        void getInventoryRecord();

    }

    interface Model {
        //盘点记录
        Observable getInventoryRecord();
    }
}
