package com.ui.ks.ReportLoss.presenter;

import android.text.TextUtils;

import com.MyApplication.KsApplication;
import com.bean.GoodInfoRespone;
import com.bean.ResultResponse;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.library.base.mvp.BasePresenter;
import com.library.utils.MessageEventUtil;
import com.ui.ks.R;
import com.ui.ks.ReportLoss.ReportLossGoodActivity;
import com.ui.ks.ReportLoss.contract.ReportLossGoodContract;
import com.ui.ks.ReportLoss.model.ReportLossGoodModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by lyf on 2020/7/22.
 */

public class ReportLossGoodPresenter extends BasePresenter<ReportLossGoodActivity> implements ReportLossGoodContract.Presenter {

    private ReportLossGoodModel mModel;

    public ReportLossGoodPresenter(ReportLossGoodActivity mView) {
        super(mView);
        mModel = new ReportLossGoodModel();
    }

    /**
     * @Description:添加报损
     * @Author:lyf
     * @Date: 2020/7/23
     */
    @Override
    public void addReportLossGoods(GoodInfoRespone.ResponseBean.DataBean.GoodsInfoBean bean) {
        if (bean == null) {
            return;
        }
        String nums = mView.getLossNums();
        String desc = mView.getProblemDescription();
        //报损数量小于等于0
        if (TextUtils.isEmpty(nums) && Integer.parseInt(nums) <= 0) {
            ToastUtils.showShort(mView.getResources().getString(R.string.str392));
            return;
        }
        //报损原因为空
        if (TextUtils.isEmpty(desc)) {
            ToastUtils.showShort(mView.getResources().getString(R.string.str395));
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", bean.getGoods_id());
        map.put("nums", nums);
        map.put("store", bean.getStore());
        map.put("desc", desc);
        map.put("work_id", KsApplication.getInt("seller_id", 0) + "");
        addSubscription(mModel.addReportLossGoods(new Gson().toJson(map)), new Subscriber<ResultResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultResponse response) {
                if ("200".equals(response.getResponse().getStatus())) {
                    //刷新报损列表
                    EventBus.getDefault().post(MessageEventUtil.getStringMap("lossListRefresh", ""));
                    mView.showToast(response.getResponse().getData().toString());
                    mView.finish();
                }
            }
        });
    }
}
