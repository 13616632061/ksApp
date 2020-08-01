package com.library.utils;

import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.library.R;
import com.library.app.LibAplication;

/**
 * @Description:自定义Toast
 * @Author:lyf
 * @Date: 2020/7/15
 */

public class CustomToastUtil {

    /**
     * @Description:默认Toast的msg 为黑色
     * @Author:lyf
     * @Date: 2020/7/15
     */
    public static void showShort(String msg) {
        ToastUtils.showShort(msg);
        ToastUtils.setMsgColor(LibAplication.getContext().getResources().getColor(R.color.blackText));
    }

    /**
     * @Description:自定义Toast的msg的颜色
     * @Author:lyf
     * @Date: 2020/7/15
     */
    public static void showShort(String msg, int color) {
        ToastUtils.showShort(msg);
        ToastUtils.setMsgColor(LibAplication.getContext().getResources().getColor(color));
    }
}
