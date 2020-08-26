package com.ui.ks.TimeFilter.contract;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lyf on 2020/8/15.
 */

public interface TimeFilterContract {

    interface View {
        //初始化日期时间
        void initDateTime();

        //设置某一天
        void setDate(String startDate, String endTime, long L, long d);

        //设置月份
        void setMonth(int L, int d);

        //开始日期
        void setStartDate(String date);

        String getStartDate();

        //开始时间
        void setStartTime(String time);

        String getStartTime();

        //结束日期
        void setEndDate(String date);

        String getEndDate();

        //结束时间
        void setEndTime(String time);

        String getEndTime();

        //显示日期
        void showCalendarDate(TextView textView);

        //显示时间
        void showCalendarTime(TextView editText);
    }
}
