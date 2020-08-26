package com.ui.fragment.contract;

import android.view.View;

import com.ui.adapter.GoodsSalesStatisticsAdapter;

import rx.Observable;

/**
 * @Description:商品销售统计
 * @Author:lyf
 * @Date: 2020/8/2
 */

public interface GoodsSalesStatisticsFragmentContract {

    interface View {
        /**
         * @Description:初始化适配器
         * @Author:lyf
         * @Date: 2020/8/2
         */
        GoodsSalesStatisticsAdapter initAdapter();

        /**
         * @Description:空视图
         * @Author:lyf
         * @Date: 2020/8/11
         */
        android.view.View setEmptyView();
    }

    interface Presenter {
        /**
         * @Description:初始化适配器
         * @Author:lyf
         * @Date: 2020/8/2
         */
        void initAdapter();

        /**
         * @Description:商品销售统计
         * @Author:lyf
         * @Date: 2020/8/2
         */
        void goodsSalesStatistics();

        /**
         * @Description:销售额升序
         * @Author:lyf
         * @Date: 2020/8/2
         */
        void setSaleMoneyUpSort();

        /**
         * @Description:销售额降序
         * @Author:lyf
         * @Date: 2020/8/2
         */
        void setSaleMoneyDownSort();

        /**
         * @Description:销售数量升序
         * @Author:lyf
         * @Date: 2020/8/2
         */
        void setSaleNumsUpSort();

        /**
         * @Description:销售数量降序
         * @Author:lyf
         * @Date: 2020/8/2
         */
        void setSaleNumsDownSort();
        /**
        *@Description:空视图
        *@Author:lyf
        *@Date: 2020/8/11
        */
        void setEmptyView();
    }

    interface Model {
        /**
         * @Description:商品销售统计
         * @Author:lyf
         * @Date: 2020/8/2
         */
        Observable goodsSalesStatistics();
    }
}
