package com.ui.ks;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.base.BaseActivity;
import com.ui.global.Global;
import com.ui.update.UpdateAsyncTask;
import com.ui.util.SysUtils;

public class AboutActivity extends BaseActivity {
    private String versionName;
    private TextView set_version_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initToolbar(this);

        TextView about_version_text = (TextView) findViewById(R.id.about_version_text);
        about_version_text.setText(Html.fromHtml(Global.ABOUT_TEXT));

        versionName = SysUtils.getAppVersionName(this);
        //检查更新
        View set_update = (View) findViewById(R.id.set_update_item);
        set_version_text = (TextView) set_update.findViewById(R.id.ll_set_hint_text);
        SysUtils.setLine(set_update, Global.SET_CELLUP, "系统升级", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVersion();
            }
        });

        //版权信息
        View set_fk = (View) findViewById(R.id.set_fk_item);
        SysUtils.setLine(set_fk, Global.SET_SINGLE_LINE, "版权信息", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle a = new Bundle();
                a.putString("title", "版权信息");
                a.putString("url", SysUtils.getCopyUri());

                SysUtils.startAct(AboutActivity.this, new AdActivity(), a);
            }
        });
    }

    public void checkVersion() {
        UpdateAsyncTask myAsyncTask = new UpdateAsyncTask(AboutActivity.this, true);
        myAsyncTask.execute();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (KsApplication.hasNewVersion) {
            set_version_text.setText("发现有新版本 V" + KsApplication.newVersionName);
        } else {
            set_version_text.setText("V" + versionName);
        }
    }
}
