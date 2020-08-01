package com.ui.ChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.ui.SafeEdit.DensityUtils;

/**
 * Created by user on 2016/10/19.
 */
public class IndexHorizontalScrollView extends HorizontalScrollView {

    private static final String TAG = "IndexHorizontal";
    private Paint textPaint;
    private Today24HourView today24HourView;
    private NearlySevenDayView nearlySevenDayView;
    private NearlyThirtyDayView nearlyThirtyDayView;

    public IndexHorizontalScrollView(Context context) {
        this(context, null);
    }

    public IndexHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(DensityUtils.sp2px(12));
        textPaint.setAntiAlias(true);
        textPaint.setColor(new Color().WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int offset = computeHorizontalScrollOffset();
        int maxOffset = computeHorizontalScrollRange() - DensityUtils.getScreenWidth(getContext());
        if(today24HourView != null){
            today24HourView.setScrollOffset(offset, maxOffset);
        }
        if(nearlySevenDayView != null){
            nearlySevenDayView.setScrollOffset(offset, maxOffset);
        }
        if(nearlyThirtyDayView != null){
            nearlyThirtyDayView.setScrollOffset(offset, maxOffset);
        }
    }

    public void setToday24HourView(Today24HourView today24HourView){
        this.today24HourView = today24HourView;
    }
    public void setNearlySevenDayView(NearlySevenDayView nearlySevenDayView){
        this.nearlySevenDayView = nearlySevenDayView;
    }
    public void setNearlyThirtyDayView(NearlyThirtyDayView nearlyThirtyDayView){
        this.nearlyThirtyDayView = nearlyThirtyDayView;
    }
}
