package com.ui.ks;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.base.BaseActivity;
import com.ui.global.Global;
import com.ui.util.SysUtils;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    Boolean isExit = false;
    Boolean hasTask = false;
    Timer tExit;
    TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tExit = new Timer();
        task = new TimerTask() {
            public void run() {
                isExit = false;
                hasTask = true;
            }
        };

        //业务员管理
        View set_item_1 = (View) findViewById(R.id.set_main_item_1);
        SysUtils.setLine(set_item_1, Global.SET_CELLUP, "业务员管理", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.startAct(MainActivity.this, new ReportActivity());
            }
        });

        //昨日营业额
        View set_item_2 = (View) findViewById(R.id.set_main_item_2);
        SysUtils.setLine(set_item_2, Global.SET_CELLWHITE, "代理商管理", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.startAct(MainActivity.this, new ReportActivity());
            }
        });

        //商家管理
        View set_item_3 = (View) findViewById(R.id.set_main_item_3);
        SysUtils.setLine(set_item_3, Global.SET_SINGLE_LINE, "商家管理", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.startAct(MainActivity.this, new ShopActivity());
            }
        });

        //登录界面
        View set_item_4 = (View) findViewById(R.id.set_main_item_4);
        SysUtils.setLine(set_item_4, Global.SET_TWO_LINE, "登录界面", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.startAct(MainActivity.this, new LoginActivity());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(isExit==false){
                isExit=true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                if(!hasTask){
                    tExit.schedule(task, 2000);
                }
            }else{
                finish();
                System.exit(0);
            }
        }

        return false;
    }
}
