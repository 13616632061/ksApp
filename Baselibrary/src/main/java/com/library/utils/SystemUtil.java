package com.library.utils;

/**
 * Created by lyf on 2020/8/15.
 */

public class SystemUtil {
    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
}
