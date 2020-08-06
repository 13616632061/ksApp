package com.ui.fragment.contract;

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
        *@Description:销售额升序
        *@Author:lyf
        *@Date: 2020/8/2
        */
        void setSaleMoneyUpSort();
        /**
        *@Description:销售额降序
        *@Author:lyf
        *@Date: 2020/8/2
        */
        void setSaleMoneyDownSort();
        /**
        *@Description:销售数量升序
        *@Author:lyf
        *@Date: 2020/8/2
        */
        void setSaleNumsUpSort();
        /**
        *@Description:销售数量降序
        *@Author:lyf
        *@Date: 2020/8/2
        */
        void setSaleNumsDownSort();
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
