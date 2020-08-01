package com.ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static DBHelper sInstance;
	public static final String DATABASE_NAME = "yiyou.db";
	private static final int DATABASE_VERSION = 3;

	public static int version() {
		return DATABASE_VERSION;
	}

	public static DBHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DBHelper(context.getApplicationContext());
		}

		return sInstance;
	}

	public DBHelper(Context context) {
		//CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		addHistory(db);
		addHasRead(db);
		addOpenOrder(db);
		addSearchHistory(db);
	}

	//如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion>oldVersion){
			addOpenOrder(db);
			addSearchHistory(db);
		}
	}

	//历史记录
	private void addHistory(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS records" +
				"(tid INTEGER DEFAULT 0, type varchar(10), name varchar(200), releasetime LONG)");

	}

	//已阅列表
	private void addHasRead(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS has_read" +
				"(f_tid INTEGER DEFAULT 0, f_type INTEGER DEFAULT 0, PRIMARY KEY (f_type, f_tid))");

	}

	/**
	 * 开单页面购物车表
	 * @param db
     */
	private void addOpenOrder(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS openorder" +
				"(id varchar(20) PRIMARY KEY, tag_id varchar(20),name varchar(20),price double,num integer,obj_id varchar(20),item_id varchar(20))");
	}
	/**
	 * 搜索历史记录表
	 */
	private void addSearchHistory(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS searchhistoryrecords" +
				"(name varchar(20))");
	}
}

