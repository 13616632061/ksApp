package com.ui.ks;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.base.BaseActivity;
import com.ui.util.LoginUtils;
import com.ui.util.SysUtils;
import com.ui.view.avtivity.ShopActivity;

/**
 * 启动app时的欢迎页面，2s后进入
 */
public class Welcome2Activity extends BaseActivity {
    String TAG="测试登出的步骤";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 0, true, false);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_welcome2, null);

        setContentView(view);


        new Handler().postDelayed(new Runnable(){
            public void run() {
//                Intent i = new Intent(Welcome2Activity.this, LoginActivity.class);
                if (LoginUtils.isSeller()) {
                    Log.d(TAG, "run: 1111111");
                    SysUtils.startAct(Welcome2Activity.this, new ShopActivity());
                } else if (LoginUtils.isShopper()) {
                    Log.d(TAG, "run: 2222222");
                    SysUtils.startAct(Welcome2Activity.this, new ShopActivity());
                }else if (LoginUtils.isMember()) {
                    Log.d(TAG, "run: 3333333");
                    SysUtils.startAct(Welcome2Activity.this, new ReportActivity());
                } else if (LoginUtils.isMainStore()) {
                    Log.d(TAG, "run: 4444444");
                    SysUtils.startAct(Welcome2Activity.this, new MainStoreActivity());
                }else {
                    SysUtils.startAct(Welcome2Activity.this, new LoginActivity());
//                    Intent i = new Intent(Welcome2Activity.this, LoginActivity.class);
//                    Welcome2Activity.this.startActivity(i);
//                    Welcome2Activity.this.finish();
                }
                Log.d(TAG, "run: 6666666");
//                Welcome2Activity.this.startActivity(i);
                Welcome2Activity.this.finish();
            }
        }, 2000);
    }
}
