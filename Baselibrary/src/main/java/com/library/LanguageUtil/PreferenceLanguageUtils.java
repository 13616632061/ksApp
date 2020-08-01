package com.library.LanguageUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.library.app.LibAplication;

/**
 * @Description:语言
 * @Author:lyf
 * @Date: 2020/6/1
 */

public class PreferenceLanguageUtils {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static PreferenceLanguageUtils instance;
    private static String LANGUAGE = "set_language";
    public static String LANGUAGE_SHARE_NAME = "language_share_name";
    //语言类型
    private String OPS_DATA_LANGUAGE_TYPE = "ops_data_language_type";
    private String OPS_DATA_LANGUAGE_COUNTRY = "ops_data_language_country";

    /**
     * 获取配置的实例
     */

    public static PreferenceLanguageUtils getInstance() {
        if (instance == null) {
            instance = new PreferenceLanguageUtils();
        }
        return instance;
    }

    public void init() {
        preferences = LibAplication.getContext().getSharedPreferences(LANGUAGE_SHARE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 设置语言
     *
     * @param languageIndex
     */
    public void setLanguage(int languageIndex) {
        editor.putInt(LANGUAGE, languageIndex).commit();
    }

    /**
     * 获取app语言设置
     */
    public int getLanguage() {
        return preferences.getInt(LANGUAGE, 0);//默认设置为中文
    }
    /**
     *@Description:保存语言类型
     *@Author:lyf
     *@Date: 2020/6/1
     */
    public void saveLanguageType(String lan){
        editor.putString(OPS_DATA_LANGUAGE_TYPE,lan);
        editor.commit();
    }

    /**
    *@Description:获取语言类型
    *@Author:lyf
    *@Date: 2020/6/1
    */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getLanguageType(){

        String lan=preferences.getString(OPS_DATA_LANGUAGE_TYPE,"zh");
        if (lan.isEmpty()){
           Resources resources= LibAplication.getContext().getApplicationContext().getResources();
           Configuration configuration=resources.getConfiguration();
           lan=configuration.locale.getLanguage();

        }
        return lan;
    }
    /**
     *@Description:保存语言国家
     *@Author:lyf
     *@Date: 2020/6/1
     */
    public void saveLanguageCountry(String country){
        editor.putString(OPS_DATA_LANGUAGE_COUNTRY,country);
        editor.commit();
    }
    /**
     *@Description:获取国家语言
     *@Author:lyf
     *@Date: 2020/6/1
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getLanguageCountry(){

        String country=preferences.getString(OPS_DATA_LANGUAGE_COUNTRY,"");
        if (country.isEmpty()){
            Resources resources= LibAplication.getContext().getApplicationContext().getResources();
            Configuration configuration=resources.getConfiguration();
            country=configuration.locale.getCountry();

        }
        return country;
    }
}
