package com.library.base.mvp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.library.LanguageUtil.LanguageUtil;
import com.library.LanguageUtil.PreferenceLanguageUtils;
import com.library.R;
import com.library.app.LibAplication;
import com.library.utils.DialogUtils;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2019/4/24.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    private T mPresenter;
    //对所有activity进行管理
    private static Activity mCurrentActivity;

    public static List<Activity> mActivitys = new LinkedList<>();

    private Dialog progressDialog = null;


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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        //设置状态栏颜色
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), 0);


        ButterKnife.bind(this);
        //路由自动属性注入
        ARouter.getInstance().inject(this);



        //初始化的时候将其添加到集合中
        synchronized (mActivitys) {
            mActivitys.add(this);
        }
        initView();
//        initData();

    }


    /**
     * 返回一个用于页面显示界面的布局id
     *
     * @return
     */
    public abstract int getContentView();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
//    protected abstract void initData();
    @Override
    protected void onResume() {
        super.onResume();
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //测试内存泄漏，正式一定要隐藏
        RefWatcher refWatcher = LibAplication.getRefWatcher(this);//1
        refWatcher.watch(this);

        //退出的时候清除
        synchronized (mActivitys) {
            mActivitys.remove(this);
        }
    }

    public void routerNavigation(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    public void initTabTitle(String title, String rightTv) {
        ImageView ivBack = findViewById(R.id.iv_back);
        TextView titleTv = findViewById(R.id.toolbar_title);
        TextView right = findViewById(R.id.toolbar_right);
        titleTv.setText(title);
        right.setText(rightTv);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    public void showLoading() {
        showLoading(this, getString(R.string.lib_str2));
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

    public void showToast(String msg) {
        ToastUtils.showShort(msg);
        ToastUtils.setMsgColor(getResources().getColor(R.color.blackText));

    }
}
