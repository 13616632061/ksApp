package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.entity.PayResult;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.LoginUtils;
import com.ui.util.SysUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayActivity extends ShareBaseActivity {
    private double pay_money = 0;
    private boolean isPaySuccess = false;
    private View set_pay, set_ali;
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private TextView textView2, textView10;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initToolbar(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("pay_money")) {
                pay_money = bundle.getDouble("pay_money");
            }
        }

        if(pay_money <= 0) {
            finish();
        }

        textView2 = (TextView) findViewById(R.id.textView2);
        textView10 = (TextView) findViewById(R.id.textView10);

        //微信付款
        set_pay = (View) findViewById(R.id.set_wx_item);
        //微信支付
        SysUtils.setLine(set_pay, Global.SET_SINGLE_LINE, getString(R.string.str160), R.drawable.share_wx_friends, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWeixinPay();
            }
        });

        //支付宝付款
        set_ali = (View) findViewById(R.id.set_ali_item);
        SysUtils.setLine(set_ali, Global.SET_SINGLE_LINE, getString(R.string.str161), R.drawable.pay_ali, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAliPay();
            }
        });

        //需付款
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(SysUtils.getMoneyFormat(pay_money));

        //注册微信支付
        req = new PayReq();
        msgApi.registerApp(Global.WX_APP_ID);
    }

    private void toSuccess() {
        textView10.setText(getString(R.string.str162));//付款金额
        setToolbarTitle(getString(R.string.str163));//充值成功
        isPaySuccess = true;

        set_pay.setVisibility(View.GONE);
        set_ali.setVisibility(View.GONE);
    }


    //接受广播，更新ui
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            toSuccess();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_PAY_SUCCESS_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(broadcastReceiver);
        } catch(Exception e) {

        }
    }

    //微信支付
    private void sendWeixinPay() {
        if(isLoading) {
            return;
        }

        isLoading = true;
        Map<String,String> map = new HashMap<String,String>();
        map.put("money", String.valueOf(pay_money));
//        map.put("type", LoginUtils.isSeller() ? "seller" : "member");

        String uri = LoginUtils.isSeller() ? SysUtils.getSellerServiceUrl("create_wechat_order") : SysUtils.getMemberServiceUrl("create_wechat_order");

        CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                isLoading = false;
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
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
                isLoading = false;
                hideLoading();

                SysUtils.showNetworkError();
            }
        });

        executeRequest(r);

        showLoading(PayActivity.this, getString(R.string.str92));
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
                        toSuccess();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            SysUtils.showError(getString(R.string.str164));

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

    private void sendAliPay() {
        if(isLoading) {
            return;
        }

        isLoading = true;
        Map<String,String> map = new HashMap<String,String>();
        map.put("money", String.valueOf(pay_money));

        String uri = LoginUtils.isSeller() ? SysUtils.getSellerServiceUrl("create_ali_order") : SysUtils.getMemberServiceUrl("create_ali_order");

        CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                isLoading = false;
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
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
                                PayTask alipay = new PayTask(PayActivity.this);
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
                isLoading = false;
                hideLoading();

                SysUtils.showNetworkError();
            }
        });

        executeRequest(r);

        showLoading(PayActivity.this, getString(R.string.str92));
    }
}
