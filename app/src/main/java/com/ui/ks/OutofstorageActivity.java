package com.ui.ks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.BaseActivity;
import com.constant.RouterPath;
import com.material.widget.PaperButton;
import com.ui.ks.OutInStore.OutInStoreQueryListActivity;
import com.ui.util.DateUtils;
import com.ui.util.SysUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 出入库报表查询
 * Created by admin on 2018/5/30.
 */

public class OutofstorageActivity extends BaseActivity implements View.OnClickListener {


    private String date_begin;
    private String date_end;

    Calendar mCalendar = Calendar.getInstance(Locale.CHINA);//设置为中国时间
    DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");//设置时间格式
    Date mDate = new Date(System.currentTimeMillis());//获取当前系统时间
    EditText et_orderstart_date, et_orderstart_time, et_orderend_date, et_orderend_time;
    private PaperButton btn_ordersearch_save;
    private Button btn_ordersearch_reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outofstorage_activity);

        initToolbar(this);

        initView();

    }

    private void initView() {
        et_orderstart_date = (EditText) findViewById(R.id.et_orderstart_date);
        et_orderstart_time = (EditText) findViewById(R.id.et_orderstart_time);
        et_orderend_date = (EditText) findViewById(R.id.et_orderend_date);
        et_orderend_time = (EditText) findViewById(R.id.et_orderend_time);

        btn_ordersearch_save = (PaperButton) findViewById(R.id.btn_ordersearch_save);
        btn_ordersearch_reset = (Button) findViewById(R.id.btn_ordersearch_reset);


        et_orderstart_date.setOnClickListener(this);
        et_orderstart_time.setOnClickListener(this);
        et_orderend_date.setOnClickListener(this);
        et_orderend_time.setOnClickListener(this);
        btn_ordersearch_save.setOnClickListener(this);
        btn_ordersearch_reset.setOnClickListener(this);

        //设置输入框不弹出监听及手动输入
        et_orderstart_date.setInputType(InputType.TYPE_NULL);
        et_orderstart_date.setFocusable(false);
        et_orderstart_time.setInputType(InputType.TYPE_NULL);
        et_orderstart_time.setFocusable(false);
        et_orderend_date.setInputType(InputType.TYPE_NULL);
        et_orderend_date.setFocusable(false);
        et_orderend_time.setInputType(InputType.TYPE_NULL);
        et_orderend_time.setFocusable(false);


        et_orderstart_date.setText(mDateFormat.format(mDate));
        et_orderend_date.setText(mDateFormat.format(mDate));
        et_orderstart_time.setText("00:00");
        et_orderend_time.setText(mTimeFormat.format(mDate));

    }

    //获取时间 yyyy-MM-dd  HH:mm
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public String getTime1() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }


    //点击事件
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.et_orderstart_date:
                showCalendarDate(et_orderstart_date);
                break;
            case R.id.et_orderstart_time:
                showCalendarTime(et_orderstart_time);
                break;
            case R.id.et_orderend_date:
                showCalendarDate(et_orderend_date);
                break;
            case R.id.et_orderend_time:
                showCalendarTime(et_orderend_time);
                break;
            case R.id.btn_ordersearch_save://保存
                toGoOutInStoreQueryPage();
                break;
            case R.id.btn_ordersearch_reset://重置
                et_orderstart_date.setText(mDateFormat.format(mDate));
                et_orderend_date.setText(mDateFormat.format(mDate));
                et_orderstart_time.setText("00:00");
                et_orderend_time.setText(mTimeFormat.format(mDate));
                break;
        }
    }

    /**
     * @Description:跳转出入库查询列表页
     * @Author:lyf
     * @Date: 2020/9/12
     */
    private void toGoOutInStoreQueryPage() {
        date_begin = et_orderstart_date.getText().toString().trim() + " " + et_orderstart_time.getText().toString().trim();
        date_end = et_orderend_date.getText().toString().trim() + " " + et_orderend_time.getText().toString().trim();
        ARouter.getInstance().build(RouterPath.ACTIVITY_OUT_IN_SOTRE_QUERY)
                .withString("date_begin", date_begin)
                .withString("date_end", date_end)
                .navigation();
    }

    /**
     * 显示时间
     *
     * @param editText
     */
    private void showCalendarTime(final EditText editText) {
        SysUtils.hideSoftKeyboard(OutofstorageActivity.this);
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                editText.setText(mTimeFormat.format(mCalendar.getTime()));
            }
        };
        TimePickerDialog mTimePickerDialog = new TimePickerDialog(OutofstorageActivity.this, time,
                mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
        mTimePickerDialog.show();
    }

    /**
     * 显示日期
     *
     * @param editText
     */
    private void showCalendarDate(final EditText editText) {
        SysUtils.hideSoftKeyboard(OutofstorageActivity.this);
        final String CurDate = DateUtils.getCurDate().substring(0, 10);
        DatePickerDialog.OnDateSetListener data = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (DateUtils.getDateSpan(CurDate, mDateFormat.format(mCalendar.getTime()), 1) > 0) {
                    editText.setText(CurDate);
                    SysUtils.showError(getString(R.string.str334));
                } else {
                    editText.setText(mDateFormat.format(mCalendar.getTime()));
                }

            }
        };
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(OutofstorageActivity.this, data, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }

}
