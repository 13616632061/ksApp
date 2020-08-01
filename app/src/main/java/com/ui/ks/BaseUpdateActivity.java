package com.ui.ks;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.base.BaseActivity;

public class BaseUpdateActivity extends BaseActivity {
    //文件下载完成，尝试打开提醒用户安装
    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        public void onReceive(Context ctx, Intent intent) {
            //获取下载的文件id
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if(downId > 0) {
                DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri apk = manager.getUriForDownloadedFile(downId);

                Intent promptInstall = new Intent(Intent.ACTION_VIEW);
                promptInstall.setDataAndType(apk, "application/vnd.android.package-archive");
                promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(promptInstall);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        //注册监听器
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            //反注册监听器
            unregisterReceiver(onDownloadComplete);
        } catch(Exception e) {

        }
    }
}
