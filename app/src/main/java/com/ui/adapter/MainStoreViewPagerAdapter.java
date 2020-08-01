package com.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 广告轮播适配器
 * Created by Administrator on 2017/1/15.
 */

public class MainStoreViewPagerAdapter extends PagerAdapter{
    private Context context;
    private ArrayList<ImageView> imageView_list;
    private Handler handler;

    public MainStoreViewPagerAdapter(Context context, ArrayList<ImageView> imageView_list,Handler handler) {
        this.context = context;
        this.imageView_list = imageView_list;
        this.handler= handler;
    }

    @Override
    public int getCount() {
        if(imageView_list.size()>1){
            return Integer.MAX_VALUE;
        }
        return  imageView_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);//无限循环左右是要删除，否则向左滑没有图片
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 设置图片的位置
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT );
//        layoutParams.height = 200;// 设置图片的高度
//        layoutParams.width = 800; // 设置图片的宽度


        int realposition=position%imageView_list.size();//无限滑动

        ImageView imageView=new ImageView(context);
            imageView=imageView_list.get(realposition);
        try {
            container.addView(imageView);
        }catch (Exception e) {

        }
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //按下
                            handler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //滑动
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //取消
//                        handler.removeCallbacksAndMessages(null);
//                        handler.sendEmptyMessageDelayed(0,3000);
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起
                        handler.sendEmptyMessageDelayed(0,3000);
                        break;
                }
                return true;
            }
        });
        return imageView;
    }
}
