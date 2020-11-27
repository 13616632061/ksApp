package com.ui.view.avtivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.MyApplication.KsApplication;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.global.Global;
import com.ui.ks.LoginActivity;
import com.ui.ks.MainStoreActivity;
import com.ui.ks.R;
import com.ui.ks.ReportActivity;
import com.ui.util.CustomRequest;
import com.ui.util.LoginUtils;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {
//    private boolean hasSpash = false;
Boolean isExit = false;
    Boolean hasTask = false;
    Timer tExit;
    TimerTask task;
    RelativeLayout relativeLayout2, relativeLayout1, relativeLayout3;
    private static final int REQUEST_PERMISSION = 0;
    public IWXAPI api;
    long[] mHits = new long[3];//点击3下

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 0);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_welcome, null);

        setContentView(view);

        requestPermission();

        RelativeLayout relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    int isTest = KsApplication.getInt("isTest", 0);
                    new MaterialDialog.Builder(WelcomeActivity.this)
                            .title(getString(R.string.str114))
                            .items(R.array.env_list)
                            .theme(SysUtils.getDialogTheme())
                            .itemsCallbackSingleChoice(isTest, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    okModifyEnv(which);
                                    return true;
                                }
                            })
                            .positiveText(getString(R.string.sure))
                            .negativeText(getString(R.string.cancel))
                            .show();
                }
            }
        });

        tExit = new Timer();
        task = new TimerTask() {
            public void run() {
                isExit = false;
                hasTask = true;
            }
        };

        //普通登录
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSellerSite();
            }
        });

        //易星到家
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //店铺
        relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtils.isSeller()) {
                    SysUtils.startAct(WelcomeActivity.this, new ShopActivity());
                } else if (LoginUtils.isShopper()) {
                    SysUtils.startAct(WelcomeActivity.this, new ShopActivity());
                }else if (LoginUtils.isMember()) {
                    SysUtils.startAct(WelcomeActivity.this, new ReportActivity());
                } else if (LoginUtils.isMainStore()) {
                    SysUtils.startAct(WelcomeActivity.this, new MainStoreActivity());
                }else {
                    SysUtils.startAct(WelcomeActivity.this, new LoginActivity());
                }
            }
        });
    }
    private void okModifyEnv(int env) {
        KsApplication.putInt("isTest", env);

        //退出登录
        LoginUtils.logout(this, 0);

        if(env == 1) {
            SysUtils.showSuccess("已切换为测试环境");
        } else {
            SysUtils.showSuccess("已切换为线上环境");
        }
    }


//    READ_EXTERNAL_STORAGE
    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showWaringDialog();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(isExit==false){
                isExit=true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                if(!hasTask){
                    tExit.schedule(task, 2000);
                }
            } else{
                moveTaskToBack(false);  //进入后台
//                finish();
//                System.exit(0);
            }
        }

        return false;
    }


    private boolean hasInitWx = false;
    public void initWeixin() {
        if(!hasInitWx) {
            String wx_app_id = Global.isDebug ? Global.WX_DEBUG_APP_ID : Global.WX_APP_ID;

//            SysUtils.showSuccess(wx_app_id);
            api = WXAPIFactory.createWXAPI(this, wx_app_id, false);
            boolean ret = api.registerApp(wx_app_id);
//            SysUtils.showSuccess("to register: " + (ret ? "1" : "0"));

            hasInitWx = true;
        }
    }

    private void toH5() {
        wxLogin();
    }

    private void wxLogin() {
        initWeixin();
        if(!api.isWXAppInstalled()) {
            SysUtils.showError("微信未安装");
        } else {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "none";
            boolean ret = api.sendReq(req);
//            SysUtils.showSuccess("to login: " + (ret ? "1" : "0"));
        }
    }


    //接受广播，更新ui
    private BroadcastReceiver broadcastWeixinReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //微信登录返回
            if(intent.hasExtra("code")) {
                String code = intent.getStringExtra("code");

                //取得access token
                String u = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + (Global.isDebug ? Global.WX_DEBUG_APP_ID : Global.WX_APP_ID)
                        + "&secret=" + (Global.isDebug ? Global.WX_DEBUG_APP_SECRET : Global.WX_APP_SECRET)
                        + "&code=" + code + "&grant_type=authorization_code";
                CustomRequest r = new CustomRequest(Request.Method.GET, u, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        hideLoading();

                        try {
                            if(jsonObject.has("access_token")) {
                                String token = jsonObject.getString("access_token");
                                String openid = jsonObject.getString("openid");

                                //根据token和openid去获取用户信息
                                weixinGetUserInfo(token, openid);
                            } else if(jsonObject.has("errcode")) {
                                SysUtils.showError(jsonObject.getString("errmsg"));
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

                showLoading(WelcomeActivity.this, "正在验证......");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastWeixinReceiver, new IntentFilter(Global.BROADCAST_WEIXIN_LOGIN_ACTION));
        Log.d("print打印广播的数据","打印广播的数据0000");
        if (LoginUtils.isSeller()) {
            Log.d("print打印广播的数据","打印广播的数据1111");
            SysUtils.startAct(WelcomeActivity.this, new ShopActivity());
//            WelcomeActivity.this.finish();
        } else if (LoginUtils.isShopper()) {
            Log.d("print打印广播的数据","打印广播的数据22222");
            SysUtils.startAct(WelcomeActivity.this, new ShopActivity());
//            WelcomeActivity.this.finish();
        }else if (LoginUtils.isMember()) {
            Log.d("print打印广播的数据","打印广播的数据33333");
            SysUtils.startAct(WelcomeActivity.this, new ReportActivity());
//            WelcomeActivity.this.finish();
        } else if (LoginUtils.isMainStore()) {
            Log.d("print打印广播的数据","打印广播的数据44444");
            SysUtils.startAct(WelcomeActivity.this, new MainStoreActivity());
//            WelcomeActivity.this.finish();
        }else {
            Log.d("print打印广播的数据","打印广播的数据555555");
            SysUtils.startAct(WelcomeActivity.this, new LoginActivity());
//            WelcomeActivity.this.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(broadcastWeixinReceiver);
        } catch(Exception e) {

        }
    }

    private void weixinGetUserInfo(String token, String openid) {
        String u = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token
                + "&openid=" + openid + "&lang=zh_CN";
        CustomRequest r = new CustomRequest(Request.Method.GET, u, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();

                try {
                    if(jsonObject.has("openid")) {
                        KsApplication.putString("wxUserInfo", jsonObject.toString());

                        toSite();
                    } else if(jsonObject.has("errcode")) {
                        SysUtils.showError(jsonObject.getString("errmsg"));
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

        showLoading(WelcomeActivity.this, "正在获取用户身份......");
    }

    private void toSite() {
//        SysUtils.openUrl(WelcomeActivity.this, "http://www.smgypt.com/webview.php");
        SysUtils.openUrl(WelcomeActivity.this, SysUtils.getWebUri() + "wap/seller-seller_page-56.html");
    }

    private void toSellerSite() {
        String u = KsApplication.getString("wxUserInfo", "");

        if (!StringUtils.isEmpty(u)) {
            toSite();
        } else {
            toH5();
        }
    }
}
