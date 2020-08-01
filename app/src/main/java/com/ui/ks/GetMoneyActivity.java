package com.ui.ks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.CustomRequest;
import com.ui.util.DateUtils;
import com.ui.util.SetEditTextInput;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetMoneyActivity extends BaseActivity implements View.OnClickListener {

    private TextView btn_set,tv_getmoney_bank,tv_getmoney_remain,tv_getmoney_explain,tv_remind;
    private TextView tv_fast_servicemoney,radio_btn_normal_explain1,radio_btn_fast_explain1;
    private PaperButton confirm;
    private RadioButton radio_btn_normal,radio_btn_fast;
    private RelativeLayout radio_btn_fast_layout;
    private EditText et_input_getmoney;
    private boolean isLoading = false;
    double advance;//余额
    int switch_status;//快速提现权限0 关闭 1 开启
    int button;//提现权限 0 关闭 1 开启
    int time_button;//是否在快速到账提现时间，范围以内：1在范围以内，0在范围以外
    int type=3;//提现类型 1普通 2快速 3表示没有选择提现类型
    double service;//服务费
    private String fastfree;
    private String fasttime;
    private String normaltime;
    private int isstore;//1表示总店提现，2表示分店提现

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money);

        SysUtils.setupUI(GetMoneyActivity.this,findViewById(R.id.activity_get_money));
        initToolbar(this);

        initView();
        initData();

    }

    private void initData() {
        Intent intent=getIntent();
        isstore=intent.getIntExtra("isstore",2);
        getData();
        btn_set.setVisibility(View.VISIBLE);
        btn_set.setBackgroundColor(Color.parseColor("#ffff8905"));
        btn_set.setText(getString(R.string.str213));
    }

    private void initView() {
        btn_set= (TextView) findViewById(R.id.btn_set);
        confirm= (PaperButton) findViewById(R.id.confirm);
        tv_getmoney_bank= (TextView) findViewById(R.id.tv_getmoney_bank);
        tv_getmoney_remain= (TextView) findViewById(R.id.tv_getmoney_remain);
        tv_getmoney_explain= (TextView) findViewById(R.id.tv_getmoney_explain);
        tv_fast_servicemoney= (TextView) findViewById(R.id.tv_fast_servicemoney);
        tv_remind= (TextView) findViewById(R.id.tv_remind);
        et_input_getmoney= (EditText) findViewById(R.id.et_input_getmoney);
        radio_btn_normal= (RadioButton) findViewById(R.id.radio_btn_normal);
        radio_btn_fast= (RadioButton) findViewById(R.id.radio_btn_fast);
        radio_btn_fast_layout= (RelativeLayout) findViewById(R.id.radio_btn_fast_layout);
        radio_btn_normal_explain1= (TextView) findViewById(R.id.radio_btn_normal_explain1);
        tv_fast_servicemoney= (TextView) findViewById(R.id.tv_fast_servicemoney);
        radio_btn_fast_explain1= (TextView) findViewById(R.id.radio_btn_fast_explain1);

        btn_set.setOnClickListener(this);
        confirm.setOnClickListener(this);
        radio_btn_normal.setOnClickListener(this);
        radio_btn_fast.setOnClickListener(this);
        tv_getmoney_explain.setOnClickListener(this);
        SetEditTextInput.judgeNumber(et_input_getmoney);

        radio_btn_normal.setChecked(true);
        type=1;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set:
                SysUtils.startAct(GetMoneyActivity.this,new MoneyLogActivity());
                break;
            case R.id.radio_btn_normal:
                radio_btn_fast.setChecked(false);
                tv_remind.setVisibility(View.GONE);
                type=1;
                break;
            case R.id.radio_btn_fast:
                radio_btn_normal.setChecked(false);
                tv_remind.setVisibility(View.GONE);
                type=2;
                break;
            case R.id.tv_getmoney_explain:
                SysUtils.startAct(GetMoneyActivity.this,new GetMoneyExplainActivity());
                break;
            case R.id.confirm:
                submit();
                break;
        }
    }
    public void getData() {
        if (isLoading) {
            return;
        }

        isLoading = true;

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("center"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                isLoading = false;

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("提现ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        advance = dataObject.getDouble("advance");
                        service=dataObject.getDouble("service_fee");
                        tv_getmoney_remain.setText("当前余额"+advance+"元");
                        JSONObject button_obj=dataObject.getJSONObject("button");
                        button=button_obj.getInt("button");
                        String fastfree=button_obj.getString("fastfree");
                        String fasttime=button_obj.getString("fasttime");
                        String normaltime=button_obj.getString("normaltime");
                        time_button=button_obj.getInt("time_button");
                        radio_btn_normal_explain1.setText(normaltime);
                        if (service>0){
                            tv_fast_servicemoney.setText(fastfree);
                        }else {
                            tv_fast_servicemoney.setText("  (免费)");
                        }
                        radio_btn_fast_explain1.setText(fasttime);
                        JSONObject seller_info=dataObject.getJSONObject("seller_info");
                        String bankcard=seller_info.getString("bankcard");
                        String acbank=seller_info.getString("acbank");
                        if (bankcard.length()<=4){
                            tv_getmoney_bank.setText(acbank+"("+bankcard+")");
                        }else {
                            String bankcard_str=bankcard.substring(bankcard.length()-4,bankcard.length());
                            tv_getmoney_bank.setText(acbank+"("+bankcard_str+")");
                        }
                        switch_status=seller_info.getInt("switch_status");
                        if(button==1&&switch_status==1&&time_button==1){
                            radio_btn_fast_layout.setVisibility(View.VISIBLE);
                            tv_getmoney_explain.setVisibility(View.VISIBLE);
                        }else {
                            radio_btn_fast_layout.setVisibility(View.GONE);
                            tv_getmoney_explain.setVisibility(View.GONE);
                        }

                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                hideLoading();
                SysUtils.showNetworkError();
            }
        });

        executeRequest(r);

//        showLoading(getActivity());
    }
    public void submit() {
        tv_remind.setVisibility(View.GONE);
        String et_input_getmoney_str=et_input_getmoney.getText().toString().trim();
        //时间毫秒
        String time_minu=DateUtils.getNowtimeKeyStr();
        double pay_money = 0;
        if(!StringUtils.isEmpty(et_input_getmoney_str)) {
            pay_money = Double.parseDouble(et_input_getmoney_str);
            if(pay_money<1){
                SysUtils.showError(getString(R.string.str214));//最少提现金额大于1元！
                return;
            }
        }
        if (pay_money<=0){
            SysUtils.showError(getString(R.string.str215));//请输入提现金额！
            return;
        }
        if (type==3){
            SysUtils.showError(getString(R.string.str216));//请选择提现类型！
            return;
        }
        Map<String,String> map = new HashMap<String,String>();
        if(type==2){
            map.put("money", String.valueOf(pay_money));
        }else if (type==1){
            map.put("money", String.valueOf(pay_money));
        }
        map.put("type", type+"");
        map.put("isstore",isstore+"");

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("deductAdvance"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("提现ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");

                    if (!status.equals("200")) {
                        if(status.equals("E.403")){
                            AlertDialog.Builder builder=new AlertDialog.Builder(GetMoneyActivity.this)
                                    .setMessage(message)
                                    .setPositiveButton(getString(R.string.sure), new AlertDialog.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getData();
                                        }
                                    });
                            builder.show();
                        }else if(status.equals("E.402")){
                            tv_remind.setVisibility(View.VISIBLE);
                            tv_remind.setText(message);
                        }else {
                            SysUtils.showError(message);
                        }
                    } else {
                        JSONObject dataObject = ret.getJSONObject("data");
                        et_input_getmoney.setText("");
                        new MaterialDialog.Builder(GetMoneyActivity.this)
                                .theme(SysUtils.getDialogTheme())
                                .content(getString(R.string.str217))//提现申请已提交，我们会尽快处理您的申请
                                .positiveText(getString(R.string.str218))//知道啦
                                .show();
                        advance=dataObject.getDouble("advance");
                        tv_getmoney_remain.setText(getString(R.string.str219)+advance+getString(R.string.str23));//当前余额
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                SysUtils.showNetworkError();
            }
        });

        executeRequest(r);
        showLoading(GetMoneyActivity.this, getString(R.string.str92));

//        showLoading(getActivity());
    }
}
