package com.ui.ks;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.afollestad.materialdialogs.MaterialDialog;
import com.material.widget.Switch;
import com.ui.db.DBHelper;
import com.ui.global.Global;
import com.ui.update.UpdateAsyncTask;
import com.ui.util.SysUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


public class SetActivity extends BaseUpdateActivity {
    private TextView cache_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        initToolbar(this);





        //语音提示
        View set_push = (View) findViewById(R.id.set_push_item);
        Switch ll_set_push_switch = (Switch) set_push.findViewById(R.id.ll_set_switch);
        ImageView ll_set_push_idx = (ImageView) set_push.findViewById(R.id.ll_set_idx);
        ll_set_push_idx.setVisibility(View.GONE);
        ll_set_push_switch.setChecked(KsApplication.getBoolean("notify_sound", true));
        ll_set_push_switch.setVisibility(View.VISIBLE);
        ll_set_push_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                KsApplication.putBoolean("notify_sound", arg1);
            }
        });

        SysUtils.setLine(set_push, Global.SET_CELLUP, getString(R.string.str156), 0, null);//语音提示

        //振动开关
        View set_net = (View) findViewById(R.id.set_net_item);
        Switch ll_set_net_switch = (Switch) set_net.findViewById(R.id.ll_set_switch);
        ImageView ll_set_net_idx = (ImageView) set_net.findViewById(R.id.ll_set_idx);
        ll_set_net_idx.setVisibility(View.GONE);
        ll_set_net_switch.setChecked(KsApplication.getBoolean("notify_vibrate", true));
        ll_set_net_switch.setVisibility(View.VISIBLE);
        ll_set_net_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                KsApplication.putBoolean("notify_vibrate", arg1);
            }
        });

        SysUtils.setLine(set_net, Global.SET_SINGLE_LINE, getString(R.string.str157), 0, null);//振动开关

        //清除缓存
        View set_cache = (View) findViewById(R.id.set_cache_item);
        cache_btn = (TextView) set_cache.findViewById(R.id.ll_set_hint_text);
        System.out.println("formatSize()="+formatSize());
        cache_btn.setText(formatSize());
        SysUtils.setLine(set_cache, Global.SET_TWO_LINE, getString(R.string.str158), 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {//清除缓存
                new MaterialDialog.Builder(SetActivity.this)
                        .content(getString(R.string.str155))//确定清理缓存？
                        .theme(SysUtils.getDialogTheme())
                        .positiveText(getString(R.string.sure))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                clearCache();
                            }
                        })
                        .show();
            }
        });
    }


    public void checkVersion() {
        UpdateAsyncTask myAsyncTask = new UpdateAsyncTask(SetActivity.this, true);
        myAsyncTask.execute();
    }

    private long getWebviewSize() {
        long size = 0;

        try {
            size += new File(getDatabasePath("webview.db").getAbsolutePath()).length();
            size += new File(getDatabasePath("webviewCache.db").getAbsolutePath()).length();
        } catch(Exception e) {

        }

        return size;
    }

    private long getDbSize() {
        long dbSize = 0;
        DBHelper dbHelper = DBHelper.getInstance(SetActivity.this);
        SQLiteDatabase sqlite = null;

        try {
            sqlite = dbHelper.getReadableDatabase();
            dbSize = new File(sqlite.getPath()).length();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlite != null && sqlite.isOpen()) {
                sqlite.close();
            }
        }

        return dbSize;
    }

    private String formatSize() {
        long dbSize = getDbSize();

        long cacheSize = SysUtils.getCacheSize(this);
        long uilDiskCacheSize = SysUtils.getCacheSizeByDir(StorageUtils.getCacheDirectory(this));   //uil缓存大小
        long totalSize = dbSize + cacheSize + uilDiskCacheSize;

        String size = SysUtils.humanReadableByteCount(totalSize, false);
        System.out.println("dbSize="+dbSize);
        System.out.println("cacheSize="+cacheSize);
        System.out.println("uilDiskCacheSize="+uilDiskCacheSize);
        System.out.println("size="+size);
        return size;

    }

    private void deleteDb(String name) {
        try {
            this.deleteDatabase(name);
        } catch(Exception e) {

        }
    }

    private void clearCache() {
        deleteDb("ks.db");
        try {
            SysUtils.clearCache(this);
        } catch(Exception e) {

        }

        try {
            //清空uil缓存
            imageLoader.clearMemoryCache();
            imageLoader.clearDiskCache();
        } catch(Exception e) {

        }
        String formatSize = "0 KiB";
        cache_btn.setText(formatSize);
    }
}
