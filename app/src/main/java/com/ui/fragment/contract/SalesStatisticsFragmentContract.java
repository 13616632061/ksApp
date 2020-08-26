package com.ui.fragment.contract;

import com.bean.SalesStatisticsRespone;
import com.library.weight.ChartEntity;

import java.util.List;

import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by lyf on 2020/8/5.
 */

public interface SalesStatisticsFragmentContract {

    interface View {
        /**
         * @Description:统计时间
         * @Author:lyf
         * @Date: 2020/8/8
         */
        void setTvDate();

        /**
         * @Description:当月现金统计图表
         * @Author:lyf
         * @Date: 2020/8/5
         */
        void curMonthcashStatisticsChart(List<ChartEntity> chartEntities);

        /**
         * @Description:当月移动统计图表
         * @Author:lyf
         * @Date: 2020/8/8
         */
        void curMonthLineStatisticsChart(List<ChartEntity> chartEntities);

        /**
         * @Description:当月会员统计图表
         * @Author:lyf
         * @Date: 2020/8/8
         */
        void curMonthMemberStatistics(List<ChartEntity> chartEntities);

    }

    interface Presenter {
        /**
         * @Description:设置图表数据
         * @Author:lyf
         * @Date: 2020/8/5
         */
        void setChartEntityData(SalesStatisticsRespone.ResponseBean.DataBean response, List<ChartEntity> chartEntities);

        /**
         * @Description:当月现金统计
         * @Author:lyf
         * @Date: 2020/8/5
         */
        void curMonthcashStatistics(int month);

        /**
         * @Description:当月移动统计
         * @Author:lyf
         * @Date: 2020/8/8
         */
        void curMonthLineStatistics(int month);

        /**
         * @Description:当月会员统计
         * @Author:lyf
         * @Date: 2020/8/8
         */
        void curMonthMemberStatistics(int month);
    }

    interface Model {
        /**
         * @Description:当月现金统计
         * @Author:lyf
         * @Date: 2020/8/5
         */
        Observable curMonthcashStatistics(int month);

        /**
         * @Description:当月移动统计
         * @Author:lyf
         * @Date: 2020/8/8
         */
        Observable curMonthLineStatistics(int month);

        /**
         * @Description:当月会员统计
         * @Author:lyf
         * @Date: 2020/8/8
         */
        Observable curMonthMemberStatistics(int month);
    }
}
