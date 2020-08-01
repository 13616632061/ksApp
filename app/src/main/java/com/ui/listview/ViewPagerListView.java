package com.ui.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

public class ViewPagerListView extends ListView {
	private Context context;

	private final int DIRECTION_VERTICAL = 0;
	private final int DIRECTION_HORIZONTAL = 1;
	private final int DIRECTION_NO_VALUE = -1;

	private int mTouchSlop;
	private int mGestureDirection;

	private float mDistanceX;
	private float mDistanceY;
	private float mLastX;
	private float mLastY;

	public ViewPagerListView(Context context) {
		super(context);

		this.context = context;
		init();
	}

	public ViewPagerListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		init();
	}

	public ViewPagerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;
		init();
	}

	private void init() {
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDistanceY = mDistanceX = 0f;
				mLastX = ev.getX();
				mLastY = ev.getY();
				mGestureDirection = DIRECTION_NO_VALUE;
				break;
			case MotionEvent.ACTION_MOVE:
				final float curX = ev.getX();
				final float curY = ev.getY();
				mDistanceX += Math.abs(curX - mLastX);
				mDistanceY += Math.abs(curY - mLastY);
				mLastX = curX;
				mLastY = curY;
				break;
		}

		return super.onInterceptTouchEvent(ev) && shouldIntercept();
	}


	private boolean shouldIntercept(){
		if((mDistanceY > mTouchSlop || mDistanceX > mTouchSlop) && mGestureDirection == DIRECTION_NO_VALUE){
			if(Math.abs(mDistanceY) > Math.abs(mDistanceX)){
				mGestureDirection = DIRECTION_VERTICAL;
			}
			else{
				mGestureDirection = DIRECTION_HORIZONTAL;
			}
		}

		if(mGestureDirection == DIRECTION_VERTICAL){
			return true;
		}
		else{
			return false;
		}
	}
}
