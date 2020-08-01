package com.ui.ks.InventoryRecord.prestener;

import com.api.SubscriberCallBack;
import com.bean.InventoryRecordRespone;
import com.bean.ResultResponse;
import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BasePresenter;
import com.ui.ks.InventoryRecord.InventoryRecordActivity;
import com.ui.ks.InventoryRecord.adapter.InventoryRecordAdapter;
import com.ui.ks.InventoryRecord.contract.InventoryRecordContract;
import com.ui.ks.InventoryRecord.model.InventoryRecordModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/7/8.
 */

public class InventoryRecordPrestener extends BasePresenter<InventoryRecordActivity> implements InventoryRecordContract.Presenter {
    private static final String TAG = InventoryRecordPrestener.class.getSimpleName();
    private InventoryRecordModel mModel;

    private InventoryRecordAdapter mAdapter;
    private List<InventoryRecordRespone.ResponseBean.DataBean.ListBean> mData = new ArrayList<>();

    public InventoryRecordPrestener(InventoryRecordActivity mView) {
        super(mView);
        mModel = new InventoryRecordModel();
    }


    /**
     * @Description:盘点记录数据
     * @Author:lyf
     * @Date: 2020/7/11
     */
    public List<InventoryRecordRespone.ResponseBean.DataBean.ListBean> getmData() {
        return mData;
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/7/11
     */
    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:获取盘点记录数据
     * @Author:lyf
     * @Date: 2020/7/11
     */
    @Override
    public void getInventoryRecord() {
        addSubscription(mModel.getInventoryRecord(), new Subscriber<InventoryRecordRespone>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(InventoryRecordRespone respone) {
                LogUtils.d(TAG, respone.toString());
                if ("200".equals(respone.getResponse().getStatus())){
                    mData.clear();
                    mData.addAll(respone.getResponse().getData().getList());
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }
}
