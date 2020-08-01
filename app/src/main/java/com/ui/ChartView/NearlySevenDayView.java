package com.ui.ChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.ui.SafeEdit.DensityUtils;
import com.ui.entity.HourItem;
import com.ui.ks.R;
import com.ui.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/10/19.
 */
public class NearlySevenDayView extends View {
    private static final String TAG = "Today24HourView";
    private static final int ITEM_WIDTH = 250; //每个Item的宽度
    private static final int MARGIN_LEFT_ITEM = 15; //左边预留宽度
    private static final int MARGIN_RIGHT_ITEM = 100; //右边预留宽度

    private static final int windyBoxAlpha = 80;
    private static final int windyBoxMaxHeight = 80;
    private static final int windyBoxMinHeight = 20;
    private static final int windyBoxSubHight = windyBoxMaxHeight - windyBoxMinHeight;
    private static final int bottomTextHeight = 60;

    private int mHeight, mWidth;
    private int tempBaseTop;  //温度折线的上边Y坐标
    private int tempBaseBottom; //温度折线的下边Y坐标
    private Paint bitmapPaint, windyBoxPaint, linePaint, pointPaint, dashLinePaint;
    private TextPaint textPaint;

    private List<HourItem> listItems;
    private int maxScrollOffset = 0;//滚动条最长滚动距离
    private int scrollOffset = 0; //滚动条偏移量
    private int currentItemIndex = 0; //当前滚动的位置所对应的item下标
    private int currentWeatherRes = -1;

    private double maxTemp= 2000;
    private int minTemp =0;
    private int maxWindy = 5;
    private int minWindy = 2;
    private  ArrayList<String> TEMP;
//    private  int TEMP[] ={0} ;
//    private static int[]  TEMP=MainActivity.TEMP1;;
    private static  int ITEM_SIZE=17;  //24小时

//    private static final int WINDY[] = {2, 3, 3,
//            3, 4, 4, 4, 4,
//            2, 2, 2, 3, 3,
//            3, 5, 5, 5};
//    private static final int WEATHER_RES[] ={R.mipmap.w0, R.mipmap.w1, R.mipmap.w3, -1, -1
//            ,R.mipmap.w5, R.mipmap.w7, R.mipmap.w9, -1, -1
//            ,-1, R.mipmap.w10, R.mipmap.w15, -1, -1
//            ,-1, -1, -1, -1, -1
//            ,R.mipmap.w18, -1, -1, R.mipmap.w19};
    private boolean salenum=false;
    private boolean salemoney=false;
    private boolean todaytype=false;
    public  String  getMax(ArrayList<String>  arr)
    {
        String max = arr.get(0);
        for(int x=1; x<arr.size(); x++)
        {
            if(Double.parseDouble(arr.get(x))>Double.parseDouble(max))
                max =arr.get(x);
        }
        return max;
    }
    //销售量
    public  ArrayList<String> getdataSevenDay_salenum( ArrayList<String> data){
        TEMP=null;
        TEMP=data;
        if(data.size()>0) {
            maxTemp = Double.parseDouble(getMax(data))+100;
        }
        minTemp =0;
        ITEM_SIZE=7;
        salenum=true;
        todaytype=true;
        init();
        return TEMP;
    }
    //销售额
    public  ArrayList<String> getdataSevenDay_salemoney( ArrayList<String> data){
        TEMP=null;
        TEMP=data;
        if(data.size()>0) {
            maxTemp = Double.parseDouble(getMax(data))+2000;
        }
        minTemp =0;
        ITEM_SIZE=7;
        salemoney=true;
        todaytype=true;
        init();
        return TEMP;
    }
    //利润
    public  ArrayList<String> getdataSevenDay_saleprofit( ArrayList<String> data){
        TEMP=null;
        TEMP=data;
        if(data.size()>0) {
            maxTemp = Double.parseDouble(getMax(data))+2000;
        }
        minTemp =0;
        ITEM_SIZE=7;
        salemoney=true;
        todaytype=true;
        init();
        return TEMP;
    }

    public NearlySevenDayView(Context context) {
        this(context, null);
    }
    public NearlySevenDayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public NearlySevenDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    private void init() {
            mWidth= MARGIN_LEFT_ITEM + MARGIN_RIGHT_ITEM + (ITEM_SIZE) * ITEM_WIDTH;
        mHeight = 430; //暂时先写死
        tempBaseTop = (430 - bottomTextHeight)/4;
        tempBaseBottom = (430 - bottomTextHeight);

        initHourItems();
        initPaint();
    }

    private void initPaint() {
        pointPaint = new Paint();
        pointPaint.setColor(new Color().WHITE);
        pointPaint.setAntiAlias(true);
        pointPaint.setTextSize(8);

        linePaint = new Paint();
        linePaint.setColor(new Color().WHITE);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);

        dashLinePaint = new Paint();
        dashLinePaint.setColor(new Color().WHITE);
        PathEffect effect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        dashLinePaint.setPathEffect(effect);
        dashLinePaint.setStrokeWidth(3);
        dashLinePaint.setAntiAlias(true);
        dashLinePaint.setStyle(Paint.Style.STROKE);

        windyBoxPaint = new Paint();
        windyBoxPaint.setTextSize(1);
        windyBoxPaint.setColor(new Color().WHITE);
        windyBoxPaint.setAlpha(windyBoxAlpha);
        windyBoxPaint.setAntiAlias(true);

            textPaint = new TextPaint();
            textPaint.setTextSize(DensityUtils.sp2px(12));
            textPaint.setColor(new Color().WHITE);
            textPaint.setAntiAlias(true);

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
    }

    //简单初始化下，后续改为由外部传入
    private void initHourItems(){
        listItems = new ArrayList<>();
        for(int i=0; i<ITEM_SIZE; i++){
            String time = DateUtils.getNearlyDateYMD(DateUtils.getCurDateYMD(),i);

            int left =MARGIN_LEFT_ITEM  +  i * ITEM_WIDTH;
            int right = left + ITEM_WIDTH - 1;
//            int top = (int)(mHeight -bottomTextHeight +
//                    (maxWindy - WINDY[i])*1.0/(maxWindy - minWindy)*windyBoxSubHight
//                    - windyBoxMaxHeight);
            int bottom =  mHeight - bottomTextHeight;
            Rect rect = new Rect(left, 0, right, bottom);
            if(i<TEMP.size()) {
                Point point = calculateTempPoint(left, right, Double.parseDouble(TEMP.get(i)));

                HourItem hourItem = new HourItem();
                hourItem.windyBoxRect = rect;
                hourItem.time = time;
//            hourItem.windy = WINDY[i];
                hourItem.temperature = Double.parseDouble(TEMP.get(i));
                hourItem.tempPoint = point;
//            hourItem.res = WEATHER_RES[i];
                listItems.add(hourItem);
            }
    }
    }
    private Point calculateTempPoint(int left, int right, double temp){
        double minHeight = tempBaseTop;
        double maxHeight = tempBaseBottom;
        double tempY = maxHeight - (temp - minTemp)* 1.0/(maxTemp - minTemp) * (maxHeight - minHeight);
        Point point = new Point((left + right)/2, (int)tempY);
        return point;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0; i<listItems.size(); i++){
            Rect rect = listItems.get(i).windyBoxRect;
            Point point = listItems.get(i).tempPoint;
//            //画风力的box和提示文字
//            onDrawBox(canvas, rect, i);
            //画温度的点
            onDrawTemp(canvas, i);
//            //画表示天气图片
//            if(listItems.get(i).res != -1 && i != currentItemIndex){
//                Drawable drawable = ContextCompat.getDrawable(getContext(), listItems.get(i).res);
//                drawable.setBounds(point.x - DisplayUtil.dip2px(getContext(), 10),
//                        point.y - DisplayUtil.dip2px(getContext(), 25),
//                        point.x + DisplayUtil.dip2px(getContext(), 10),
//                        point.y - DisplayUtil.dip2px(getContext(), 5));
//                drawable.draw(canvas);
//            }
            onDrawLine(canvas, i);
            onDrawText(canvas, i);
        }
        //底部水平的白线
        linePaint.setColor(new Color().WHITE);
        canvas.drawLine(0, mHeight - bottomTextHeight, mWidth, mHeight - bottomTextHeight, linePaint);
        //中间温度的虚线
//        Path path1 = new Path();
//        path1.moveTo(MARGIN_LEFT_ITEM, tempBaseTop);
//        path1.quadTo(mWidth - MARGIN_RIGHT_ITEM, tempBaseTop, mWidth - MARGIN_RIGHT_ITEM, tempBaseTop);
//        canvas.drawPath(path1, dashLinePaint);
//        Path path2 = new Path();
//        path2.moveTo(MARGIN_LEFT_ITEM, tempBaseBottom);
//        path2.quadTo(mWidth - MARGIN_RIGHT_ITEM, tempBaseBottom, mWidth - MARGIN_RIGHT_ITEM, tempBaseBottom);
//        canvas.drawPath(path2, dashLinePaint);

    }
    private void onDrawTemp(Canvas canvas, int i) {
        HourItem item = null;
        Point point = null;
        item = listItems.get(i);
        point= item.tempPoint;
        canvas.drawCircle(point.x, point.y, 10, pointPaint);

        if (currentItemIndex == i) {
            if (i < listItems.size()) {

                //计算提示文字的运动轨迹
                int Y = getTempBarY();
                //画出背景图片
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.hour_24_float);
                drawable.setBounds(getScrollBarX(),
                        Y - DensityUtils.sp2px(24),
                        getScrollBarX() + ITEM_WIDTH,
                        Y - DensityUtils.sp2px(4));
                drawable.draw(canvas);
                //画天气
                int res;
                res = findCurrentRes(i);
                if (res != -1) {
                    Drawable drawTemp = ContextCompat.getDrawable(getContext(), res);
                    drawTemp.setBounds(getScrollBarX() + ITEM_WIDTH / 2 + (ITEM_WIDTH / 2 - DensityUtils.sp2px(18)) / 2,
                            Y - DensityUtils.sp2px(23),
                            getScrollBarX() + ITEM_WIDTH - (ITEM_WIDTH / 2 - DensityUtils.sp2px(18)) / 2,
                            Y - DensityUtils.sp2px(5));
                    drawTemp.draw(canvas);

                }
                //画出温度提示
                int offset = ITEM_WIDTH / 2;
                if (res == -1)
                    offset = ITEM_WIDTH;
                Rect targetRect = new Rect(getScrollBarX(), Y - DensityUtils.sp2px(24)
                        , getScrollBarX() + offset, Y - DensityUtils.sp2px(4));
                Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
                int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                textPaint.setTextAlign(Paint.Align.CENTER);
                if (salenum) {
                    canvas.drawText("" + item.temperature, targetRect.centerX(), baseline, textPaint);
                } else if (salemoney) {
                    canvas.drawText("￥" + item.temperature, targetRect.centerX(), baseline, textPaint);
                }
            }
        }
    }
    private int findCurrentRes(int i) {
        if(listItems.get(i).res != -1)
            return listItems.get(i).res;
        for(int k=i; k>=0; k--){
            if(listItems.get(k).res != -1)
                return listItems.get(k).res;
        }
        return -1;
    }

    //画底部风力的BOX
    private void onDrawBox(Canvas canvas, Rect rect, int i) {
        // 新建一个矩形
        RectF boxRect = new RectF(rect);
        HourItem item = null;
           item= listItems.get(i);
        if(i == currentItemIndex) {
            windyBoxPaint.setAlpha(255);
            canvas.drawRoundRect(boxRect, 4, 4, windyBoxPaint);
            //画出box上面的风力提示文字
            Rect targetRect = new Rect(getScrollBarX(), rect.top - DensityUtils.sp2px(20)
                    , getScrollBarX() + ITEM_WIDTH, rect.top - DensityUtils.sp2px(0));
                Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
                int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("风力" + item.windy + "级", targetRect.centerX(), baseline, textPaint);
        } else {
            windyBoxPaint.setAlpha(windyBoxAlpha);
            canvas.drawRoundRect(boxRect, 4, 4, windyBoxPaint);
        }
    }

    //温度的折线,为了折线比较平滑，做了贝塞尔曲线
    private void onDrawLine(Canvas canvas, int i) {
        linePaint.setColor(new Color().YELLOW);
        linePaint.setStrokeWidth(3);
            Point point = listItems.get(i).tempPoint;
            if(i != 0){
                Point pointPre = listItems.get(i-1).tempPoint;
                Path path = new Path();
                path.moveTo(pointPre.x, pointPre.y);
                if(i % 2 == 0)
                    path.cubicTo((pointPre.x+point.x)/2, (pointPre.y+point.y)/2-7, (pointPre.x+point.x)/2, (pointPre.y+point.y)/2+7, point.x, point.y);
                else
                    path.cubicTo((pointPre.x+point.x)/2, (pointPre.y+point.y)/2+7, (pointPre.x+point.x)/2, (pointPre.y+point.y)/2-7, point.x, point.y);
                canvas.drawPath(path, linePaint);
        }
    }

    //绘制底部时间
    private void onDrawText(Canvas canvas, int i) {
        //此处的计算是为了文字能够居中
        Rect rect = listItems.get(i).windyBoxRect;
        Rect targetRect = new Rect(rect.left, rect.bottom, rect.right, rect.bottom + bottomTextHeight);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);

        String text = listItems.get(i).time;
        canvas.drawText(text, targetRect.centerX(), baseline, textPaint);
    }

    public void drawLeftTempText(Canvas canvas, int offset, TextPaint textPaint){
        //画最左侧的文字
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(maxTemp + "°", DensityUtils.sp2px(6) + offset, tempBaseTop, textPaint);
        canvas.drawText(minTemp + "°", DensityUtils.sp2px(6) + offset, tempBaseBottom, textPaint);
    }

    //设置scrollerView的滚动条的位置，通过位置计算当前的时段
    public void setScrollOffset(int offset, int maxScrollOffset){
        this.maxScrollOffset = maxScrollOffset;
        scrollOffset = offset;
        int index = calculateItemIndex(offset);
        currentItemIndex = index;
        invalidate();
    }

    //通过滚动条偏移量计算当前选择的时刻
    private int calculateItemIndex(int offset){
//        Log.d(TAG, "maxScrollOffset = " + maxScrollOffset + "  scrollOffset = " + scrollOffset);
        int x = getScrollBarX();
        int sum = MARGIN_LEFT_ITEM  - ITEM_WIDTH/2;
            for(int i=0; i<ITEM_SIZE; i++){
                sum += ITEM_WIDTH;
                if(x < sum)
                    return i;
            }
            return ITEM_SIZE - 1;
    }

    private int getScrollBarX(){
            int x = (ITEM_SIZE - 1) * ITEM_WIDTH * scrollOffset/ maxScrollOffset;
            x= x + MARGIN_LEFT_ITEM;
            return x;
    }

    //计算温度提示文字的运动轨迹
    private int getTempBarY(){
        int x = getScrollBarX();
        int sum = MARGIN_LEFT_ITEM ;
        Point startPoint = null, endPoint;
        int i;
        for(i=0; i<ITEM_SIZE; i++){
            if (i < listItems.size()) {
                sum += ITEM_WIDTH;
                if (x < sum) {
                    startPoint = listItems.get(i).tempPoint;
                    break;
                }
            }
        }
        int y = 0;
        if(i+1 >= ITEM_SIZE || startPoint == null) {

            if (i < listItems.size()) {
                return listItems.get(ITEM_SIZE - 1).tempPoint.y;
            }
        }
        if (i< listItems.size()) {

            endPoint = listItems.get(i).tempPoint;

            Rect rect = listItems.get(i).windyBoxRect;
            y = (int)(startPoint.y + (x - rect.left)*1.0/ITEM_WIDTH * (endPoint.y - startPoint.y));
        }
        return y;
    }

}
