package com.example.palpointer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {
	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String TABLE_NAME = "mytable";
	public static final String DATA_BASE_NAME = "mydatabase";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_CREATE = "create table mytable (name text not null primary key, phone text not null);";

	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public DataHandler (Context ctx){
		this.ctx = ctx;
		dbhelper = new DataBaseHelper(ctx);
	}

	private static class DataBaseHelper extends SQLiteOpenHelper {

		public DataBaseHelper(Context ctx) {
			super(ctx, DATA_BASE_NAME,null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS  mytable");
			onCreate(db);
		}

	}

	public DataHandler open(){
		db = dbhelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbhelper.close();
	}

	public long insertData (String name, String phone) {
		ContentValues content = new ContentValues();
		content.put(NAME, name);
		content.put(PHONE, phone);
		return db.insertOrThrow(TABLE_NAME, null, content);
	}

	public int deleteData(String name) {
		return db.delete(TABLE_NAME, "NAME = ?", new String [] {name});	
	}
	
	public int updateData(String name, String newName, String newNr) {
		ContentValues content = new ContentValues();
		content.put(NAME, newName);
		content.put(PHONE, newNr);
		return db.update(TABLE_NAME, content, "NAME = ?", new String [] {name});
	}

	public Cursor returnData () {
		return db.query(TABLE_NAME, new String[] {NAME, PHONE}, null, null, null, null, null);
	}
}
