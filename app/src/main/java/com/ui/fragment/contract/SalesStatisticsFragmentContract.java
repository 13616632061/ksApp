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
         * @Description:当月现金统计图表
         * @Author:lyf
         * @Date: 2020/8/5
         */
        void curMonthcashStatisticsChart(List<ChartEntity> chartEntities);

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
        void curMonthcashStatistics();
    }

    interface Model {
        /**
         * @Description:当月现金统计
         * @Author:lyf
         * @Date: 2020/8/5
         */
        Observable curMonthcashStatistics();
    }
}
