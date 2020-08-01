package com.ui.ks;

import android.os.Bundle;
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

public class ProfileEntryActivity extends BaseActivity {
    //公告内容
    TextView textView9;
    EditText textView10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 1);
        setContentView(R.layout.activity_profile_entry);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);

        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (EditText) findViewById(R.id.textView10);    //详细地址
        new DeleteEditText(textView10, textView9);


        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notice = textView10.getText().toString();
                if (StringUtils.isEmpty(notice)) {
                    SysUtils.showError("请输入登录密码");
                } else {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("pwd", notice);

                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("toLog"), map, new Response.Listener<JSONObject>() {
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
                                    Bundle b = new Bundle();
                                    b.putBoolean("hasPass", true);

                                    SysUtils.startAct(ProfileEntryActivity.this, new ProfileActivity(), b);
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

                    showLoading(ProfileEntryActivity.this);
                }
            }
        });
    }

}
