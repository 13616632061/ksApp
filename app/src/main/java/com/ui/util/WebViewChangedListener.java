package com.ui.util;

public interface WebViewChangedListener {
    void onStarted();
    void onOverideUrlLoading(String url);
    void onProgressChanged(int progress);
    void onProgressFinished(String title);
    void onError();
    void setCommentNum(String commentNum);
    void setHits(String hitsNum);
    void dianzanReview(String reviewId);
}
