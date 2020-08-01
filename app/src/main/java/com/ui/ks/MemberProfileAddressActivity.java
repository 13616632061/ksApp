package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class MemberProfileAddressActivity extends BaseActivity {

    //省
    RelativeLayout relativeLayout1;
    TextView textView8;

    //市
//    RelativeLayout relativeLayout2;
//    TextView textView12;

    //县
//    RelativeLayout relativeLayout3;
//    TextView textView14;

    //详细地址
    TextView textView9;
    EditText textView10;

    private int provinceId = 0, cityId = 0, areaId = 0;
    private String area = "";
    private String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 2);
        setContentView(R.layout.activity_member_profile_address);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("provinceId")) {
                provinceId = bundle.getInt("provinceId");
            }
            if (bundle.containsKey("cityId")) {
                cityId = bundle.getInt("cityId");
            }
            if (bundle.containsKey("areaId")) {
                areaId = bundle.getInt("areaId");
            }
            if (bundle.containsKey("area")) {
                area = bundle.getString("area");
            }
            if (bundle.containsKey("address")) {
                address = bundle.getString("address");
            }
        }

        //省
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        textView8 = (TextView) findViewById(R.id.textView8);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putInt("pid", 0);

                SysUtils.startAct(MemberProfileAddressActivity.this, new AddressLocationActivity(), bundle, true);
            }
        });

        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (EditText) findViewById(R.id.textView10);    //详细地址
        new DeleteEditText(textView10, textView9);

        textView8.setText(area);
        textView10.setText(address);


        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(provinceId <= 0) {
                    SysUtils.showError("请选择所在地区");
                } else {
                    String address = textView10.getText().toString();
                    if(StringUtils.isEmpty(address)) {
                        SysUtils.showError("请填写详细地址");
                    } else {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("area", getPostArea());
                        map.put("addr", address);

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
                                        SysUtils.showSuccess("地址已修改");

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

                        showLoading(MemberProfileAddressActivity.this);
                    }
                }
            }
        });
    }

    private String getPostArea() {
        String ret = "mainland";
        ret += ":" + area;
        if(areaId > 0) {
            ret += ":" + areaId;
        } else if(cityId > 0) {
            ret += ":" + cityId;
        } else if(provinceId > 0) {
            ret += ":" + provinceId;
        }

//        SysUtils.showSuccess(ret);

        return ret;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            if(b != null && b.containsKey("provinceId") && b.containsKey("cityId") && b.containsKey("townId") && b.containsKey("areaStr")) {
                provinceId = b.getInt("provinceId");
                cityId = b.getInt("cityId");
                areaId = b.getInt("townId");
                area = b.getString("areaStr");
                textView8.setText(area);
            }
        }
    }
}
