package com.library.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.library.LanguageUtil.LanguageUtil;
import com.library.LanguageUtil.PreferenceLanguageUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

/**
 * Created by Administrator on 2019/4/24.
 */

public class LibAplication extends Application {

    private RefWatcher refWatcher;
    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文

    @Override
    public void onCreate() {
        super.onCreate();
        //对全局属性赋值
        mContext = getApplicationContext();

        refWatcher = setupLeakCanary();
        //初始化工具类
        Utils.init(this);
        //初始化路由
        ARouter.openDebug();
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        //初始化语言设置
        PreferenceLanguageUtils.getInstance().init();
        setLanguage();
        //初始化zxing
        ZXingLibrary.initDisplayOpinion(this);

               /*=================litepal数据库=====================*/
        LitePal.initialize(this);
        //获取到SQLiteDatabase的实例，创建数据库表
        SQLiteDatabase db = LitePal.getDatabase();
        dbregisterDatabaseListener();
        //360插件初始化
//        RePlugin.App.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

//        RePlugin.App.attachBaseContext(this);
    }


    /**
     * @Description:语言设置
     * @Author:lyf
     * @Date: 2020/7/12
     */
    private void setLanguage() {
        int languageIndex = PreferenceLanguageUtils.getInstance().getLanguage();
        LanguageUtil.setLanguage(this, LanguageUtil.switchLanguage(languageIndex));
    }

    /**
     * 初始化内存监测工具 LeakCanary
     *
     * @return
     */
    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        LibAplication leakApplication = (LibAplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * @Description:监听数据库的创建和升级
     * @Author:lyf
     * @Date: 2020/8/1
     */
    private void dbregisterDatabaseListener() {
        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                LogUtils.i("数据库的创建");
            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {
                LogUtils.i("数据库的升级 oldVersion：  " + oldVersion + " newVersion: " + newVersion);
            }
        });
    }
}
