package com.ui.fragment.contract;

import android.view.View;

import com.ui.adapter.GoodsSalesStatisticsAdapter;

import retrofit2.http.Field;
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

        /**
         * @Description:设置筛选时间
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void setTvTime(String time);

        /**
         * @Description:跳转时间筛选
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void goToTimeFilter();

        /**
         * @Description:初始化日期时间
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void initDateTime();
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
         * @Description:空视图
         * @Author:lyf
         * @Date: 2020/8/11
         */
        void setEmptyView();
        /**
        *@Description:筛选时间商品销售统计
        *@Author:lyf
        *@Date: 2020/9/24
        */
        void goodsStatisticFilter(String beginTime, String endTime);
    }

    interface Model {
        /**
         * @Description:商品销售统计
         * @Author:lyf
         * @Date: 2020/8/2
         */
        Observable goodsSalesStatistics();

        /**
         * @Description:筛选时间商品销售统计
         * @Author:lyf
         * @Date: 2020/9/24
         */
        Observable goodsStatisticFilter(String beginTime, String endTime);
    }
}
