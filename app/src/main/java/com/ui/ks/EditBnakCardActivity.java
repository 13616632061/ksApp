package com.ui.ks;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改银行卡页面
 */

public class EditBnakCardActivity extends BaseActivity {

    private TextView tv_phone;
    private TextView tv_sendcode;
    private EditText et_code;
    private String  username;//商户名
    private String  khr;//开户人
    private String  bankcard;//卡号
    private int finalMemId;//商户id
    private String finalType,finalMobile;
    private PaperButton bt_editcard_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bnak_card);

        SysUtils.setupUI(this, findViewById(R.id.activity_edit_bnak_card));//点击空白处隐藏软键盘
        initToolbar(this);//封装的页面返回键，及返回监听
        initView();
        setListener();
    }
    private void  initView(){
        tv_phone=(TextView) findViewById(R.id.tv_phone);
        tv_sendcode=(TextView) findViewById(R.id. tv_sendcode);
        et_code=(EditText) findViewById(R.id.et_code);
        bt_editcard_next=(PaperButton) findViewById(R.id.bt_editcard_next);

        getPhone();
    }
    private void setListener(){
            tv_sendcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCode();
                }
            });
            bt_editcard_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getbtnNext();
                }
            });

    }
    /**
     *点击“下一步”按钮，验证码校验
     */
    private void getbtnNext(){
                           String str_code=et_code.getText().toString().trim();
                    if(TextUtils.isEmpty(finalMobile)){
                        SysUtils.showError("请先获取验证码");
                        return;
                    }
                    if(TextUtils.isEmpty(str_code)){
                        SysUtils.showError("请先输入验证码");
                        return;
                    }else {
                        //校验验证码
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("mobile", finalMobile);
                        map.put("sel_meb_id", String.valueOf(finalMemId));
                        map.put("type", finalType);
                        map.put("code", str_code);

                        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("resetpwdCode"), map, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    hideLoading();
                                    JSONObject ret = SysUtils.didResponse(jsonObject);
                                    System.out.println("ret="+ret);
                                    String status = ret.getString("status");
                                    String message = ret.getString("message");

                                    if (!status.equals("200")) {
                                        SysUtils.showError(message);
                                    } else {
                                        JSONObject dataObject = ret.getJSONObject("data");

                                        Bundle b = new Bundle();
                                        b.putString("khr", khr);
                                        b.putString("bankcard", bankcard);

                                        SysUtils.startAct(EditBnakCardActivity.this, new SubmitEditBnakCardActivity(), b);
                                        finish();
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

                        showLoading(EditBnakCardActivity.this, getString(R.string.str92));
                    }
    }
    /**
     * 获取手机号和账户名
     */
    private void  getPhone(){
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("account"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("retttt="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONObject sellerObject = dataObject.getJSONObject("seller_info");
                        tv_phone.setText(sellerObject.getString("mobile"));
                        username=sellerObject.getString("seller_name");
                        khr=sellerObject.optString("khr");
                        bankcard=sellerObject.optString("bankcard");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showNetworkError();
                hideLoading();
            }
        });

        executeRequest(r);
        showLoading(this);
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String str_tv_phone=tv_phone.getText().toString().trim();
        if(TextUtils.isEmpty(str_tv_phone)) {
            SysUtils.showError("手机号不能为空!");
        } else {
            Map<String,String> map = new HashMap<String,String>();
            map.put("name", username);
            map.put("phone", str_tv_phone);
            map.put("type", String.valueOf(1));//1表示类型为商户

            CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("sendVcodesSms"), map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        hideLoading();
                        JSONObject ret = SysUtils.didResponse(jsonObject);
                        String status = ret.getString("status");
                        String message = ret.getString("message");

                        if (!status.equals("200")) {
                            SysUtils.showError(message);
                        } else {
                            SysUtils.showSuccess("验证码已发送，请检查您的手机");
                            JSONObject dataObject = ret.getJSONObject("data");
                            finalMemId = dataObject.getInt("sel_meb_id");
                            finalType = dataObject.getString("type");
                            finalMobile = dataObject.getString("mobile");
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

            showLoading(EditBnakCardActivity.this, "请稍等......");
        }
    }
}
