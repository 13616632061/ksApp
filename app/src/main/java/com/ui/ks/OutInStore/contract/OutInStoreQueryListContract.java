package com.ui.ks.OutInStore.contract;

import com.ui.entity.OutInStoreListResponse;
import com.ui.ks.OutInStore.adapter.QueryOutofStoreListAdapter;

import rx.Observable;

/**
 * Created by lyf on 2020/9/12.
 */

public interface OutInStoreQueryListContract {

    interface View {
        /**
         * @Description:初始化适配器
         * @Author:lyf
         * @Date: 2020/9/12
         */
        QueryOutofStoreListAdapter initAdapter();

        /**
         * @Description:跳转出入库详情
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void goToOutInStoreDetail(int position);

        /**
         * @Description:下拉刷新
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void OnRefresh();

        /**
         * @Description:下拉刷新状态
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void setRefreshEnable(boolean enable);

        /**
         * @Description:总数量
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void setOrderTotalNum(String totalNum);

    }

    interface Presnter {
        /**
         * @Description:初始化适配器
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void initAdapter();

        /**
         * @Description:查询出入库列表数据
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void queryOutInStoreListData(String startTime, String endTime);

        /**
         * @Description:下拉刷新
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void OnRefresh();

        /**
         * @Description:上拉加载
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void onLoadMoreRequested();

        /**
         * @Description:上拉加载数据处理
         * @Author:lyf
         * @Date: 2020/9/12
         */
        void handleLoadMore(OutInStoreListResponse response);

    }

    interface Model {
        /**
         * @Description:查询出入库列表数据
         * @Author:lyf
         * @Date: 2020/9/12
         */
        Observable queryOutInStoreListData(String startTime, String endTime, int page);
    }
}
