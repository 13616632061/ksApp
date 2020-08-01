package com.ui.ks;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.BitmapUtils;
import com.ui.util.DateUtils;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 订单搜索页面
 */

public class OrderSearchActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_orderstart_date,et_orderstart_time,et_orderend_date,et_orderend_time;
    private EditText et_order_money,et_order_num;
    private Button btn_ordersearch_weixin,btn_ordersearch_zhifubao,btn_ordersearch_qqwallet,btn_pay_scancode,btn_pay_cash,btn_pay_member;
    private Button btn_ordersearch_reset;
    private PaperButton btn_ordersearch_save;
    Calendar mCalendar=Calendar.getInstance(Locale.CHINA);//设置为中国时间
    DateFormat mDateFormat=new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    DateFormat mTimeFormat=new SimpleDateFormat("HH:mm");//设置时间格式
    Date mDate=new Date(System.currentTimeMillis());//获取当前系统时间
    private int isSelect_weixin_num=0;//微信收款方式点击求余，求余等于0位选中，默认0
    private int isSelect_zhifubao_num=0;//支付宝收款方式点击求余，求余等于0位选中，默认0
    private int isSelect_qqwallet_num=0;//qq钱包收款方式点击求余，求余等于0位选中，默认0
    private int isSelect_cash_num=0;//扫码收款方式点击求余，求余等于0位选中，默认0
    private int isSelect_scancode_num=0;//扫码收款方式点击求余，求余等于0位选中，默认0
    private int isSelect_member_num=0;//扫码收款方式点击求余，求余等于0位选中，默认0
    private String wx="";//微信收款方式
    private String alipay="";//支付宝收款方式
    private String qq="";//QQ钱包收款方式
    private String micro="";//扫描收款方式
    private String cash="";//现金收款方式
    private String member="";//
    private String date_begin;
    private String date_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_search);

        SysUtils.setupUI(this,findViewById( R.id.activity_order_search));
        initToolbar(this);

        initView();


    }
    //初始化视图
    private void initView() {
        et_orderstart_date= (EditText) findViewById(R.id.et_orderstart_date);
        et_orderstart_time= (EditText) findViewById(R.id.et_orderstart_time);
        et_orderend_date= (EditText) findViewById(R.id.et_orderend_date);
        et_orderend_time= (EditText) findViewById(R.id.et_orderend_time);
        et_order_money= (EditText) findViewById(R.id.et_order_money);
        et_order_num= (EditText) findViewById(R.id.et_order_num);
        btn_ordersearch_weixin= (Button) findViewById(R.id.btn_ordersearch_weixin);
        btn_ordersearch_zhifubao= (Button) findViewById(R.id.btn_ordersearch_zhifubao);
        btn_ordersearch_qqwallet= (Button) findViewById(R.id.btn_ordersearch_qqwallet);
        btn_pay_scancode= (Button) findViewById(R.id.btn_pay_scancode);
        btn_pay_cash= (Button) findViewById(R.id.btn_pay_cash);
        btn_ordersearch_reset= (Button) findViewById(R.id.btn_ordersearch_reset);
        btn_ordersearch_save= (PaperButton) findViewById(R.id.btn_ordersearch_save);
        btn_pay_member= (Button) findViewById(R.id.btn_pay_member);


        et_orderstart_date.setOnClickListener(this);
        et_orderstart_time.setOnClickListener(this);
        et_orderend_date.setOnClickListener(this);
        et_orderend_time.setOnClickListener(this);
        btn_ordersearch_weixin.setOnClickListener(this);
        btn_ordersearch_zhifubao.setOnClickListener(this);
        btn_ordersearch_qqwallet.setOnClickListener(this);
        btn_pay_scancode.setOnClickListener(this);
        btn_ordersearch_reset.setOnClickListener(this);
        btn_ordersearch_save.setOnClickListener(this);
        btn_pay_cash.setOnClickListener(this);
        btn_pay_member.setOnClickListener(this);


//设置输入框不弹出监听及手动输入
        et_orderstart_date.setInputType(InputType.TYPE_NULL);
        et_orderstart_date.setFocusable(false);
        et_orderstart_time.setInputType(InputType.TYPE_NULL);
        et_orderstart_time.setFocusable(false);
        et_orderend_date.setInputType(InputType.TYPE_NULL);
        et_orderend_date.setFocusable(false);
        et_orderend_time.setInputType(InputType.TYPE_NULL);
        et_orderend_time.setFocusable(false);
//          设置初始值
        initResetData();
//        设置输入金额限制
//        SetEditTextInput.setEtPoint(et_order_money);
        SetEditTextInput.judgeNumber(et_order_money);
    }

    /**
     * 初始化数据
     */
    private void initResetData() {
        et_orderstart_date.setText(mDateFormat.format(mDate));
        et_orderend_date.setText(mDateFormat.format(mDate));
        et_orderstart_time.setText("00:00");
        et_orderend_time.setText(mTimeFormat.format(mDate));
        et_order_money.setText("");
        et_order_num.setText("");
        isSelect_weixin_num=0;//微信收款方式点击求余，求余等于0位选中，默认0
        isSelect_zhifubao_num=0;//支付宝收款方式点击求余，求余等于0位选中，默认0
        isSelect_qqwallet_num=0;//qq钱包收款方式点击求余，求余等于0位选中，默认0
        isSelect_scancode_num=0;
        isSelect_cash_num=0;
//        btn_ordersearch_weixin.setBackgroundResource(R.drawable.ordersearch_payway_btn);
//        btn_ordersearch_zhifubao.setBackgroundResource(R.drawable.ordersearch_payway_btn);
//        btn_ordersearch_qqwallet.setBackgroundResource(R.drawable.ordersearch_payway_btn);
//        btn_pay_scancode.setBackgroundResource(R.drawable.ordersearch_payway_btn);

        btn_ordersearch_weixin.setBackgroundResource(R.drawable.ordersearch_payway_btn);
        setBtnDrawable(R.drawable.weixinsearch2,btn_ordersearch_weixin);
        wx="";

        btn_ordersearch_zhifubao.setBackgroundResource(R.drawable.ordersearch_payway_btn);
        setBtnDrawable(R.drawable.ordersearch_zhifubao2,btn_ordersearch_zhifubao);
        alipay="";

        btn_ordersearch_qqwallet.setBackgroundResource(R.drawable.ordersearch_payway_btn);
        setBtnDrawable(R.drawable.ordersearch_qqwallet2,btn_ordersearch_qqwallet);
        qq="";

        btn_pay_scancode.setBackgroundResource(R.drawable.ordersearch_payway_btn);
        setBtnDrawable(R.drawable.ordersearch_payway2,btn_pay_scancode);
        micro="";

        btn_pay_cash.setBackgroundResource(R.drawable.ordersearch_payway_btn);
        setBtnDrawable(R.drawable.ordersearch_payway2,btn_pay_scancode);
        cash="";

        btn_pay_member.setBackgroundResource(R.drawable.ordersearch_payway_btn);
        setBtnDrawable(R.drawable.ordersearch_payway2,btn_pay_scancode);
        member="";

    }

    /**
     * 时间监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.btn_ordersearch_weixin:
                if (isSelect_weixin_num%2==0){
                    btn_ordersearch_weixin.setBackgroundResource(R.drawable.background_ordersearch_reset_btn);
                    setBtnDrawable(R.drawable.weixinsearch1,btn_ordersearch_weixin);
                    wx="wxpayjsapi";
                }else {
                    btn_ordersearch_weixin.setBackgroundResource(R.drawable.ordersearch_payway_btn);
                    setBtnDrawable(R.drawable.weixinsearch2,btn_ordersearch_weixin);
                    wx="";
                }
                isSelect_weixin_num++;
                break;
            case R.id.btn_ordersearch_zhifubao:
                if (isSelect_zhifubao_num%2==0){
                    btn_ordersearch_zhifubao.setBackgroundResource(R.drawable.background_ordersearch_reset_btn);
                    setBtnDrawable(R.drawable.ordersearch_zhifubao1,btn_ordersearch_zhifubao);
                    alipay="alipay";
                }else {
                    btn_ordersearch_zhifubao.setBackgroundResource(R.drawable.ordersearch_payway_btn);
                    setBtnDrawable(R.drawable.ordersearch_zhifubao2,btn_ordersearch_zhifubao);
                    alipay="";
                }
                isSelect_zhifubao_num++;
                break;
            case R.id.btn_ordersearch_qqwallet:
                if (isSelect_qqwallet_num%2==0){
                    btn_ordersearch_qqwallet.setBackgroundResource(R.drawable.background_ordersearch_reset_btn);
                    setBtnDrawable(R.drawable.ordersearch_qqwallet1,btn_ordersearch_qqwallet);
                    qq="qq";
                }else {
                    btn_ordersearch_qqwallet.setBackgroundResource(R.drawable.ordersearch_payway_btn);
                    setBtnDrawable(R.drawable.ordersearch_qqwallet2,btn_ordersearch_qqwallet);
                    qq="";
                }
                isSelect_qqwallet_num++;
                break;
            case R.id.btn_pay_scancode:
                if (isSelect_scancode_num%2==0){
                    btn_pay_scancode.setBackgroundResource(R.drawable.background_ordersearch_reset_btn);
                    setBtnDrawable(R.drawable.ordersearch_payway1,btn_pay_scancode);
                    micro="micro";
                }else {
                    btn_pay_scancode.setBackgroundResource(R.drawable.ordersearch_payway_btn);
                    setBtnDrawable(R.drawable.ordersearch_payway2,btn_pay_scancode);
                    micro="";
                }
                isSelect_scancode_num++;
                break;
            case R.id.btn_pay_cash:
                if (isSelect_cash_num%2==0){
                    btn_pay_cash.setBackgroundResource(R.drawable.background_ordersearch_reset_btn);
                    setBtnDrawable(R.drawable.paycash_purple_25,btn_pay_cash);
                    cash="cash";
                }else {
                    btn_pay_cash.setBackgroundResource(R.drawable.ordersearch_payway_btn);
                    setBtnDrawable(R.drawable.paycash_gray_25,btn_pay_cash);
                    cash="";
                }
                isSelect_cash_num++;
                break;
            case R.id.btn_pay_member:
                if (isSelect_member_num%2==0){
                    btn_pay_member.setBackgroundResource(R.drawable.background_ordersearch_reset_btn);
                    setBtnDrawable(R.drawable.member2,btn_pay_member);
                    member="yes";
                }else {
                    btn_pay_member.setBackgroundResource(R.drawable.ordersearch_payway_btn);
                    setBtnDrawable(R.drawable.member1,btn_pay_member);
                    member="";
                }
                isSelect_member_num++;
                break;
            case R.id.btn_ordersearch_reset:
                initResetData();
                break;
            case R.id.btn_ordersearch_save:
                date_begin=et_orderstart_date.getText().toString().trim()+" "+et_orderstart_time.getText().toString().trim();
                date_end= et_orderend_date.getText().toString().trim()+" "+et_orderend_time.getText().toString().trim();
                String money= et_order_money.getText().toString().trim();
                String order_id= et_order_num.getText().toString().trim();
                if(TextUtils.isEmpty(order_id)){
                order_id="";
                 }
                if(DateUtils.getDateSpan(date_begin,date_end,0)>=7&&order_id.length()==0){
                    SysUtils.showError("搜索时间间隔不能超过7天！");
                    return;
                }
//                if( !date_begin.substring(0,7).equals(date_end.substring(0,7))){
//                    SysUtils.showError("搜索时间必须为同一个月！");
//                    return;
//                }
                if (money.length()==0&&order_id.length()==0&&wx==""&&alipay==""&&qq==""&&micro==""&&cash==""&&member==""){
                    Bundle bundle = new Bundle();
                    bundle.putString("date_begin", date_begin);
                    bundle.putString("date_end", date_end);
                    bundle.putInt("type", 5);
                    SysUtils.startAct(OrderSearchActivity.this, new ThisMonthAllOrderActivity(), bundle);
                }else {

                    Intent intent_save = new Intent(OrderSearchActivity.this, SearchActivity.class);
                    intent_save.putExtra("date_begin", date_begin);
                    intent_save.putExtra("date_end", date_end);
                    intent_save.putExtra("money", money);
                    intent_save.putExtra("order_id", order_id);
                    intent_save.putExtra("wx", wx);
                    intent_save.putExtra("alipay", alipay);
                    intent_save.putExtra("qq", qq);
                    intent_save.putExtra("micro", micro);
                    intent_save.putExtra("cash", cash);
                    intent_save.putExtra("member", member);
                    startActivity(intent_save);
                }

                break;
        }
    }

    /**
     * 设置收款方式，选择图片的变化
     * @param drawable_view
     * @param btn
     */
    private  void setBtnDrawable(int drawable_view,Button btn ){
        Drawable drawable= getResources()
                .getDrawable(drawable_view);
/// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        btn .setCompoundDrawables(drawable,null,null,null);
        btn.setPadding(BitmapUtils.Dp2Px(OrderSearchActivity.this,15),0,0,0);
    }

    /**
     * 显示时间
     * @param editText
     */
    private void showCalendarTime(final EditText editText) {
        SysUtils.hideSoftKeyboard(OrderSearchActivity.this);
        final TimePickerDialog.OnTimeSetListener time=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                mCalendar.set(Calendar.MINUTE,minute);
                    editText.setText(mTimeFormat.format(mCalendar.getTime()));
            }
        };
        TimePickerDialog mTimePickerDialog=new TimePickerDialog(OrderSearchActivity.this,time,
               mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),true);
        mTimePickerDialog.show();
    }

    /**
     * 显示日期
     * @param editText
     */
    private void showCalendarDate(final EditText editText) {
        SysUtils.hideSoftKeyboard(OrderSearchActivity.this);
        final String CurDate=DateUtils.getCurDate().substring(0,10);
        DatePickerDialog.OnDateSetListener data=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR,year);
                mCalendar.set(Calendar.MONTH,monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                if(DateUtils.getDateSpan(CurDate,mDateFormat.format(mCalendar.getTime()),1)>0){
                    editText.setText(CurDate);
                    SysUtils.showError("开始日期超过当前日期,自动调整为当前日期");
                }else {
                    editText.setText(mDateFormat.format(mCalendar.getTime()));
                }

            }
        };
        DatePickerDialog mDatePickerDialog=new DatePickerDialog(OrderSearchActivity.this,data,mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }
}
