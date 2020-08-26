package com.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.LogUtils;
import com.ui.ks.R;
import com.ui.update.UpdateAsyncTask;
import com.ui.util.DialogUtils;
import com.library.LanguageUtil.LanguageUtil;
import com.library.LanguageUtil.PreferenceLanguageUtils;
import com.ui.util.LoginUtils;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;
import com.ui.util.SystemBarTintManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    private Dialog progressDialog = null;
    public Toolbar toolbar = null;
    private SystemBarTintManager tintManager = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected TextView toolbarTitle;
//    public static TextView toolbarshopTitle;

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            LogUtils.i("avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void attachBaseContext(Context newBase) {
        if (null == newBase) {
            super.attachBaseContext(newBase);
        } else {
            int languageIndex = PreferenceLanguageUtils.getInstance().getLanguage();
            super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, LanguageUtil.switchLanguage(languageIndex)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        onCreate(savedInstanceState, 0);
    }

    protected void onCreate(Bundle savedInstanceState, int loginType) {
        onCreate(savedInstanceState, loginType, true);
    }

    protected void onCreate(Bundle savedInstanceState, int loginType, boolean setTheme) {
        onCreate(savedInstanceState, loginType, setTheme, true);
    }

    protected void onCreate(Bundle savedInstanceState, int loginType, boolean setTheme, boolean checkUpdate) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            LogUtils.i("onCreate fixOrientation when Oreo, result = " + result);
        }
        super.onCreate(savedInstanceState);
        setTitle(null);
        ActivityManager.getInstance().addActivity(this);

        boolean canNext = true;
        if (loginType == 1) {
            //卖家
            if (!LoginUtils.isSeller()) {
                canNext = false;
            }
        } else if (loginType == 2) {
            //业务员
            if (!LoginUtils.isMember()) {
                canNext = false;
            }
        } else if (loginType == 3) {
            //总店
            if (!LoginUtils.isMainStore()) {
                canNext = false;
            }
        } else if (loginType == 4) {
            //营业员
            if (!LoginUtils.isShopper()) {
                canNext = false;
            }
        }
        if (!canNext) {
            LoginUtils.toLogin(this, loginType);

            finish();
            return;

        }
        //应用主题
        if (setTheme) {
//            Theme.onActivityCreate(this, savedInstanceState);
        }

        if (checkUpdate) {
            //检测版本更新
            checkVersion();
        }
    }

    public void checkVersion() {
        UpdateAsyncTask myAsyncTask = new UpdateAsyncTask(this, false);
        myAsyncTask.execute();
    }

    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideLoading();
    }

    public void showLoading(Context ctx, String msg) {
        showLoading(ctx, msg, true);
    }

    public void showLoading(Context ctx, String msg, boolean canCancel) {
        try {
            progressDialog = DialogUtils.createLoadingDialog(ctx, msg, canCancel);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoading(Context ctx) {
        showLoading(ctx, getString(R.string.str92));
    }

    public void hideLoading() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initToolbar(AppCompatActivity act) {
        initToolbar(act, 1);
    }

    /**
     * @param act
     * @param type     0：不设置，1：红色，2：透明 3 白色
     * @param showHome 是否显示返回按钮
     */
    public void initToolbar(AppCompatActivity act, int type, boolean showHome) {
        toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_white);
        act.setSupportActionBar(toolbar);

        ActionBar actionbar = act.getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(showHome);

        if (type == 2) {
            //透明时手动调用下这句话，处理某些android版本中虽然指定透明主题，但打开默认还是会有一层颜色的情况
            toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else if (type == 1) {
//            toolbar.getBackground().setAlpha(100);
        }

        setTintColor(type);

        //标题
        String label = null;
        try {
            label = getResources().getString(
                    getPackageManager().getActivityInfo(getComponentName(), 0).labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        if (label != null) {
            setToolbarTitle(label);
        }
    }

    public void setTintColor(int type) {
        if (type > 0 && initTint()) {
            tintManager.setStatusBarTintEnabled(true);
            setStatusBarTintColor(getResources().getColor(getColorType(type)));
        }
    }

    private boolean initTint() {
        boolean ret = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }

            ret = true;
        }

        return ret;
    }

    public void setStatusBarTintColor(int color) {
        if (initTint()) {
            tintManager.setStatusBarTintColor(color);
        }
    }

    public void initToolbar(AppCompatActivity act, int type) {
        initToolbar(act, type, true);
    }

    private int getColorType(int type) {
        if (type == 2) {
            return R.color.transparent;
        } else if (type == 3) {
            return R.color.black;
        } else if (type == 4) {
            return R.color.black;
        } else if (type == 5) {
            return R.color.white;
        } else {
//            return Theme.isFemaleMode() ? R.color.female_dark_primary_color : R.color.dark_primary_color;
            return R.color.dark_primary_color;
        }
    }

    //销毁activity前，销毁掉所有的网络请求
    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
        ActivityManager.getInstance().finishActivity(this);
    }

    //请求网络
    public void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

    /**
     * @Description:是否透明主题
     * @Author:lyf
     * @Date: 2020/8/15
     */
    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * @Description:是否修改方向
     * @Author:lyf
     * @Date: 2020/8/15
     */
    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
