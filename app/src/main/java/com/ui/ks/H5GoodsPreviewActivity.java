package com.ui.ks;

import android.os.Bundle;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONObject;

public class H5GoodsPreviewActivity extends BaseActivity {
    private WebView webview_preview;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_goods_preview);
        SysUtils.setupUI(H5GoodsPreviewActivity.this,findViewById(R.id.activity_h5_goods_preview));
        initToolbar(this);

        webview_preview= (WebView) findViewById(R.id.webview_preview);
        getSellerUrl();
    }

    /**
     * 获取H5商品预览页
     */
    private void getSellerUrl() {

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("getSellerUrl"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("预览结果："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                       URL = ret.getString("data");
                        webview_preview.loadUrl(URL);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showError("网络不给力");
                hideLoading();
            }
        });

        executeRequest(r);
        showLoading(H5GoodsPreviewActivity.this);
    }
}
