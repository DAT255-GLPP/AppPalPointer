package com.example.palpointer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {
	private static final String NAME = "name";
	private static final String PHONE = "phone";
	private static final String TABLE_NAME = "mytable";
	private static final String DATA_BASE_NAME = "mydatabase";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_CREATE = "create table mytable (name text not null primary key, phone text not null);";

	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public DataHandler (Context ctx){
		this.ctx = ctx;
		dbhelper = new DataBaseHelper(ctx);
	}

	//Extending SQLiteOpenHelper class to get its methods
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
	
	/**
	 * Opening the database
	 */
	public DataHandler open(){
		db = dbhelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closing the database
	 */ 
	public void close() {
		dbhelper.close();
	}
	
	/**
	 * Insert contact name and phone number into database
	 */  
	public long insertData (String name, String phone) {
		ContentValues content = new ContentValues();
		content.put(NAME, name);
		content.put(PHONE, phone);
		return db.insertOrThrow(TABLE_NAME, null, content);
	}

	/**
	 * Deleting a contact with specified name and phone number
	 */  
	public int deleteData(String name, String phone) {
		return db.delete(TABLE_NAME, "NAME = ? AND PHONE = ?", new String [] {name, phone});	
	}


	/**
	 * Updates a contact with new name and phone number
	 */ 
	public int updateData(String name, String phone, String newName, String newNr) {
		ContentValues content = new ContentValues();
		content.put(NAME, newName);
		content.put(PHONE, newNr);
		return db.update(TABLE_NAME, content, "NAME = ? AND PHONE = ?", new String [] {name, phone});
	}
	
	/**
	 * Returns data from the table
	 */
	public Cursor returnData () {
		return db.query(TABLE_NAME, new String[] {NAME, PHONE}, null, null, null, null, null);
	}
}
