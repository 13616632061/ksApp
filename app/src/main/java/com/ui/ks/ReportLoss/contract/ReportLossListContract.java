package com.ui.ks.ReportLoss.contract;

import com.bean.ReportLostListRespone;
import com.ui.ks.ReportLoss.adapter.ReportLossListaAdapter;

import rx.Observable;

/**
 * Created by lyf on 2020/7/8.
 */

public interface ReportLossListContract {

    interface View {
        //初始化适配器
        ReportLossListaAdapter initAdapter();

        //添加报损
        void toAddReportLoss();

        //跳转报损详情页
        void toGoResportLossDetail(ReportLostListRespone.ResponseBean.DataBean bean);
    }

    interface Presenter {
        //初始化适配器
        void initAdapter();

        //报损列表
        void getReportLossList();

    }

    interface Model {
        //报损列表
        Observable getReportLossList();
    }
}
