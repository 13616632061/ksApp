package com.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/2.
 */

public class PreferencesService {

    private Context context;

    public PreferencesService(Context context) {
        this.context = context;
    }

    /**
     * 保存参数
     * @param num1   挂单自动打印小票次数
     * @param num2   结算自动打印小票次数
     */
    public void save_print_num(Integer num1, Integer num2) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("person_like_print_num", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("num1",num1);
        editor.putInt("num2",num2);
        editor.commit();
    }
    /**
     *获取挂单自动打印小票次数
     结算自动打印小票次数
     * @return
     */
    public Map<String, String> getPerferences_print_num() {
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("person_like_print_num", Context.MODE_PRIVATE);
        params.put("num1", String.valueOf(preferences.getInt("num1", 2)));
        params.put("num2", String.valueOf(preferences.getInt("num2", 1)));
        return params;
    }

    /**
     *
     * @param change 开单页商品样式
     */
    public void save_change(Integer change) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("person_like_change", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("change", change);
        editor.commit();
    }

    /**
     * 获取开单页商品样式参数
     * @return
     */
    public Map<String, String> getPerferences_change() {
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("person_like_change", Context.MODE_PRIVATE);
        params.put("change", String.valueOf(preferences.getInt("change", 1)));
        return params;
    }
    /**
     *
     * @param isprint 挂单自动打印
     */
    public void save_isprint(Integer isprint) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("person_like_isprint", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("isprint", isprint);
        editor.commit();
    }

    /**
     * 获取挂单自动打印参数
     * @return
     */
    public Map<String, String> getPerferences_isprint() {
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("person_like_isprint", Context.MODE_PRIVATE);
        params.put("isprint", String.valueOf(preferences.getInt("isprint", 1)));
        return params;
    }
    /**
     *
     * @param isprint 结算自动打印
     */
    public void save_isprint_success(Integer isprint) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("person_like_isprint_success", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("isprint_success", isprint);
        editor.commit();
    }

    /**
     * 获取结算自动打印参数
     * @return
     */
    public Map<String, String> getPerferences_isprint_success() {
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("person_like_isprint_success", Context.MODE_PRIVATE);
        params.put("isprint_success", String.valueOf(preferences.getInt("isprint_success", 1)));
        return params;
    }
    /**
     *
     * @param seller_name 商家名字
     */
    public void save_seller_name(String seller_name,String tel) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("person_like_seller_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("seller_name",seller_name);
        editor.putString("tel",tel);
        editor.commit();
    }

    /**
     * 商家名字
     * @return
     */
    public Map<String, String> getPerferences_seller_name() {
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("person_like_seller_name", Context.MODE_PRIVATE);
        params.put("seller_name", preferences.getString("seller_name",""));
        params.put("tel", preferences.getString("tel",""));
        return params;
    }
    /**
     *
     * @param order_id 扫描二维码的订单id
     */
    public void save_order_id(String order_id) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("paycode_order_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("order_id",order_id);
        editor.commit();
    }

    /**
     * 扫描二维码的订单id
     * @return
     */
    public Map<String, String> getPerferences_order_id() {
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("paycode_order_id", Context.MODE_PRIVATE);
        params.put("order_id", preferences.getString("order_id",""));
        return params;
    }
}
