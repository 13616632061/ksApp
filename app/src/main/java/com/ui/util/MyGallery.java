package com.ui.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class MyGallery extends Gallery {
    private Handler handler;
    private boolean scrollRight = true;

    public MyGallery(Context paramContext) {
    	super(paramContext);
    	handler = new Handler();
    	postDelayedScrollNext();
    }

    public MyGallery(Context paramContext, AttributeSet paramAttributeSet) {
    	super(paramContext, paramAttributeSet);
    	handler = new Handler();
    	postDelayedScrollNext();
    }

    public MyGallery(Context paramContext, AttributeSet paramAttributeSet,
	    int paramInt) {
    	super(paramContext, paramAttributeSet, paramInt);
    	handler = new Handler();
    	postDelayedScrollNext();
    }

    private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
	    MotionEvent paramMotionEvent2) {
    	float f2 = paramMotionEvent2.getX();
    	float f1 = paramMotionEvent1.getX();
    	if (f2 > f1)
    		return true;
    	return false;
    }
    
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);
        return true;
    }

    private void postDelayedScrollNext() {
        handler.postDelayed(new Runnable() {
            public void run() {
                postDelayedScrollNext();
                
                int position = getSelectedItemPosition();
                
                if (position >= (getCount() - 1)) {
                	scrollRight = false;
                } else if(position <= 0) {
                	scrollRight = true;
                }
                
                if (!scrollRight) {
                	//��
                	onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
                } else {
                	//��
                	onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                }
            }
        }, 3000);
    }
}