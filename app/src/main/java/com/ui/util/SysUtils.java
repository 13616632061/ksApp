package com.ui.util;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.alibaba.fastjson.JSON;
import com.material.widget.PaperButton;
import com.ui.db.DBHelper;
import com.ui.entity.News;
import com.ui.entity.NewsCat;
import com.ui.entity.Order;
import com.ui.global.Global;
import com.MyApplication.KsApplication;
import com.ui.ks.MainActivity;
import com.ui.ks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.thefinestartist.finestwebview.FinestWebView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import com.xuebao.exam.AdActivity;
//import com.xuebao.exam.GoodsActivity;
//import com.xuebao.exam.MainActivity;
//import com.xuebao.exam.NewsDetailActivity;
//import com.xuebao.exam.SchoolDetailActivity;
//import com.xuebao.exam.ZhuantiActivity;
//import com.xuebao.global.Global;
//import com.xuebao.exam.ExamApplication;

//import com.oho.rugao.LoginActivity;

public class SysUtils {
    public static double getAppVersion(Context context) {
        double versionName = 0;
        String appName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = Double.parseDouble(pi.versionName);

            appName = pm.getApplicationLabel(pi.applicationInfo).toString();

            if (versionName <= 0) {
                return 0;
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        String appName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            appName = pm.getApplicationLabel(pi.applicationInfo).toString();

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static String getAppName(Context context) {
        String appName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);

            appName = pm.getApplicationLabel(pi.applicationInfo).toString();

            if (appName == null || appName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return appName;
    }

    public static void showError(String msg) {
        Toast.makeText(KsApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSuccess( String msg) {
        Toast.makeText(KsApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean hasSso() {
        String sso_uid = KsApplication.getString("sso_uid", "");
        int sso_type = KsApplication.getInt("sso_type", 0);

        if(sso_type > 0 && !StringUtils.isEmpty(sso_uid)) {
            return true;
        } else {
            return false;
        }
    }

    public static void showNetworkError() {
        showError("网络不给力");
    }

    public static boolean isOnline(Context ctx) {
        return SysUtils.isOnline(ctx, true);
    }

    public static boolean isOnline(Context ctx, boolean showError) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo i = cm.getActiveNetworkInfo();
        if ((i == null) || (!i.isConnected())) {
            if(showError) {
                SysUtils.showNetworkError();
            }

            return false;
        }

        return true;
    }

    /**
     * 判断wifi网络是否可用
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
            return false;
        }

        return false;
    }

    /**
     * 判断移动网络是否可用
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 得到网络类型
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    //隐藏键盘
    public static void hideSoftKeyboard(Context ctx, EditText commentText) {
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
    }

    //显示键盘
    public static void showSoftKeyboard(Context ctx, EditText commentText) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(commentText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    // 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static String GetNetIp()
    {
        URL infoUrl = null;
        InputStream inStream = null;

        try
        {
            infoUrl = new URL("http://ip.taobao.com/service/getIpInfo2.php?ip=myip");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection)connection;
            //超时设置，防止僵死
            httpConnection.setConnectTimeout(30000);
            httpConnection.setReadTimeout(30000);
            int responseCode = httpConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();

                JSONObject jsonObject = new JSONObject(strber.toString());
                String code = jsonObject.getString("code");
                if (code.equals("0"))
                {
                    JSONObject data = jsonObject.getJSONObject("data");
                    return data.getString("ip");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void startAct(Context ctx, Activity toact, Bundle bundle, boolean isResult, int flag) {
        Intent i = new Intent(ctx, toact.getClass());

        if(bundle != null) {
            i.putExtras(bundle);
        }

        if(flag > 0) {
            i.setFlags(flag);
        }

        if(isResult) {
            ((Activity) ctx).startActivityForResult(i, 1);
        } else {
            ctx.startActivity(i);
        }

    }


    public static void startAct(Context ctx, Activity toact, Bundle bundle, boolean isResult) {
        startAct(ctx, toact, bundle, isResult, 0);
    }

    public static void startAct(Context ctx, Activity toact, Bundle bundle) {
        startAct(ctx, toact, bundle, false);
    }

    public static void startAct(Context ctx, Activity toact) {
        startAct(ctx, toact, null);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    static int clearCacheFolder(final File dir) {
        int deletedFiles = 0;
        if (dir!= null && dir.isDirectory()) {
            try {
                for (File child:dir.listFiles()) {

                    //first delete subdirectories recursively
                    if (child.isDirectory()) {
                        deletedFiles += SysUtils.clearCacheFolder(child);
                    }

                    if (child.delete()) {
                        deletedFiles++;
                    }
                }
            }
            catch(Exception e) {
            }
        }
        return deletedFiles;
    }

    public static void clearCache(final Context context) {
        clearCacheFolder(context.getCacheDir());
    }

    public static long getCacheSize(final Context context) {
        return getCacheSizeByDir(context.getCacheDir());
    }

    public static long getCacheSizeByDir(final File dir) {
        long size = 0;
        if (dir!= null && dir.isDirectory()) {
            for (File child:dir.listFiles()) {

                if (child.isDirectory()) {
                    size += SysUtils.getCacheSizeByDir(child);
                }

                size += child.length();
            }
        }

        return size;
    }

    public static String getWebUri() {
        int isTest = KsApplication.getInt("isTest", 0);

        if(isTest == 1) {
//            return "http://www.czxshop.net/";
//            return "http://test.czxshop.com/";
            return "http://test.zjzccn.com/";
        } else {
            return "http://www.yzx6868.com/";
//            return "http://new.czxshop.com/";
//            return "http://fx.yzx6868.com";
//            return "http://118.178.174.183";

        }
    }


    public static String getnewWebUri() {
        int isTest = KsApplication.getInt("isTest", 0);

        if(isTest == 1) {
//            return "http://www.czxshop.net/";
//            return "http://test.czxshop.com/";
            return "http://api1.zjzccn.com/";
        } else {
            return "http://api.zjzccn.com/";
//            return "http://new.czxshop.com/";
//            return "http://fx.yzx6868.com";
//            return "http://118.178.174.183";

        }
    }

    //打印二维码的数据
    public static String getnewcode(){
        int isTest = KsApplication.getInt("isTest", 0);
        if(isTest == 1) {
            return "http://bbs.czxshop.com/";
        } else {
            return "http://moli.zjzccn.com/";
        }
    }


    public static String getServiceUrl(String method) {
        return SysUtils.getWebUri() + "rpc/service/?method=ks.seller." + method + "&vsn=1.0&format=json";
    }

    public static String getMemberServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.member." + method + "&vsn=1.0&format=json";
        ret += "&member_token=" + KsApplication.getString("token", "");
        return ret;
    }

    public static String getSellerServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }

    public static String getSellerpinbanServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }


    public static String getGoodspinbanServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_goods." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }


    /**
     *商品添加
     * @param method
     * @return
     */
    public static String getGoodsServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.goods." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }



    /**
     * 二维码扫描收款
     * @param method
     * @return
     */
    public static String payCodeServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }

    /**
     * 修改银行卡号
     * @param method
     * @return
     */
    public static String editBankCardServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }

    /**
     * 修改是否加入桌面
     * @param method
     * @return
     */
    public static String getnewsellerUrl(String method) {
        String ret = SysUtils.getnewWebUri() + "seller/" + method+".html?";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }

    /**
     * 商户信息上传图片
     * @param method
     * @return
     */
    public static String getUploadImageServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }
    /**
     * 商品上传上传图片
     * @param method
     * @return
     */
    public static String getUploadImageGoodsUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.goods." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + KsApplication.getString("token", "");
        return ret;
    }

    public static String getServiceUri(String ext) {
        String ret = Global.webUrl;

        if(!StringUtils.isEmpty(ext)) {
            ret += "?" + ext;
        }

        return ret;
    }

    public static Map<String,String> apiCall(Context ctx, Map<String,Object> param) {
        Map<String,String> map = new HashMap<String,String>();
        try {
            String postData = JSON.toJSONString(param);

            String time = String.valueOf(System.currentTimeMillis() / 1000);
            String postSign = SysUtils.MD5(postData + time + "yiyou123");

            map.put("data", postData);
            map.put("time", time);
            map.put("sign", postSign);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPicSavePath(Context ctx, String yourTitle) {
        if (!isExternalStorageWritable()) {
            SysUtils.showError("sd卡不可写");
            return "";
        }

        String imageFileName = "";
        if(StringUtils.isEmpty(yourTitle)) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageFileName = "huigu_" + timeStamp;
        } else {
            imageFileName = yourTitle;
        }
        imageFileName += ".png";

        String dir = Environment.getExternalStorageDirectory() + "/huigu/";
        File mydir = new File(dir);
        if(!mydir.exists()) {
            mydir.mkdirs();
        }

        imageFileName = dir + imageFileName;

        return imageFileName;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        return formatter.format(curDate);
    }

    //保存到相册
    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static ArrayList<NewsCat> getCacheCatList(Context ctx, String list_cat) {
        ArrayList<NewsCat> mNewsCatList = new ArrayList<NewsCat>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String db_str = sp.getString("cat_str_" + list_cat, "");

//        Log.v("xuebao", "md5: " + list_cat + ", str: " + db_str);
        if(db_str.length() > 0) {
            String [] temp = db_str.split("@@@");
            for(int i = 0; i < temp.length; i++) {
                if(temp[i].length() > 0) {
                    String[] temp2 = temp[i].split("!!!");

                    String catId = "", catTitle = "", catUrl = "", catPicUrl = "";
                    catId = temp2[0];
                    catTitle = temp2[1];
                    if(temp2.length > 2) {
                        catUrl = temp2[2];

                        if(temp2.length > 3) {
                            catPicUrl = temp2[3];
                        }
                    }

                    NewsCat nc = new NewsCat();
                    nc.setCid(catId);
                    nc.setTitle(catTitle);
                    nc.setUrl(catUrl);
                    nc.setPicUrl(catPicUrl);
                    mNewsCatList.add(nc);
                }
            }
        }

        return mNewsCatList;
    }

    public static boolean opCacheCatListByStr(Context ctx, String list_cat, String list_md5, String list_str) {
        boolean mustUpdate = true;

        if(list_md5.length() > 0 && list_str.length() > 0) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
            String db_md5 = sp.getString("cat_md5_" + list_cat, "");

            if(db_md5.length() > 0 && db_md5.equals(list_md5)) {
                mustUpdate = false;
            }

            if(mustUpdate) {
                sp.edit().putString("cat_md5_" + list_cat, list_md5).commit();
                sp.edit().putString("cat_str_" + list_cat, list_str).commit();

            }
        }

        return mustUpdate;
    }

    public static boolean hasFav(Context ctx, int tid, String favType) {
        DBHelper dbHelper = DBHelper.getInstance(ctx);
        SQLiteDatabase sqlite = null;
        int count = 0;

        try {
            sqlite = dbHelper.getWritableDatabase();

//			synchronized(sqlite) {
            String sql = "SELECT COUNT(*) FROM new_fav WHERE tid = ? AND type = ?";
            Cursor cursor = sqlite.rawQuery(sql, new String[] { String.valueOf(tid), favType });
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
//			}
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlite != null && sqlite.isOpen()) {
                sqlite.close();
            }
        }

        return (count == 0) ? false : true;
    }

    public static boolean opFav(Context ctx, String tid, String title, String pic_url, String favType) {
        boolean hasFav = SysUtils.hasFav(ctx, Integer.parseInt(tid), favType);

        DBHelper dbHelper = DBHelper.getInstance(ctx);
        SQLiteDatabase sqlite = null;
//		String successStr = "";

        try {
            sqlite = dbHelper.getWritableDatabase();

//			synchronized(sqlite) {
            if(hasFav) {
                sqlite.execSQL("DELETE FROM new_fav WHERE tid = ? AND type = ?", new Object[] {tid, favType});
//					successStr = "ÒÑÈ¡ÏûÊÕ²Ø";
            } else {
                long releasetime = System.currentTimeMillis();
                sqlite.execSQL("INSERT INTO new_fav VALUES(?, ?, ?, ?, ?)",
                        new Object[] {tid, title, pic_url, favType, String.valueOf(releasetime)});
//					successStr = "ÒÑ¼ÓÈëÊÕ²Ø";
            }
//			}
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlite != null && sqlite.isOpen()) {
                sqlite.close();
            }
        }

//		SysUtils.showSuccess(ctx, successStr);

        return !hasFav;

//		ctx.setFav(!hasFav);
    }

    public static void callTel(Context ctx, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        ctx.startActivity(intent);
    }

    public static void setLine(View view, int line_type, String text, int icon, View.OnClickListener click) {
        setLine(view, line_type, text, icon, click, false);
    }


    public static void setLine(View view, int line_type, String text, int icon, View.OnClickListener click, boolean showIcon) {
        //设置线的类型
        View ll_set_bottom_line = (View) view.findViewById(R.id.ll_set_bottom_line);
        if(line_type == Global.SET_SINGLE_LINE) {
            view.setBackgroundResource(R.drawable.selector_cell_single_line);
        } else if(line_type == Global.SET_CELLUP) {
            view.setBackgroundResource(R.drawable.selector_cell_up_line);
            ll_set_bottom_line.setVisibility(View.VISIBLE);
        } else if(line_type == Global.SET_CELLWHITE) {
            view.setBackgroundResource(R.drawable.selector_cell_white);
            ll_set_bottom_line.setVisibility(View.VISIBLE);
        } else if(line_type == Global.SET_TWO_LINE) {
            view.setBackgroundResource(R.drawable.selector_cell_two_line);
        } else if(line_type == Global.SET_UP_LINE) {
            view.setBackgroundResource(R.drawable.selector_cell_up_line);
        } else if(line_type == Global.SET_PRESSED) {
            view.setBackgroundResource(R.drawable.selector_cell_pressed);
        }
        TextView set_about_text = (TextView) view.findViewById(R.id.ll_set_main_text);
        set_about_text.setText(text);
        ImageView set_about_icon = (ImageView) view.findViewById(R.id.ll_set_icon);
        if(icon > 0) {
            set_about_icon.setImageResource(icon);
        } else {
            set_about_icon.setVisibility(showIcon ? View.INVISIBLE : View.GONE);
        }
        view.setOnClickListener(click);
    }


    public static int getActionBarSize(Context ctx) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = ctx.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public static String getThumbUrl(String pic) {
        if(!StringUtils.isEmpty(pic)) {
            String filenameArray[] = pic.split("\\.");
            String extension = filenameArray[filenameArray.length-1];

            if(!StringUtils.isEmpty(extension)) {
                pic = pic.replace("_s." + extension, "." + extension);
                pic = pic.replace("_m." + extension, "." + extension);
                pic = pic.replace("." + extension, "_s." + extension);
            }
        }

        return pic;
    }

    public static String genderStr(String gender) {
        if(!StringUtils.isEmpty(gender)) {
            if(gender.equals("1")) {
                return "男";
            } else if(gender.equals("2")) {
                return "女";
            }
        }

        return "";
    }

    public static int genderInt(String gender) {
        if(!StringUtils.isEmpty(gender)) {
            if(gender.equals("1")) {
                return 1;
            } else if(gender.equals("2")) {
                return 2;
            }
        }

        return 0;
    }

    public static int getWidgetColor(Context context) {
        final int materialBlue = context.getResources().getColor(R.color.md_material_blue_600);

        int widgetColor = DialogUtils.resolveColor(context, R.attr.colorAccent, materialBlue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            widgetColor = DialogUtils.resolveColor(context, android.R.attr.colorAccent, widgetColor);
        }

        return widgetColor;
    }


    public static void setPaperAccent(Context ctx, PaperButton button) {
        button.setColor(ctx.getResources().getColor(R.color.accent_color));
        button.setShadowColor(ctx.getResources().getColor(R.color.accent_color));
        button.setTextColor(ctx.getResources().getColor(R.color.white));
    }

    public static void setPaperNormal(Context ctx, PaperButton button) {
        button.setColor(ctx.getResources().getColor(R.color.white));
        button.setShadowColor(ctx.getResources().getColor(R.color.white));
        button.setTextColor(ctx.getResources().getColor(R.color.text_secondary_color));
    }

    public static int convertDipToPixels(Context c, float dips) {
        return (int) (dips * c.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static Theme getDialogTheme() {
        return Theme.LIGHT;
    }

    public static int getStickyTop(Context ctx) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (ctx.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,ctx.getResources().getDisplayMetrics());
        }
        int actionBarPaddingTop = 0;
        if (ctx.getTheme().resolveAttribute(R.attr.title_bar_padding_top, tv, true)) {
            actionBarPaddingTop = TypedValue.complexToDimensionPixelSize(tv.data,ctx.getResources().getDisplayMetrics());
        }
        int topOffset = actionBarHeight + actionBarPaddingTop;
        topOffset += 12;

        return topOffset;
    }

    public static void setAdHeight(RelativeLayout ad_layout) {
        RelativeLayout.LayoutParams frame_layout = new RelativeLayout.LayoutParams(Global.magicWidth, Global.magicHeight);
        ad_layout.setLayoutParams(frame_layout);
    }

    public static String priceFormat(double d, boolean hasSuffix) {
        String ret = "";
        if(d == (long) d) {
            ret = String.format("%d",(long)d);
        } else {
            ret = String.format("%.2f", d);
            ret = String.format("%s", ret);

//            Log.v("huigu", "price: " + ret);
            if (ret.endsWith(".0")) {
                if (ret.equals(".0")) {
                    ret = "0";
                } else {
                    ret = ret.substring(0, ret.length() - 2);
                }
            }
        }

        if(hasSuffix) {
            ret = String.format("￥%s元", ret);
        }

        return ret;
    }

    public static int ceil(int a, int b) {
        if(b > 0) {
            double v = (double) a / b;
            return (int) Math.ceil(v);
        }

        return 0;
    }


    public static void setupUI(final Activity act, View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    SysUtils.hideSoftKeyboard(act);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                SysUtils.setupUI(act, innerView);
            }
        }
    }

    public static void newsClick(Context ctx, News bean) {
        int setlink = bean.getSetlink();

        if(setlink == 2) {
            //url
            SysUtils.openUrl(ctx, bean.getLinkurl());

//            Bundle bundle = new Bundle();
//            bundle.putString("url", bean.getLinkurl());
//            bundle.putString("title", bean.getSubject());
//            bundle.putString("pic_url", bean.getPic_url());
//            bundle.putString("resume", bean.getResume());
//            bundle.putBoolean("disableNav", true);
//
//            SysUtils.startAct(ctx, new AdActivity(), bundle);
        } else if (setlink == 1) {
            //内部链接
            String module = bean.getLinkurl();
            if (module.equals("article")) {
                //文章
                int articleId = bean.getTid();
                if (articleId > 0) {
                    //文章
                    Bundle b = new Bundle();
                    b.putInt("news_id", articleId);

//                    SysUtils.startAct(ctx, new NewsDetailActivity(), b);
                }
            }
//            SysUtils.showSuccess(module);
        }
    }

    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     *  px 转为 dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 预防按钮连续点击
     */
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    /**
     * 得到资讯栏目的分类
     * @param ctx
     * @return
     */
    public static ArrayList<NewsCat> getNewsSelectCat(Context ctx) {
        ArrayList<NewsCat> finalSelect = new ArrayList<NewsCat>();

        String channel_order = KsApplication.getString(Global.NEWS_USER_TAG, "");
        String channel_other = KsApplication.getString(Global.NEWS_OTHER_TAG, "");
        List<NewsCat> mNewsCatList = SysUtils.getCacheCatList(ctx, "news");

//        Log.v("xuebao", channel_order);
//        Log.v("xuebao", channel_other);

        if(mNewsCatList.size() > 0) {
            String[] temp = !StringUtils.isEmpty(channel_order) ? channel_order.split(",") : new String[0];
            String[] temp_other = !StringUtils.isEmpty(channel_other) ? channel_other.split(",") : new String[0];

            //首先把用户已选的加入进来
            for(int i = 0; i < temp.length; i++) {
                for(int j = 0; j < mNewsCatList.size(); j++) {
                    NewsCat bean = mNewsCatList.get(j);

                    if(bean.getCid().equals(temp[i])) {
                        finalSelect.add(bean);
                        break;
                    }
                }
            }

            //再把同时不在 已选 和 待选 的，表示可能是刚加的分类，那么需要把这个算到已选分类中加到结尾
            for(int i = 0; i < mNewsCatList.size(); i++) {
                NewsCat bean = mNewsCatList.get(i);

                boolean has = false;
                for(int j = 0; j < temp.length; j++) {
                    if(!StringUtils.isEmpty(temp[j])) {

                        if(bean.getCid().equals(temp[j])) {
                            //如果已经有了
                            has = true;
                            break;
                        }
                    }
                }

                if(!has) {
                    for(int j = 0; j < temp_other.length; j++) {
                        if(!StringUtils.isEmpty(temp_other[j])) {
                            if(bean.getCid().equals(temp_other[j])) {
                                has = true;
                                break;
                            }
                        }
                    }
                }

                if(!has) {
                    //两个都没有，那么就是新增的分类
                    finalSelect.add(bean);
                }
            }
        }

        return finalSelect;
    }

    public static void recreate(Context ctx) {
        if(Build.VERSION.SDK_INT >= 16) {
            TaskStackBuilder.create(ctx)
                    .addNextIntent(new Intent(ctx, MainActivity.class))
                    .addNextIntent(((Activity)ctx).getIntent())
                    .startActivities();
        } else {
            ((Activity) ctx).finish();
            Intent i = new Intent(ctx, MainActivity.class);
            ctx.startActivity(i);
        }
    }

    public static String getMacAddress(Context ctx) {
        String macAddress = null, ip = null;
        WifiManager wifiMgr = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
//            ip = int2ip(info.getIpAddress());
        }

        return macAddress;
    }

    public static double ScHgt(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }


    public static void updateOvilColor(ImageButton btn, int c) {
        Drawable background = btn.getBackground();
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable)background;
            shapeDrawable.getPaint().setColor(c);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable)background;
            gradientDrawable.setColor(c);
        }
    }

    public static void openUrl(Context context, String url) {
        new FinestWebView.Builder((Activity)context)
                .iconDefaultColorRes(R.color.white)
                .stringResRefresh(R.string.webview_refresh)
                .stringResShareVia(R.string.webview_share)
                .stringResCopyLink(R.string.webview_copy_link)
                .stringResOpenWith(R.string.webview_open)
                .show(url);
    }

    public static DisplayImageOptions imageOption() {
        return SysUtils.imageOption(true);
    }

    public static DisplayImageOptions imageOption(boolean fade) {
        if(fade) {
            return new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.placeholder_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new FadeInBitmapDisplayer(1500))
                    .build();
        } else {
            return new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.placeholder_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getCopyUri() {
        return "http://www.yzx6868.com/apk/copyright.html";
    }

    /**
     * 得到唯一id
     * @param ctx
     * @return
     */
    public static String getDeviceID(Context ctx) {
        //1 compute IMEI
        TelephonyManager TelephonyMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE

        //2 compute DEVICE ID
        String devIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits

        //3 android ID - unreliable
        String androidID = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

        //4 wifi manager, read MAC address - requires  android.permission.ACCESS_WIFI_STATE or comes as null
        WifiManager wm = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        String wLANMAC = wm.getConnectionInfo().getMacAddress();

        //5 Bluetooth MAC address  android.permission.BLUETOOTH required
        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bTMAC = m_BluetoothAdapter.getAddress();

        //6 SUM THE IDs
        String deviceID = imei + devIDShort + androidID + wLANMAC + bTMAC;

        return deviceID;
    }

    public static String getMoneyFormat(double d) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
        return nf.format(d);
    }

    public  static JSONObject didResponse(JSONObject responseObject) {
        JSONObject ret = new JSONObject();
        try {
            JSONObject re = responseObject.getJSONObject("response");
            String status = re.getString("status");
            String message = re.getString("message");

            ret.put("status", status);
            ret.put("message", message);

            Object o = re.get("data");
            ret.put("data", o);

//            if (o instanceof String) {
//
//            }
//            ret.put("data", re.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static Order getOrderRow(JSONObject data) {
        Order b = null;

        try {
            String desk_num = SysUtils.getFinalString("desk_num", data);
            if(desk_num.equals("0")) {
                desk_num = "";
            }
            if("cash".equals(data.getString("payment"))){
                b = new Order(
                        data.getString("order_id"),
                        data.getString("createtime"),
                        SysUtils.getFinalString("name", data),
                        data.optInt("pay_status"),
                        data.optInt("shipping_id"),
                        data.optInt("ship_status"),
                        data.optInt("button_affirm"),
                        data.optInt("button_off"),
                        data.optInt("shipments_yes"),
                        data.optInt("shipments_no"),
                        SysUtils.getFinalString("ship_addr", data),
                        SysUtils.getFinalString("ship_name", data),
                        SysUtils.getFinalString("ship_mobile", data),
                        SysUtils.getFinalString("ship_tel",data),
                        data.optDouble("total_amount"),
                        data.optDouble("cost_freight"),
                        data.optInt("delivery_express"),
                        data.optInt("delivery_seller"),
                        data.optInt("delivery_seller_dt_id"),
                        SysUtils.getFinalString("seller_name", data),
                        SysUtils.getFinalString("seller_tel", data),
                        SysUtils.getFinalString("memo", data),
//                        SysUtils.getFinalString("mark_text", data),
                        data.optInt("distribution"),
                        SysUtils.getFinalString("status", data),
                        data.optDouble("cost_item"),
                        data.optDouble("pmt_order"),
                        data.optDouble("payed"),
                        SysUtils.getFinalString("order_status", data),
                        SysUtils.getFinalString("print_number", data),
                        data.optDouble("total_amount"),
                        SysUtils.getFinalString("quantity", data),
                        SysUtils.getFinalString("qrcode_url", data),
                        desk_num,
                        SysUtils.getFinalString("qr_uri", data),
                        SysUtils.getFinalString("payment", data),
                        SysUtils.getFinalString("status", data),
                        SysUtils.getFinalString("remark",data),
                        SysUtils.getFinalString("province_name", data),
                        SysUtils.getFinalString("city_name", data),
                        SysUtils.getFinalString("district_name", data));
            }else {
                b = new Order(
                        data.getString("order_id"),
                        data.getString("createtime"),
                        SysUtils.getFinalString("name", data),
                        data.optInt("pay_status"),
                        data.optInt("shipping_id"),
                        data.optInt("ship_status"),
                        data.optInt("button_affirm"),
                        data.optInt("button_off"),
                        data.optInt("shipments_yes"),
                        data.optInt("shipments_no"),
                        SysUtils.getFinalString("ship_addr", data),
                        SysUtils.getFinalString("ship_name", data),
                        SysUtils.getFinalString("ship_mobile", data),
                        SysUtils.getFinalString("ship_tel",data),
                        data.optDouble("final_amount"),
                        data.optDouble("cost_freight"),
                        data.optInt("delivery_express"),
                        data.optInt("delivery_seller"),
                        data.optInt("delivery_seller_dt_id"),
                        SysUtils.getFinalString("seller_name", data),
                        SysUtils.getFinalString("seller_tel", data),
//                        SysUtils.getFinalString("mark_text", data),
                        SysUtils.getFinalString("memo", data),
                        data.optInt("distribution"),
                        SysUtils.getFinalString("status", data),
                        data.optDouble("cost_item"),
                        data.optDouble("pmt_order"),
                        data.optDouble("payed"),
                        SysUtils.getFinalString("order_status", data),
                        SysUtils.getFinalString("print_number", data),
                        data.optDouble("apay_order"),
                        data.optString("order_num"),
                        SysUtils.getFinalString("qrcode_url", data),
                        desk_num,
                        SysUtils.getFinalString("qr_uri", data),
                        SysUtils.getFinalString("payment", data),
                        SysUtils.getFinalString("status", data),
                        SysUtils.getFinalString("remark",data),
                        SysUtils.getFinalString("province_name", data),
                        SysUtils.getFinalString("city_name", data),
                        SysUtils.getFinalString("district_name", data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    public static String getFinalString(String key, JSONObject data) {
        String ret = "";

        try {
            if (!data.isNull(key)) {
                ret = data.getString(key);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static int getFinalInt(String key, JSONObject data) {
        int ret = 0;

        try {
            if (!data.isNull(key)) {
                ret = data.getInt(key);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}

