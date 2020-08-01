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

public class NoticeActivity extends BaseActivity {
    //公告内容
    TextView textView9;
    EditText textView10;

    private String notice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 1);
        setContentView(R.layout.activity_notice);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("notice")) {
                notice = bundle.getString("notice");
            }
        }

        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (EditText) findViewById(R.id.textView10);    //详细地址
        new DeleteEditText(textView10, textView9);

        textView10.setText(notice);


        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notice = textView10.getText().toString().trim();
                if (StringUtils.isEmpty(notice)) {
                    SysUtils.showError(getString(R.string.str321));//店铺公告必填
                } else {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("intro", notice);

                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("intro"), map, new Response.Listener<JSONObject>() {
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
                                    SysUtils.showSuccess(getString(R.string.str322));//店铺公告已保存

                                    Intent returnIntent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("refreshReport", true);
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

                    showLoading(NoticeActivity.this);
                }
            }
        });
    }

}
