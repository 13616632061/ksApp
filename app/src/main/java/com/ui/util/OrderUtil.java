package com.ui.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wjj on 16/1/5.
 */
public class OrderUtil {
    public static void cancel(Context ctx, String orderId) {
////        List<Object> paramList = new ArrayList<Object>();
////        paramList.add(orderId);
////        String postData = StoreUtils.postData(ctx, "order.close", paramList);
////
////        String sign = StoreUtils.getSign(postData);
////        Map<String,String> map = new HashMap<String,String>();
////        map.put("data", postData);
////        map.put("signature", sign);
////        map.put("version", Global.SCHOOL_API_VERSION);
////
////        final BaseActivity act = (BaseActivity) ctx;
////
////        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getStoreServiceUri(""), map, new Response.Listener<JSONObject>() {
////            @Override
////            public void onResponse(JSONObject jsonObject) {
////                act.hideLoading();
////
////                try {
////                    int code = jsonObject.getInt("code");
////
////                    if(code == 0) {
////                        JSONObject dataObject = jsonObject.getJSONObject("data");
////                        int error = dataObject.getInt("error");
////
////                        if (error == 0) {
////                            SysUtils.showSuccess("订单已取消");
////
////                            act.sendBroadcast(new Intent(Global.BROADCAST_REFRESH_ORDER_ACTION));
////                        } else {
////                            SysUtils.showError(dataObject.getString("message"));
////                        }
////                    }
////                } catch(Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError volleyError) {
////                act.hideLoading();
////
////                SysUtils.showNetworkError();
////            }
////        });
////
////        act.executeRequest(r);
////
////        act.showLoading(ctx, "请稍等......");
//    }
//
//    public static void success(Context ctx, String orderId) {
//        List<Object> paramList = new ArrayList<Object>();
//        paramList.add(orderId);
//        String postData = StoreUtils.postData(ctx, "order.success", paramList);
//
//        String sign = StoreUtils.getSign(postData);
//        Map<String,String> map = new HashMap<String,String>();
//        map.put("data", postData);
//        map.put("signature", sign);
//        map.put("version", Global.SCHOOL_API_VERSION);
//
//        final BaseActivity act = (BaseActivity) ctx;
//
//        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getStoreServiceUri(""), map, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                act.hideLoading();
//
//                try {
//                    int code = jsonObject.getInt("code");
//
//                    if(code == 0) {
//                        JSONObject dataObject = jsonObject.getJSONObject("data");
//                        int error = dataObject.getInt("error");
//
//                        if (error == 0) {
//                            SysUtils.showSuccess("已收货");
//
//                            act.sendBroadcast(new Intent(Global.BROADCAST_REFRESH_ORDER_ACTION));
//                        } else {
//                            SysUtils.showError(dataObject.getString("message"));
//                        }
//                    }
//                } catch(Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                act.hideLoading();
//
//                SysUtils.showNetworkError();
//            }
//        });
//
//        act.executeRequest(r);
//
//        act.showLoading(ctx, "请稍等......");
    }

    public static void getTongji(final Context ctx, final OrderTongjiListener listener) {
        if(LoginUtils.hasLogin()) {
            Map<String,Object> finalMap = new HashMap<String,Object>();
            Map<String,String> postMap = SysUtils.apiCall(ctx, finalMap);

            CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getServiceUrl("my/order/tongji"), postMap, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code = jsonObject.getInt("code");

                        if (code == 0) {
                            JSONObject tongjiObject = jsonObject.getJSONObject("tongji");

                            if(listener != null) {
                                listener.onFinish(
                                        tongjiObject.getInt("notpay"),
                                        tongjiObject.getInt("paid"),
                                        tongjiObject.getInt("send"),
                                        tongjiObject.getInt("success")
                                );
                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    SysUtils.showNetworkError();
                }
            });

            ((BaseActivity)ctx).executeRequest(r);
        } else {
            //未登录
            if(listener != null) {
                listener.onFinish(0, 0, 0, 0);
            }
        }
    }
}
