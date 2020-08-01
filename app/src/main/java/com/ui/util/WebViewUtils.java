package com.ui.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ui.entity.News;
import com.ui.global.Global;
import com.base.BaseActivity;
import com.ui.ks.PicViewActivity;
import com.ui.ks.ShareBaseActivity;

public class WebViewUtils {
    private Context context;
    private WebView webview;
    private String url;
    private String data;
    private Handler mHandler;
    private WebViewChangedListener listener = null;
    private boolean canNewWindow = false;
    private boolean isSuccess = false;
    private boolean isMini = false;
    private int type = 0;

    public WebViewUtils(Context context, WebView webview, String url) {
        init(context, webview, url, 0);
    }

    public WebViewUtils(Context context, WebView webview, String text, int type) {
        init(context, webview, text, type);
    }

    private void init(Context context, WebView webview, String text, int type) {
        this.context = context;
        this.webview = webview;

        this.type = type;
        if(type == 1) {
            //html字符串
            this.data = text;
        } else {
            //url
            this.url = text;
        }

        webview.getSettings().setUserAgentString(Global.UA);
        //取消长按调用系统复制
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mHandler = new Handler();
    }

    private void setWebCache() {
        if(SysUtils.isOnline(this.context, false)) {
            webview.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
        } else {
            webview.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ONLY );
        }

    }

    public interface JavascriptCallback {

    }

    public class OhoJavaScriptInterface implements JavascriptCallback {
        OhoJavaScriptInterface() {
        }

        @JavascriptInterface
        public void showError(String msg) {
            SysUtils.showError(msg);
        }

        @JavascriptInterface
        public void playPic(final String picList, final String offset) {
            mHandler.post(new Runnable() {
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", picList);
                    bundle.putInt("offset", Integer.valueOf(offset));

                    SysUtils.startAct(context, new PicViewActivity(), bundle);
                    ((BaseActivity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }

        @JavascriptInterface
        public void refresh() {
            mHandler.post(new Runnable() {
                public void run() {
                    setWebCache();
                    webview.loadUrl(url);
                }
            });
        }

        @JavascriptInterface
        public void setShareResume(final String resume) {
            mHandler.post(new Runnable() {
                public void run() {
                    ((ShareBaseActivity)context).setShareResume(resume);
                }
            });
        }

        @JavascriptInterface
        public void setCommentNum(final String commentNum) {
            mHandler.post(new Runnable() {
                public void run() {
                    if(listener != null) {
                        listener.setCommentNum(commentNum);
                    }
                }
            });
        }

        @JavascriptInterface
        public void setHits(final String hitsNum) {
            mHandler.post(new Runnable() {
                public void run() {
                    if(listener != null) {
                        listener.setHits(hitsNum);
                    }
                }
            });
        }


        @JavascriptInterface
        public void setNewsClick(final String tid, final String setlink, final String digest,
                                 final String subject, final String linkurl, final String pic_url, final String resume) {
            mHandler.post(new Runnable() {
                public void run() {
                    News bean = new News(Integer.parseInt(tid),
                                        Integer.parseInt(setlink),
                                        Integer.parseInt(digest),
                                        subject,
                                        linkurl,
                                        pic_url,
                                        resume);
                    SysUtils.newsClick(context, bean);
                }
            });
        }

        @JavascriptInterface
        public void dianzanReview(final String reviewId) {
            mHandler.post(new Runnable() {
                public void run() {
                    if(listener != null) {
                        listener.dianzanReview(reviewId);
                    }
                }
            });
        }
    }

    public void load() {
        try {
            WebSettings websettings = webview.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setPluginState(WebSettings.PluginState.ON);
//            websettings.setUseWideViewPort(true);
//            websettings.setLoadWithOverviewMode(true);
//            websettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                websettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            } else {
                websettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            }

            //缓存机制
            webview.getSettings().setAllowFileAccess( true );
            webview.getSettings().setAppCacheEnabled( true );

            setWebCache();

            //添加javascript接口
            final OhoJavaScriptInterface myJavaScriptInterface = new OhoJavaScriptInterface();
            webview.addJavascriptInterface(myJavaScriptInterface, "msjs");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                websettings.setAllowUniversalAccessFromFileURLs(true);
            }

            if(this.type == 1) {
                //string
                String html = "<!DOCTYPE html>";
                html += "<html>";
                html += "<head>";
                html += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
                html += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\" />";
                html += "<link href=\"file:///android_asset/www/css/weui.min.css\" rel=\"stylesheet\" />";
                html += "<link href=\"file:///android_asset/www/css/comm.css\" rel=\"stylesheet\" />";
                html += "<script type=\"text/javascript\" src=\"file:///android_asset/www/js/jquery-1.9.1.min.js\"></script>";
                html += "<script type=\"text/javascript\" src=\"file:///android_asset/www/js/comm.js\"></script>";
                html += "<script type='text/javascript'>";
                html += "var g_web_uri='" + Global.webUrl + "';";
                html += "</script>";
                html += "</head>";
                html += "<body>";
                html += data;
                html += "</body>";
                html += "</html>";

//                Log.v("yiyou", html);
//                webview.loadData(data, "text/html", "UTF-8");
                webview.loadDataWithBaseURL("blarg://ignored", html, "text/html", "UTF-8", "");
            } else {
                webview.loadUrl(url);
            }

            webview.requestFocusFromTouch();
            webview.requestFocus(View.FOCUS_DOWN);

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    if(isMini) {
                        view.loadUrl("file:///android_asset/www/error_mini.html");
                    } else {
                        view.loadUrl("file:///android_asset/www/error.html");
                    }

                    isSuccess = false;

                    if(listener != null) {
                        listener.onError();
                    }
                }

                @Override
                public void onPageStarted(WebView view, String u, Bitmap facIcon) {
                    super.onPageStarted(view, u, facIcon);

                    if(listener != null) {
                        listener.onStarted();
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("tel:")) {
                        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        context.startActivity(tel);
                        return true;
                    } else if (url.startsWith("mailto:")) {
//                        String body = "Enter your Question, Enquiry or Feedback below:\n\n";
//                        Intent mail = new Intent(Intent.ACTION_SEND);
//                        mail.setType("application/octet-stream");
//                        mail.putExtra(Intent.EXTRA_EMAIL, new String[]{"email address"});
//                        mail.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                        mail.putExtra(Intent.EXTRA_TEXT, body);
//                        context.startActivity(mail);
                        MailTo mt = MailTo.parse(url);
                        Intent i = newEmailIntent(context, mt.getTo(), "", "", "");
                        context.startActivity(i);
                        return true;
                    }

                    if(canNewWindow) {
                        if(listener != null) {
                            listener.onOverideUrlLoading(url);
                        }

                        view.loadUrl(url);

                        return super.shouldOverrideUrlLoading(view, url);
                    } else {
                        SysUtils.openUrl(context, url);
                        return true;
                    }
                }
            });

            webview.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    super.onProgressChanged(view, progress);

                    if(listener != null) {
                        listener.onProgressChanged(progress);
                    }
//
                    if(progress == 100) {
                        if(listener != null) {
                            listener.onProgressFinished(view.getTitle());
                        }

                        isSuccess = true;
                    }
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setChangedListener(WebViewChangedListener listener) {
        this.listener = listener;
    }

    public void setCanNewWindow(boolean canNewWindow) {
        this.canNewWindow = canNewWindow;
    }

    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    public void setIsMini(boolean isMini) {
        this.isMini = isMini;
    }

    private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
    }

    public void evalJs(String js) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.evaluateJavascript(js, null);
        } else {
            webview.loadUrl("javascript:" + js);
        }
    }
}
