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
import com.ui.util.DeleteEditText;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordThreeActivity extends BaseActivity {
    private String code_sing = "";
    private String code_id = "";
    private int sel_meb_id = 0;
    private String type = "";

    //新密码
    TextView textView4;
    EditText textView6;

    //确认新密码
    TextView textView7;
    EditText textView9;

    PaperButton button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_three);

        initToolbar(this);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("code_sing")) {
                code_sing = bundle.getString("code_sing");
            }
            if (bundle.containsKey("code_id")) {
                code_id = bundle.getString("code_id");
            }
            if (bundle.containsKey("sel_meb_id")) {
                sel_meb_id = bundle.getInt("sel_meb_id");
            }
            if (bundle.containsKey("type")) {
                type = bundle.getString("type");
            }
        }

        if(TextUtils.isEmpty(code_sing) || TextUtils.isEmpty(code_id) || sel_meb_id <= 0 || TextUtils.isEmpty(type)) {
            finish();
        }

        textView4 = (TextView) findViewById(R.id.textView4);
        textView6 = (EditText) findViewById(R.id.textView6);    //新密码
        new DeleteEditText(textView6, textView4);

        textView7 = (TextView) findViewById(R.id.textView7);
        textView9 = (EditText) findViewById(R.id.textView9);    //确认新密码
        new DeleteEditText(textView9, textView7);

        //最终提交
        button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newpwd = textView6.getText().toString();
                String re_newpwd = textView9.getText().toString();

                if(StringUtils.isEmpty(newpwd)) {
                    SysUtils.showError(getString(R.string.str188));//新密码不能为空
                } else if(newpwd.length() < 6) {
                    SysUtils.showError(getString(R.string.str189));//新密码至少6位
                } else if(!newpwd.equals(re_newpwd)) {
                    SysUtils.showError(getString(R.string.str190));//确认密码应与新密码一致
                } else {
                    //提交
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("code_sing", code_sing);
                    map.put("code_id", code_id);
                    map.put("sel_meb_id", String.valueOf(sel_meb_id));
                    map.put("type", type);
                    map.put("login_password", newpwd);
                    map.put("psw_confirm", re_newpwd);

                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("resetPassword"), map, new Response.Listener<JSONObject>() {
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
                                    SysUtils.showSuccess(getString(R.string.str191));//密码已重置，请重新登录

                                    SysUtils.startAct(ForgetPasswordThreeActivity.this, new LoginActivity());
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

                    showLoading(ForgetPasswordThreeActivity.this, getString(R.string.str92));
                }
            }
        });
    }
}
