package com.ui.ks;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.CustomRequest;
import com.ui.util.DeleteEditText;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends BaseActivity {
    TextView textView4;
    EditText textView5;
    PaperButton button1;
    private int fkType = 0;
    CheckBox cb_left, cb_right;

    TextView textView6;
    EditText textView7;
    EditText editText1;

    private TextView textView2;

    private String finalMobile, finalType;
    private int finalMemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initToolbar(this);
        //用户名
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (EditText) findViewById(R.id.textView7);    //用户名
        new DeleteEditText(textView7, textView6);

        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (EditText) findViewById(R.id.textView5);    //手机号
        new DeleteEditText(textView5, textView4);

        editText1 = (EditText) findViewById(R.id.editText1);    //验证码

        cb_left = (CheckBox) findViewById(R.id.cb_left);
        cb_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFkType(2);
            }
        });
        cb_right = (CheckBox) findViewById(R.id.cb_right);
        cb_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFkType(1);
            }
        });

        setFkType(fkType);

        //发送验证码
        textView2 = (TextView) findViewById(R.id.textView2);    //重新发送按钮
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });

        //下一步
        button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editText1.getText().toString();

                if(TextUtils.isEmpty(finalMobile)) {
                    SysUtils.showError(getString(R.string.str230));//请先获取验证码
                } else if (finalMemId <= 0) {
                    SysUtils.showError(getString(R.string.str231));//请选择帐号类型
                } else if(TextUtils.isEmpty(code)) {
                    SysUtils.showError(getString(R.string.str232));//请填写短信验证码
                } else {
                    //校验验证码
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("mobile", finalMobile);
                    map.put("sel_meb_id", String.valueOf(finalMemId));
                    map.put("type", finalType);
                    map.put("code", code);

                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("resetpwdCode"), map, new Response.Listener<JSONObject>() {
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
                                    JSONObject dataObject = ret.getJSONObject("data");

                                    Bundle b = new Bundle();
                                    b.putString("code_sing", dataObject.getString("code_sing"));
                                    b.putString("code_id", dataObject.getString("code_id"));
                                    b.putInt("sel_meb_id", finalMemId);
                                    b.putString("type", finalType);

                                    SysUtils.startAct(ForgetPasswordActivity.this, new ForgetPasswordThreeActivity(), b);
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

                    showLoading(ForgetPasswordActivity.this, getString(R.string.str92));
                }
            }
        });
    }

    private void setFkType(int type) {
        if(type == 1) {
            cb_left.setChecked(false);
            cb_right.setChecked(true);
        } else if (type == 2){
            cb_left.setChecked(true);
            cb_right.setChecked(false);
        } else {
            cb_left.setChecked(false);
            cb_right.setChecked(false);
        }

        this.fkType = type;
    }

    private void getCode() {
        String username = textView7.getText().toString();
        String mobile = textView5.getText().toString();

        if(TextUtils.isEmpty(username)) {
            SysUtils.showError(getString(R.string.str233));//请填写您的登录用户名
        } else if(TextUtils.isEmpty(mobile)) {
            SysUtils.showError(getString(R.string.str234));//请填写您的手机号
        } else if (fkType != 1 && fkType != 2) {
            SysUtils.showError(getString(R.string.str235));//请选择帐号类型
        } else {
            Map<String,String> map = new HashMap<String,String>();
            map.put("name", username);
            map.put("phone", mobile);
            map.put("type", String.valueOf(fkType));

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
                            SysUtils.showSuccess(getString(R.string.str236));//验证码已发送，请检查您的手机

                            JSONObject dataObject = ret.getJSONObject("data");
                            finalMobile = dataObject.getString("mobile");
                            finalMemId = dataObject.getInt("sel_meb_id");
                            finalType = dataObject.getString("type");
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

            showLoading(ForgetPasswordActivity.this, getString(R.string.str92));//请稍等
        }
    }
}
