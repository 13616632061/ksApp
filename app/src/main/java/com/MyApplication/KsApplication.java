package com.MyApplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;

import com.didi.virtualapk.PluginManager;
import com.library.LanguageUtil.LanguageUtil;
import com.library.LanguageUtil.PreferenceLanguageUtils;
import com.library.app.LibAplication;
import com.ui.db.DBHelper;
import com.ui.global.Global;
import com.ui.util.SpeechUtilOffline;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.tencent.bugly.crashreport.CrashReport;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.util.List;

public class KsApplication extends LibAplication {
    private static SharedPreferences sp;
    private static Context sContext;
    private static boolean hasGetInviteInfo = false;
    public static String invite_reg_title, invite_wx_title, app_down_uri, invite_reg_resume, invite_wx_resume, invite_reg_picurl, invite_wx_picurl;
    private DBHelper sqlHelper;
    private static KsApplication mExamApplication;
    private File saveDir;

    public static boolean hasNewVersion = false;
    public static String newVersionName = "";
    public static int selectItem=0;//订单类型初始值
    public static boolean selectItemIsCheck=true;//订单类型是否被选中
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        //插件化
        PluginManager.getInstance(base).init();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //创建可以打印log的ImageLoaderConfiguration
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs()
                .memoryCache(new LruMemoryCache(2*1024*1024))//可以通过自己的内存缓存实现
                .memoryCacheSize(2*1024*1024)//内存缓存的最大值
                .memoryCacheSizePercentage(13)
                .build();

        //初始化ImageLoader
        ImageLoader.getInstance().init(configuration);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = getApplicationContext();
        //初始化语言播报
        SpeechUtilOffline tts= new SpeechUtilOffline(this);

        if (shouldInit()) {
            MiPushClient.registerPush(this, "2882303761517488439", "5751748881439");
        }
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.v("ks_xiaomi", content, t);
            }

            @Override
            public void log(String content) {
                Log.v("ks_xiaomi", content);
            }
        };
        Logger.setLogger(this, newLogger);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        Global.screenWidth = dm.widthPixels;
        Global.screenHeight = dm.heightPixels;
        Global.magicWidth = Global.screenWidth;
        Global.magicHeight = Global.screenWidth * 8 / 15;

        initImageLoader(sContext);
        mExamApplication = this;

        WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)) .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();

        //bugly初始化
//        CrashReport.initCrashReport(getApplicationContext(), "d8348de79c", false);
//        CrashReport.testJavaCrash();

    }

    public static KsApplication getApp() {
        return mExamApplication;
    }

    // 初始化ImageLoader
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB硬盘缓存
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        ImageLoader.getInstance().init(config.build());
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }

    public static SharedPreferences getConfig() {
        return sp;
    }

    public static void putString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String key, String value) {
        return sp.getString(key, value);
    }

    public static void putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean value) {
        return sp.getBoolean(key, value);
    }

    public static void putInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int value) {
        return sp.getInt(key, value);
    }

    public static void putFloat(String key, float value) {
        sp.edit().putFloat(key, value).commit();
    }

    public static float getFloat(String key, float value) {
        return sp.getFloat(key, value);
    }

    public static void putLong(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long value) {
        return sp.getLong(key, value);
    }

    public static boolean isHasGetInviteInfo() {
        return hasGetInviteInfo;
    }

    public static void setHasGetInviteInfo(boolean h) {
        hasGetInviteInfo = h;
    }

    public static String getInvite_reg_title() {
        return invite_reg_title;
    }


    public static void setInvite_reg_title(String i) {
        invite_reg_title = i;
    }

    public static String getInvite_wx_title() {
        return invite_wx_title;
    }

    public static void setInvite_wx_title(String i) {
        invite_wx_title = i;
    }


    public DBHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new DBHelper(mExamApplication);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (sqlHelper != null) {
            //尝试关闭sqlite连接
            sqlHelper.close();
        }

        super.onTerminate();
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
