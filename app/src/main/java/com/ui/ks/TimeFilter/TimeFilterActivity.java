package com.ui.ks.TimeFilter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.OrderSearchActivity;
import com.ui.ks.R;
import com.ui.ks.TimeFilter.contract.TimeFilterContract;
import com.ui.util.DateUtils;
import com.ui.util.SysUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description:时间筛选
 * @Author:lyf
 * @Date: 2020/8/15
 */
@Route(path = RouterPath.ACTIVITY_TIME_FILTER)
public class TimeFilterActivity extends BaseActivity implements TimeFilterContract.View {


    @BindView(R.id.et_start_date)
    TextView etStartDate;
    @BindView(R.id.et_start_time)
    TextView etStartTime;
    @BindView(R.id.et_end_date)
    TextView etEndDate;
    @BindView(R.id.et_end_time)
    TextView etEndTime;

    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);//设置为中国时间
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");//设置时间格式
    private Date mDate = new Date(System.currentTimeMillis());//获取当前系统时间

    private int curL = 0;
    private int curD = 0;


    @Override
    public int getContentView() {
        return R.layout.activity_time_filter;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str425), "");//时间筛选
        initDateTime();
    }

    @OnClick({R.id.btn_nearly_three_days, R.id.btn_nearly_seven_days, R.id.btn_this_month, R.id.btn_last_month,
            R.id.btn_last_day, R.id.btn_following_day, R.id.btn_last_none_month, R.id.btn_following_none_month, R.id.btn_reset, R.id.btn_sure,
            R.id.et_start_date, R.id.et_start_time, R.id.et_end_date, R.id.et_end_time})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.et_start_date://开始日期
                showCalendarDate(etStartDate);
                break;
            case R.id.et_start_time://开始时间
                showCalendarTime(etStartTime);
                break;
            case R.id.et_end_date://结束日期
                showCalendarDate(etEndDate);
                break;
            case R.id.et_end_time://结束时间
                showCalendarTime(etEndTime);
                break;
            case R.id.btn_nearly_three_days://近三天
                setDate(mDateFormat.format(mDate), mTimeFormat.format(mDate), 3, 3);
                break;
            case R.id.btn_nearly_seven_days://近七天
                setDate(mDateFormat.format(mDate), mTimeFormat.format(mDate), 7, 7);
                break;
            case R.id.btn_this_month://本月
                setMonth(0, 0);
                break;
            case R.id.btn_last_month://上月
                setMonth(-1, -1);
                break;
            case R.id.btn_last_day://上一天
                setDate(getStartDate(), "23:59", 1, 1);
                break;
            case R.id.btn_following_day://下一天
                setDate(getStartDate(), "23:59", -1, 1);
                break;
            case R.id.btn_last_none_month://上一月
                setMonth(curL - 1, curD - 1);
                break;
            case R.id.btn_following_none_month://下一月
                setMonth(curL + 1, curD + 1);
                break;
            case R.id.btn_reset://重置
                initDateTime();
                break;
            case R.id.btn_sure://确定
                Intent intent = new Intent();
                intent.putExtra("startTime", getStartDate() + " " + getStartTime());
                intent.putExtra("endTime", getEndDate() + " " + getEndTime());
                setResult(200,intent);
                finish();
                break;

        }
    }

    /**
     * @Description:初始化日期时间
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void initDateTime() {
        setStartDate(mDateFormat.format(mDate));
        setEndDate(mDateFormat.format(mDate));
        setStartTime("00:00");
        setEndTime(mTimeFormat.format(mDate));
    }

    /**
     * @Description:设置某一天
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void setDate(String startDate, String endTime, long L, long d) {
        setStartDate(DateUtils.getNearlyDate(startDate, L).substring(0, 10));
        setEndDate(DateUtils.getNearlyDate(startDate, L - d).substring(0, 10));
        setStartTime("00:00");
        setEndTime(endTime);
    }

    /**
     * @Description:设置月份
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void setMonth(int L, int d) {
        curL = L;
        curD = d;
        setStartDate(DateUtils.getSupportBeginDayofMonth(DateUtils.strToDate(DateUtils.getMonthDate(L))));
        setEndDate(DateUtils.getSupportEndDayofMonth(DateUtils.strToDate(DateUtils.getMonthDate(d))));
        setStartTime("00:00");
        setEndTime("23:59");
    }

    /**
     * @Description:开始日期
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void setStartDate(String date) {
        etStartDate.setText(date);
    }

    @Override
    public String getStartDate() {
        return etStartDate.getText().toString().trim();
    }

    /**
     * @Description:开始时间
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void setStartTime(String time) {
        etStartTime.setText(time);

    }

    @Override
    public String getStartTime() {
        return etStartTime.getText().toString().trim();
    }

    /**
     * @Description:结束日期
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void setEndDate(String date) {
        etEndDate.setText(date);

    }

    @Override
    public String getEndDate() {
        return etEndDate.getText().toString().trim();
    }

    /**
     * @Description:结束时间
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void setEndTime(String time) {
        etEndTime.setText(time);

    }

    @Override
    public String getEndTime() {
        return etEndTime.getText().toString().trim();
    }

    /**
     * @Description:显示日期
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void showCalendarDate(TextView textView) {
        final String CurDate = DateUtils.getCurDate().substring(0, 10);
        DatePickerDialog.OnDateSetListener data = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (DateUtils.getDateSpan(CurDate, mDateFormat.format(mCalendar.getTime()), 1) > 0) {
                    textView.setText(CurDate);
                    //开始日期超过当前日期,自动调整为当前日期
                    SysUtils.showError(getString(R.string.str334));
                } else {
                    textView.setText(mDateFormat.format(mCalendar.getTime()));
                }

            }
        };
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(this, data, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }

    /**
     * @Description:显示时间
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public void showCalendarTime(TextView textView) {
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                textView.setText(mTimeFormat.format(mCalendar.getTime()));
            }
        };
        TimePickerDialog mTimePickerDialog = new TimePickerDialog(this, time,
                mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
        mTimePickerDialog.show();
    }
}
