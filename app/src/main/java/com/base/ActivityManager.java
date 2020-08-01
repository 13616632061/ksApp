package com.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by lyf on 2020/6/1.
 */

public class ActivityManager {
    private  ActivityManager(){};
    private static class Instance{
        private static final ActivityManager ACTIVITY_MANAGER=new ActivityManager();
    }
    public static ActivityManager getInstance(){
        return Instance.ACTIVITY_MANAGER;
    }


    private Deque<Activity> mStack = new LinkedList<>();

    /**
     * @Description:添加Activity到堆栈
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public void addActivity(Activity activity) {
        if (!mStack.contains(activity)) {
            mStack.add(activity);
        }
    }

    /**
     * @Description:移除
     * @Author:lyf
     * @Date: 2020/6/1
     */

    public void removeActivity(Activity activity) {
        if (mStack.contains(activity))
            mStack.remove(activity);
    }

    /**
     * @Description:获取当前Activity(堆栈中最后一个压入的)
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public Activity currentActivity() {
        Activity activity = null;
        try {
            activity = mStack.peek();
        } catch (Exception e) {
        }
        return activity;
    }

    /**
     * @Description:结束指定的activity
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * @Description:结束指定类名的activity
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public void finishActivity(Class cls) {
        for (Activity activity : mStack) {
            if (activity.getClass() == cls) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * @Description:获取指定类名的activity
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public Activity findActivity(Class cls) {
        for (Activity activity : mStack) {
            if (activity.getClass() == cls) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束所有activity
     *
     * @Description:描述
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public void finishAllActivity() {
        while (!mStack.isEmpty()) {
            Activity activity = mStack.pop();
            activity.finish();
        }
        mStack.clear();
    }

    /**
     * @Description:finish除某一activity外，所有的avtivity
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public void finishAllActivityExceptOne(Class cls) {
        for (Activity activity : mStack) {
            if (activity.getClass() == cls) {
                continue;
            } else {
                Activity act = mStack.pop();
                act.finish();
            }
        }
    }

    /**
     * @Description:finish某一activity之上的activity
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public void finishActivityAbove(Class cls) {
        Iterator iterator = mStack.descendingIterator();
        Activity tempActivity = null;
        for (Activity activity : mStack) {
            tempActivity = activity;
            if (iterator.hasNext()) {
                tempActivity = (Activity) iterator.next();
                if (tempActivity.getClass() == cls) {
                    break;
                } else {
                    tempActivity.finish();
                }
            }
        }
    }

    /**
     * @Description:推出应用程序
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public void appExit(Context context) {
        try {

            finishAllActivity();
            @SuppressLint("ServiceCast") android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
