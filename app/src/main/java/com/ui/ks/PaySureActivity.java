package com.ui.ks;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.PayResult;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.LoginUtils;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaySureActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_paymoney;
    private CheckBox check_weixin,check_zhifubao;
    private Button btn_surepay;
    private PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private int type=1;//1微信支付，2支付宝支付
    private   String order_id;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_sure);
        SysUtils.setupUI(PaySureActivity.this,findViewById(R.id.activity_pay_sure));
        initToolbar(this);

        initView();
        Intent intent=getIntent();
        if(intent!=null){
            order_id=intent.getStringExtra("order_id");
            money=intent.getStringExtra("money");
            tv_paymoney.setText("￥"+SetEditTextInput.stringpointtwo(money));
        }

    }

    private void initView() {
        tv_paymoney= (TextView) findViewById(R.id.tv_paymoney);
        check_weixin= (CheckBox) findViewById(R.id.check_weixin);
        check_zhifubao= (CheckBox) findViewById(R.id.check_zhifubao);
        btn_surepay= (Button) findViewById(R.id.btn_surepay);

        btn_surepay.setOnClickListener(this);

        check_weixin.setChecked(true);
        check_weixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check_zhifubao.setChecked(false);
                    type=1;
                }
            }
        });
        check_zhifubao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check_weixin.setChecked(false);
                    type=2;
                }
            }
        });

        //注册微信支付
        req = new PayReq();
        msgApi.registerApp(Global.WX_APP_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_surepay:
                if(type==1){
                    sendWeixinPay();
                }else if(type==2){
                    sendAliPay();
                }

                break;
        }
    }
    //微信支付
    private void sendWeixinPay() {

        Map<String,String> map = new HashMap<String,String>();
        map.put("money", String.valueOf(1));
//        map.put("type", LoginUtils.isSeller() ? "seller" : "member");

        String uri = LoginUtils.isSeller() ? SysUtils.getSellerServiceUrl("create_wechat_order") : SysUtils.getMemberServiceUrl("create_wechat_order");

        CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.print("微信ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONObject orderObject = dataObject.getJSONObject("data");

                        Log.v("ks", orderObject.toString());

                        //使用服务器返回的二次交易签名数据发起交易
                        req.appId = orderObject.getString("appid");
                        req.partnerId = orderObject.getString("partnerid");
                        req.prepayId = orderObject.getString("prepayid");
                        req.packageValue = orderObject.getString("package");
                        req.nonceStr = orderObject.getString("noncestr");
                        req.timeStamp = orderObject.getString("timestamp");
                        req.sign = orderObject.getString("sign");
                        msgApi.sendReq(req);

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

        showLoading(PaySureActivity.this, getString(R.string.str92));
    }
    private void sendAliPay() {

        Map<String,String> map = new HashMap<String,String>();
        map.put("money", String.valueOf(1));

        String uri = LoginUtils.isSeller() ? SysUtils.getSellerServiceUrl("create_ali_order") : SysUtils.getMemberServiceUrl("create_ali_order");

        CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.print("支付宝ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONObject orderObject = dataObject.getJSONObject("data");

                        String orderInfo = orderObject.getString("order_spec");
                        String sign = orderObject.getString("signedString");

                        // 完整的符合支付宝参数规范的订单信息
                        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";

                        Log.v("ks", payInfo);
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask(PaySureActivity.this);
                                // 调用支付接口，获取支付结果
                                String result = alipay.pay(payInfo, true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
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

        showLoading(PaySureActivity.this, getString(R.string.str92));
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //支付成功
                        SysUtils.showError("支付成功");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            SysUtils.showError("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            SysUtils.showError("支付失败");

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    break;
                }
                default:
                    break;
            }
        };
    };

}
