package com.ui.ks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.LoginUtils;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;


public class MemberProfileActivity extends BaseActivity {
    LinearLayout linearLayout1; //姓名行
    TextView textView1; //姓名

    LinearLayout linearLayout4; //手机号行
    TextView textView4; //手机号

    LinearLayout linearLayout5; //固定电话行
    TextView textView5; //固定电话

    LinearLayout linearLayout8; //地区
    TextView textView15;
    LinearLayout linearLayout7; //地址
    TextView textView7;

    LinearLayout linearLayout6; //修改密码行

    String name = "";
    String mobile = "";
    String tel = "";
    String area = "";
    String addr = "";

    int provinceId = 0;
    int cityId = 0;
    int areaId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 2);
        setContentView(R.layout.activity_member_profile);

        initToolbar(this);

        //姓名
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        textView1 = (TextView) findViewById(R.id.textView1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("name", name);

                SysUtils.startAct(MemberProfileActivity.this, new MemberProfileNicknameActivity(), b, true);
            }
        });


        //手机号
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        textView4 = (TextView) findViewById(R.id.textView4);
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("mobile", mobile);

                SysUtils.startAct(MemberProfileActivity.this, new MemberProfileMobileActivity(), b, true);
            }
        });

        //固定电话
        linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        textView5 = (TextView) findViewById(R.id.textView5);
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("tel", tel);

                SysUtils.startAct(MemberProfileActivity.this, new MemberProfileTelActivity(), b, true);
            }
        });

        //修改密码
        linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.startAct(MemberProfileActivity.this, new ProfilePasswordActivity());
            }
        });

        //地区
        linearLayout8 = (LinearLayout) findViewById(R.id.linearLayout8);
        textView15 = (TextView) findViewById(R.id.textView15);
        linearLayout8.setOnClickListener(a);

        //地址
        linearLayout7 = (LinearLayout) findViewById(R.id.linearLayout7);
        textView7 = (TextView) findViewById(R.id.textView7);
        linearLayout7.setOnClickListener(a);

        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(MemberProfileActivity.this)
                        .theme(SysUtils.getDialogTheme())
                        .content(getString(R.string.are_you_sure_sign_out))
                        .positiveText(getString(R.string.sure))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                LoginUtils.logout(MemberProfileActivity.this, 2);
                                SysUtils.showSuccess(getString(R.string.signed_out));

                                //退出登录广播
                                sendBroadcast(new Intent(Global.BROADCAST_LOGOUT_ACTION));
                                finish();
                            }
                        })
                        .show();
            }
        });

        initView();
    }

    private View.OnClickListener a = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle b = new Bundle();
            b.putInt("provinceId", provinceId);
            b.putInt("cityId", cityId);
            b.putInt("areaId", areaId);
            b.putString("area", area);
            b.putString("address", addr);

            SysUtils.startAct(MemberProfileActivity.this, new MemberProfileAddressActivity(), b, true);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle b = data.getExtras();
            if(b != null) {
                if (b.containsKey("refresh")) {
                    initView();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getMemberServiceUrl("setting"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        name = dataObject.getString("name");
                        textView1.setText(name);

                        mobile = dataObject.getString("mobile");
                        textView4.setText(mobile);

                        tel = dataObject.getString("tel");
                        textView5.setText(tel);

                        area = dataObject.getString("area");
                        textView15.setText(area);

                        getAreaId(dataObject.getString("area_id"));

                        addr = dataObject.getString("addr");
                        textView7.setText(addr);
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

    private void getAreaId(String area_id) {
        String[] aa = area_id.split(",");

        int aIndex = 0;
        for (int  i = 0; i < aa.length; i++) {
            if (!StringUtils.isEmpty(aa[i])) {
                int aid = Integer.parseInt(aa[i]);

                if (aid > 0) {
                    if (aIndex == 0) {
                        provinceId = aid;
                    } else if(aIndex == 1) {
                        cityId = aid;
                    } else if(aIndex == 2) {
                        areaId= aid;
                    }
                    aIndex++;
                }

            }
        }
    }
}
