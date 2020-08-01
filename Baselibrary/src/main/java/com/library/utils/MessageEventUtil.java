package com.library.utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:EventBus工具类
 * @Author:lyf
 * @Date: 2020/7/28
 */

public class MessageEventUtil {

    public static Map<String, Object> getStringMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}

