package com.ui.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class GeneralSwipeRefreshLayout extends SwipeRefreshLayout {
    private OnChildScrollUpListener mScrollListenerNeeded;

    public static interface OnChildScrollUpListener {
        public boolean canChildScrollUp();
    }

    public GeneralSwipeRefreshLayout(Context context) {
        super(context);
    }
    public GeneralSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Listener that controls if scrolling up is allowed to child views or not
     */
    public void setOnChildScrollUpListener(OnChildScrollUpListener listener) {
        mScrollListenerNeeded = listener;
    }

    @Override
    public boolean canChildScrollUp() {
        return mScrollListenerNeeded == null ? false : mScrollListenerNeeded.canChildScrollUp();
    }
}