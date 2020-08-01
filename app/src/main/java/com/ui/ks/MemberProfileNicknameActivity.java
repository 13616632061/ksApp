package com.ui.ks;

import android.content.Intent;
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


public class MemberProfileNicknameActivity extends BaseActivity {
    TextView textView1;
    EditText textView3;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 2);
        setContentView(R.layout.activity_member_profile_nickname);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if(bundle.containsKey("name")) {
                name = bundle.getString("name");
            }
        }

        textView1 = (TextView) findViewById(R.id.textView1);
        textView3 = (EditText) findViewById(R.id.textView3);    //姓名
        new DeleteEditText(textView3, textView1);
        textView3.setText(name);

        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textView3.getText().toString();

                if(StringUtils.isEmpty(username)) {
                    SysUtils.showError("姓名不能为空");
                } else {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name", username);

                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getMemberServiceUrl("save_setting"), map, new Response.Listener<JSONObject>() {
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
                                    SysUtils.showSuccess("姓名已修改");

                                    Intent returnIntent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("refresh", true);
                                    returnIntent.putExtras(bundle);
                                    setResult(RESULT_OK, returnIntent);

                                    onBackPressed();
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

                    showLoading(MemberProfileNicknameActivity.this, getString(R.string.str92));
                }
            }
        });
    }
}
