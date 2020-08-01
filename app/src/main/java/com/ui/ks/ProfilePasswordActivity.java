package com.ui.ks;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.CustomRequest;
import com.ui.util.DeleteEditText;
import com.ui.util.LoginUtils;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilePasswordActivity extends BaseActivity {
    //原始密码
    TextView textView1;
    EditText textView3;

    //新密码
    TextView textView4;
    EditText textView6;

    //确认新密码
    TextView textView7;
    EditText textView9;

    private boolean has_password = false;
    private RelativeLayout relativeLayout1;
    PaperButton button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_password);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);

        has_password = true;

        textView1 = (TextView) findViewById(R.id.textView1);
        textView3 = (EditText) findViewById(R.id.textView3);    //原始密码
        new DeleteEditText(textView3, textView1);

        textView4 = (TextView) findViewById(R.id.textView4);
        textView6 = (EditText) findViewById(R.id.textView6);    //新密码
        new DeleteEditText(textView6, textView4);

        textView7 = (TextView) findViewById(R.id.textView7);
        textView9 = (EditText) findViewById(R.id.textView9);    //确认新密码
        new DeleteEditText(textView9, textView7);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        if(has_password) {
            relativeLayout1.setVisibility(View.VISIBLE);
        } else {
            relativeLayout1.setVisibility(View.GONE);
        }


        //下一步
        button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textView3.getText().toString();
                String newpwd = textView6.getText().toString();
                String re_newpwd = textView9.getText().toString();

                if(has_password && StringUtils.isEmpty(username)) {
                    SysUtils.showError(getString(R.string.str282));//原始密码不能为空
                } else if(StringUtils.isEmpty(newpwd)) {
                    SysUtils.showError(getString(R.string.str188));//新密码不能为空
                } else if(newpwd.length() < 6) {
                    SysUtils.showError(getString(R.string.str189));//新密码至少6位
                } else if(!newpwd.equals(re_newpwd)) {
                    SysUtils.showError(getString(R.string.str190));//确认密码应与新密码一致
                } else {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name", KsApplication.getString("login_username", ""));
                    map.put("pwd", username);
                    map.put("passwd", newpwd);
                    map.put("passwdcfm", re_newpwd);
                    map.put("type", LoginUtils.isSeller() ? "1" : "2");

                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("updatepwd"), map, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            hideLoading();

                            try {
                                JSONObject ret = SysUtils.didResponse(jsonObject);
                                String status = ret.getString("status");
                                String message = ret.getString("message");

                                if (!status.equals("200")) {
                                    SysUtils.showError(message);
                                } else {
                                    SysUtils.showSuccess(getString(R.string.str283));//密码已修改

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

                    showLoading(ProfilePasswordActivity.this);
                }
            }
        });
    }

}
