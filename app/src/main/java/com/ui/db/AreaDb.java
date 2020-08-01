package com.ui.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.ui.entity.Area;
import com.MyApplication.KsApplication;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AreaDb {
    List<Area> words;
    private Context ctx;
    private static final int AREA_DATABASE_VERSION = 1;

    public AreaDb(Context ctx) {
        this.ctx = ctx;
        this.words = new ArrayList<Area>();
    }

    public void read() {
        if(words.size() < 1) {
            //直接从数据库中读取，速度会很快
            DBHelper dbHelper = DBHelper.getInstance(ctx);
            SQLiteDatabase sqlite = null;

            words = new ArrayList<Area>();

            try {
                sqlite = dbHelper.getWritableDatabase();

                String sql = "SELECT * FROM area ORDER BY ord ASC";
                Cursor cursor = sqlite.rawQuery(sql, null);

                while (cursor.moveToNext()) {
                    int cid = cursor.getInt(0);
                    String title = cursor.getString(1);
                    int pid = cursor.getInt(2);

                    Area bean = new Area();
                    bean.setCid(cid);
                    bean.setTitle(title);
                    bean.setPid(pid);

                    words.add(bean);
                }
                cursor.close();
            }catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (sqlite != null && sqlite.isOpen()) {
                    sqlite.close();
                }
            }
        }
    }

    public List<Area> getList(int pid) {
        List<Area> ret = new ArrayList<Area>();

        for(int i = 0; i < words.size(); i++) {
            Area bean = words.get(i);

            if(bean.getPid() == pid) {
                ret.add(bean);
            }
        }

        return ret;
    }

    /**
     * 是否从数据库中读取
     * @return
     */
    public boolean fromDb() {
        boolean ret = false;

        int current_version = KsApplication.getInt("area_version", 0);
        if(current_version >= AREA_DATABASE_VERSION) {
            //检查是否有一条数据（被清缓存清掉了，那么需要重新读取）
            DBHelper dbHelper = DBHelper.getInstance(ctx);
            SQLiteDatabase sqlite = null;

            try {
                sqlite = dbHelper.getWritableDatabase();
                Cursor cursor = sqlite.rawQuery("SELECT 1 FROM area", null);
                if(cursor.moveToFirst()){
                    //有记录，那么从
                    ret = true;
                }
                cursor.close();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (sqlite != null && sqlite.isOpen()) {
                    sqlite.close();
                }
            }
        }

        return ret;
    }

    public void write() {
        AssetManager am = ctx.getAssets();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        DBHelper dbHelper = DBHelper.getInstance(ctx);
        SQLiteDatabase sqlite = null;
        try {
            InputStream is = am.open("area.txt");

            int i;
            i = is.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = is.read();
            }
            is.close();

            words = JSON.parseArray(byteArrayOutputStream.toString(), Area.class);

            //保存在数据库中
            sqlite = dbHelper.getWritableDatabase();
            sqlite.execSQL("DELETE FROM area");

            for(int j = 0; j < words.size(); j++) {
                Area bean = words.get(j);

                sqlite.execSQL("INSERT INTO area VALUES(?, ?, ?, ?)",
                        new Object[] {bean.getCid(), bean.getTitle(), bean.getPid(), j});
            }

            //插入完成后
            KsApplication.putInt("area_version", AREA_DATABASE_VERSION);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlite != null && sqlite.isOpen()) {
                sqlite.close();
            }
        }
    }
}
