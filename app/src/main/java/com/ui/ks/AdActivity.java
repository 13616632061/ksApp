package com.ui.ks;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.ui.util.SysUtils;
import com.ui.util.WebViewChangedListener;
import com.ui.util.WebViewUtils;
import com.ui.widget.observablescrollview.ObservableScrollViewCallbacks;
import com.ui.widget.observablescrollview.ObservableWebView;
import com.ui.widget.observablescrollview.ScrollState;

public class AdActivity extends ShareBaseActivity implements ObservableScrollViewCallbacks {
    String title = "";
    String url = "";
    private ObservableWebView webview;
    private NumberProgressBar loadingBar;
    FrameLayout main;
    private WebViewUtils webViewUtils;
    private boolean disableNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        initToolbar(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("url")) {
                url = bundle.getString("url");
            }

            if (bundle.containsKey("pic_url")) {
                sharePicUrl = SysUtils.getThumbUrl(bundle.getString("pic_url"));
            }

            if (bundle.containsKey("title")) {
                title = bundle.getString("title");
            }

            if (bundle.containsKey("resume")) {
                shareResume = bundle.getString("resume");
            }

//            if (bundle.containsKey("disableNav")) {
//                disableNav = bundle.getBoolean("disableNav");
//            }
            disableNav = true;
        }

        setToolbarTitle(title);

        main = (FrameLayout) findViewById(R.id.main);
        webview = (ObservableWebView) findViewById(R.id.web);
        webview.setScrollViewCallbacks(this);
        loadingBar = (NumberProgressBar) findViewById(R.id.news_content_progressBar);

        initView();
    }

    private void initView() {
        webViewUtils = new WebViewUtils(this, webview, url);
        webViewUtils.setCanNewWindow(false);
        webViewUtils.setChangedListener(new WebViewChangedListener() {
            @Override
            public void onStarted() {
                YoYo.with(Techniques.FadeIn).playOn(loadingBar);
            }

            @Override
            public void onError() {
                invalidateOptionsMenu();
            }

            @Override
            public void onOverideUrlLoading(String url) {
                shareUrl = url;
            }

            @Override
            public void onProgressChanged(int progress) {
                loadingBar.setProgress(progress);
            }

            @Override
            public void onProgressFinished(String title) {
                shareUrl = url;
                shareTitle = title;
//                AdActivity.this.setToolbarTitle(shareTitle);

                loadingBar.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public void setCommentNum(String commentNum) {

            }

            @Override
            public void setHits(String hitsNum) {

            }

            @Override
            public void dianzanReview(String reviewId) {

            }
        });

        webViewUtils.load();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //处理返回键
            if(webview.canGoBack()) {
                webview.goBack();
                return true;
            } else {
                onBackPressed();
            }
        }

        return super.onKeyDown(keyCode, event);
        //return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(webViewUtils.getIsSuccess()) {
//            getMenuInflater().inflate(R.menu.menu_ad, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.menu_share) {
//            doShare(this);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        Log.e("DEBUG", "onUpOrCancelMotionEvent: " + scrollState);
        if(disableNav) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(toolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(toolbar) == -toolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-toolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(toolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(toolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(toolbar, translationY);
                ViewHelper.setTranslationY((View) webview, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) main.getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) webview).requestLayout();
            }
        });
        animator.start();
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }
}
