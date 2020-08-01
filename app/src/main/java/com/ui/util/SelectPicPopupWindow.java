package com.ui.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ui.adapter.OrderTypeMenuAdapter;
import com.MyApplication.KsApplication;
import com.ui.ks.R;

/**
 * x拍照或者从相册中选图片的弹框
 * Created by Administrator on 2016/12/9.
 */

public class SelectPicPopupWindow extends PopupWindow {
    private Button btn_take_photo;
    private Button btn_pick_photo;
    private Button btn_cancel;
    private ListView lv_ordertype_menu;
    private SystemBarTintManager tintManager;
    private String[] datastr = new String[6];
    private String[] title_str = new String[]{"全部店面总额", "南山店", "福永店", "宝安店"};
    public OrderTypeMenuAdapter adapter;

    public SelectPicPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(context);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarAlpha(255);
            }

            final View view = View.inflate(context, R.layout.iv_popwindow, null);
            btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
            btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
            btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tintManager.setStatusBarTintEnabled(false);
                    dismiss();
                }
            });
            //设置按钮监听
            btn_take_photo.setOnClickListener(itemsOnClick);
            btn_pick_photo.setOnClickListener(itemsOnClick);
            //设置SelectPicPopupWindow的View
            this.setContentView(view);
            //设置弹窗的宽
            this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            //设置弹窗的高
            this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            //设置弹窗可点击
            this.setFocusable(true);
            //设置弹出的动画效果
            this.setAnimationStyle(R.style.AnimBottom);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable mColorDrawable = new ColorDrawable(0xb0000000);
            //设置弹框的背景
            this.setBackgroundDrawable(mColorDrawable);
            //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view.findViewById(R.id.btn_take_photo).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            tintManager.setStatusBarTintEnabled(false);
                            dismiss();
                        }
                    }
                    return true;
                }
            });

        }
    }


    public SelectPicPopupWindow(Activity context, final int type, AdapterView.OnItemClickListener itemsOnClick) {
        super(context);
        final View view = View.inflate(context, R.layout.iv_popwindow_ordertype, null);
        lv_ordertype_menu = (ListView) view.findViewById(R.id.lv_ordertype_menu);
        datastr[0] = context.getString(R.string.str320);
        datastr[1] = context.getString(R.string.to_store);
        datastr[2] = context.getString(R.string.shopping_mall);
        datastr[3] = context.getString(R.string.group_buying);
        datastr[4] = context.getString(R.string.distribution);
        datastr[5] = context.getString(R.string.str326);
        if (type == 2) {
            adapter = new OrderTypeMenuAdapter(context, title_str);
        } else {
            adapter = new OrderTypeMenuAdapter(context, datastr);
        }
        adapter.setSelectIndex(KsApplication.selectItem);
        adapter.setShowCheck(KsApplication.selectItemIsCheck);
        lv_ordertype_menu.setAdapter(adapter);

        //设置按钮监听
        lv_ordertype_menu.setOnItemClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置弹窗的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置弹窗的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置弹窗可点击
        this.setFocusable(true);
        //设置弹出的动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable mColorDrawable = new ColorDrawable(0xb0000000);
        //设置弹框的背景
        this.setBackgroundDrawable(mColorDrawable);
        //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.lv_ordertype_menu).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}
