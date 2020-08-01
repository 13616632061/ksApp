package com.ui.ks;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.BaseActivity;

/**
 * Created by Administrator on 2020/3/4.
 */

public class Webpage_Activity extends BaseActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);
        initToolbar(this);

        initView();

    }

    private void initView() {
        webview= (WebView) findViewById(R.id.webview);
        webview.loadUrl("http://new.czxshop.com/wap/cash-login.html");
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }
}
