package com.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.util.CustomRequest;
import com.ui.util.LoginUtils;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class YiyouService extends Service {
    public static final String BROADCAST_ACTION = "com.ms.yiyou.receiver.update";

    //每半小时检查一次
    public static final long NOTIFY_INTERVAL = 1800 * 1000;
    Intent intent;

    private Handler mHandler;
    public YiyouService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.removeCallbacks(sendUpdatesToUI);
        mHandler.postDelayed(sendUpdatesToUI, 100);

        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            Map<String,Object> finalMap = new HashMap<String,Object>();
            Map<String,String> postMap = SysUtils.apiCall(getApplicationContext(), finalMap);

            CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getServiceUrl("user/token_login"), postMap, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int error = jsonObject.getInt("error");
                        if(error > 0) {
                            //需要退出登录
                            LoginUtils.logout(getApplicationContext(), 0);
                        } else {
                            //重新写一下用户信息
                            LoginUtils.afterLogin(getApplicationContext(), jsonObject);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });

            RequestManager.addRequest(r, this);

            mHandler.postDelayed(this, NOTIFY_INTERVAL);
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(sendUpdatesToUI);

        super.onDestroy();
    }
}

