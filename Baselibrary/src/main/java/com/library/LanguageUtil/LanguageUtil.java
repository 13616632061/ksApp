package com.library.LanguageUtil;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by lyf on 2020/6/1.
 */

public class LanguageUtil {

    /**
     * @Description:默认语言
     * @Author:lyf
     * @Date: 2020/6/1
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    static String defaultLanguage() {
        return PreferenceLanguageUtils.getInstance().getLanguageType();
    }

    /**
     * @Description:选择语言
     * @Author:lyf
     * @Date: 2020/6/1
     */
    public static Locale switchLanguage(int index) {
        Locale locale = null;
        switch (index) {
            case 0:
                locale = Locale.CHINA;
                break;
            case 1:
                locale = Locale.US;
                break;
        }
        return locale;
    }

    /**
     * @Description:设置语言（默认先从缓存中取，如果缓存没有则以手机系统设置为准）
     * @Author:lyf
     * @Date: 2020/6/1
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setLanguage(Context context) {
        String lan = PreferenceLanguageUtils.getInstance().getLanguageType();
        String country = PreferenceLanguageUtils.getInstance().getLanguageCountry();
        setLanguage(context,lan,country);

    }
    /**
    *@Description:设置语言
    *@Author:lyf
    *@Date: 2020/6/1
    */

    private static void setLanguage(Context context, String lan, String country) {
        Resources resources=context.getResources();
        Configuration configuration=resources.getConfiguration();
        DisplayMetrics dm=resources.getDisplayMetrics();

        configuration.locale=new Locale(lan,country);

        //缓存语言标记
        PreferenceLanguageUtils.getInstance().saveLanguageType(configuration.locale.getLanguage());
        PreferenceLanguageUtils.getInstance().saveLanguageCountry(configuration.locale.getCountry());

        resources.updateConfiguration(configuration,dm);
    }

    /**
    *@Description:设置语言
    *@Author:lyf
    *@Date: 2020/6/1
    */
    public static void setLanguage(Context context,Locale locale){
        Resources resources=context.getResources();
        Configuration configuration=resources.getConfiguration();

        //缓存语言标记
        PreferenceLanguageUtils.getInstance().saveLanguageType(locale.getLanguage());
        PreferenceLanguageUtils.getInstance().saveLanguageCountry(locale.getCountry());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        }else {
            configuration.locale=locale;
            DisplayMetrics dm=resources.getDisplayMetrics();
            resources.updateConfiguration(configuration,dm);
        }
    }

    /**
    *@Description:描述
    *@Author:lyf
    *@Date: 2020/6/1
    */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Context attachBaseContext(Context context, Locale locale){
       return creatConfigurationResources(context,locale);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Context creatConfigurationResources(Context context, Locale locale){
        Resources resources=context.getResources();
        Configuration configuration=resources.getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}
