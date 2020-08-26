package com.ui.ks.webView;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.R;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * 1.在要Activity中实例化WebView组件：WebView webView = new WebView(this);
 * 2.调用WebView的loadUrl()方法，设置WevView要显示的网页：
 * 互联网用：webView.loadUrl("http://www.***.com");
 * 本地文件用：webView.loadUrl(file:///android_asset/XX.html); 本地文件存放在：assets 文件中
 * 3.调用Activity的setContentView()方法来显示网页视图
 * 4.用WebView点链接看了很多页以后为了让WebView支持回退功能，需要覆盖覆盖Activity类的onKeyDown
 * ()方法，如果不做任何处理，点击系统回退剪键，整个浏览器会调用finish()而结束自身，而不是回退到上一页面
 * 5.需要在AndroidManifest.xml文件中添加权限，否则会出现Web page not available错误。
 * <uses-permission android:name="android.permission.INTERNET" />
 * 缺点：如果是载入的是普通网页，没有什么问题，但如果是html5，封装后，在android2.3以上才能正常访问，android2.2及以下，
 * SDK中的WebView还没完全支持HTML5
 * <p/>
 * 还可以直接载入html的字符串，如： String htmlString = "<h1>Title</h1>
 * <p>
 * This is HTML text<br />
 * <i>Formatted in italics</i><br />
 * Anothor Line
 * </p>
 * "; // 载入这个html页面 myWebView.loadData(htmlString, "text/html", "utf-8");
 */

public abstract class BaseHtmlActivity extends BaseActivity {

    private final static String TAG = BaseHtmlActivity.class.getSimpleName();
    @BindView(R.id.web_view)
    WebView webView;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage5;
    public static final int FILECHOOSER_RESULTCODE = 5173;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 5174;

    @Override
    public int getContentView() {
        return setContentView();
    }

    @Override
    protected void initView() {
        webView.clearCache(true);
        webView.clearHistory();
    }

    /**
     * 返回一个用于页面显示界面的布局id
     *
     * @return
     */
    public abstract int setContentView();

    /**
     * 初始化view
     */

    public void load(String html) {
        //加载需要显示的网页
        webView.loadUrl(html);
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setPluginState(WebSettings.PluginState.ON); //支持插件
        WebSettings webSettings = webView.getSettings();  //android 5.0以上
//        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        //Android webview 从Lollipop(5.0)开始webview默认不允许混合模式，https当中不能加载http资源，需要设置开启。
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        webSettings.setTextZoom(100);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setBuiltInZoomControls(true); //设置支持缩放
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setUseWideViewPort(false); //将图片调整到适合webview的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.supportMultipleWindows(); //多窗口
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setSupportMultipleWindows(true);

        //设置 应用 缓存目录
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath());
        //开启 DOM 存储功能
        webSettings.setDomStorageEnabled(true);
        //开启 数据库 存储功能
        webSettings.setDatabaseEnabled(true);
        //开启 应用缓存 功能
        webSettings.setAppCacheEnabled(true);


        // 在Android中点击一个链接，默认是调用应用程序来启动，因此WebView需要代为处理这个动作 通过WebViewClient
        // 设置WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                LogUtils.i(TAG + "webviewUrl" + "Client:  " + url);
                if (url == null) return false;
                try {
//                    url = java.net.URLDecoder.decode(url, "utf-8");
                    //处理网页无法加载问题
                    if (url.startsWith("weixin://") || url.startsWith("alipays://") ||
                            url.startsWith("mailto://") || url.startsWith("tel:") || url.startsWith("baidubox") ||
                            url.startsWith("mimarket://")
                        //其他自定义的scheme
                            ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }

                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;
                }

                //处理http和https开头的url
                if (url.endsWith(".apk")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else if (url.contains("wx.tenpay.com")) {//第三方支付平台商请求头 一般是对方固定
                    Map headers = new HashMap();
//                    headers.put("Referer", Constant.PUSH_TYPE.DUIBA_WXPAY_REFERER);
                    view.loadUrl(url, headers);
                } else {
                    view.loadUrl(url);
                }


                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) { //https
                handler.proceed(); //接受证书
            }

        });
        setWebChromeClient();
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.destroy();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }


    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webView.canGoBack()) {
                webView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            } else {
                finish();
            }
            webView.loadUrl("about:blank");
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            // 音量键的控制，调出控制台
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // 音量键的控制，调出控制台
        } else {
            finish();
        }

        return false;
    }

    /**
     * TODO 处理android系统webView调用H5文件上传JS无反应操作
     */
    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                this.openFileChooser(uploadMsg, "*/*");
            }

            // For Android >= 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType) {
                this.openFileChooser(uploadMsg, acceptType, null);
            }

            // For Android >= 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                if (mUploadMessage5 != null) {
                    mUploadMessage5.onReceiveValue(null);
                    mUploadMessage5 = null;
                }
                mUploadMessage5 = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent,
                            FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                } catch (ActivityNotFoundException e) {
                    mUploadMessage5 = null;
                    return false;
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtils.e(TAG, "title: " + title);
                LogUtils.e(TAG, "title: " + view.getTitle());
                LogUtils.e(TAG, "Url: " + view.getUrl());
                if (!TextUtils.isEmpty(title)) {
                    initTabTitle(title, "");
                }
            }
        });

    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessage5) {
                return;
            }
            mUploadMessage5.onReceiveValue(WebChromeClient.FileChooserParams
                    .parseResult(resultCode, intent));
            mUploadMessage5 = null;
        }
    }


}
