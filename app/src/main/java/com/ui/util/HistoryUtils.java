package com.ui.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ui.db.DBHelper;
import com.ui.entity.NewsCat;

import java.util.ArrayList;

public class HistoryUtils {
    public static void saveHistory(Context ctx, String type, int id, String text) {
        //存储播放历史记录
        if (!TextUtils.isEmpty(text)) {
            DBHelper dbHelper = DBHelper.getInstance(ctx);
            SQLiteDatabase sqlite = null;

            try {
                sqlite = dbHelper.getWritableDatabase();

                String sql = "SELECT COUNT(*) FROM records WHERE type = ? AND name = ?";
                Cursor cursor = sqlite.rawQuery(sql, new String[] { type, text });
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                cursor.close();

                boolean hasHistory = (count == 0) ? false : true;

                long releasetime = java.lang.System.currentTimeMillis();
                if(hasHistory) {
                    //只需要更新最近的播放时间即可

                    sqlite.execSQL("UPDATE records SET releasetime = ? WHERE type = ? AND name = ?",
                            new Object[] {String.valueOf(releasetime), type, text});
                } else {
                    //新播放，插入记录

                    sqlite.execSQL("INSERT INTO records VALUES(?, ?, ?, ?)",
                            new Object[] {id, type, text, String.valueOf(releasetime)});
                }

                sql = "SELECT releasetime FROM records WHERE type = '"
                        + type + "' ORDER BY releasetime DESC LIMIT 10 OFFSET 0";
                cursor = sqlite.rawQuery(sql, null);
                Long lastTime = (long) 0;
                while (cursor.moveToNext()) {
                    lastTime = cursor.getLong(0);
                }
                cursor.close();

                if(lastTime > 0) {
                    //删除其他记录

                    sqlite.execSQL("DELETE FROM records WHERE type = '"
                                + type + "' AND releasetime < ?", new Object[] {lastTime});
                }
            }catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (sqlite != null && sqlite.isOpen()) {
                    sqlite.close();
                }
            }
        }
    }

    public static ArrayList<NewsCat> getHistory(Context ctx, String type) {
        ArrayList<NewsCat> historyList = new ArrayList<NewsCat>();

        DBHelper dbHelper = DBHelper.getInstance(ctx);
        SQLiteDatabase sqlite = null;

        try {
            sqlite = dbHelper.getWritableDatabase();

            String sql = "SELECT * FROM records WHERE type = '" + type + "' ORDER BY releasetime DESC";
            Cursor cursor = sqlite.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String f_tid = cursor.getString(0);
//                String f_type = cursor.getString(1);
                String f_title = cursor.getString(2);
//                Long time = cursor.getLong(1);

                NewsCat bean = new NewsCat();
                bean.setCid(f_tid);
                bean.setTitle(f_title);

                historyList.add(bean);
            }
            cursor.close();
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlite != null && sqlite.isOpen()) {
                sqlite.close();
            }
        }

        return historyList;
    }

    public static void clearHistory(Context ctx, String type) {
        DBHelper dbHelper = DBHelper.getInstance(ctx);
        SQLiteDatabase sqlite = null;

        try {
            sqlite = dbHelper.getWritableDatabase();
            sqlite.execSQL("DELETE FROM records WHERE type = '" + type + "'");
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlite != null && sqlite.isOpen()) {
                sqlite.close();
            }
        }
    }
}
