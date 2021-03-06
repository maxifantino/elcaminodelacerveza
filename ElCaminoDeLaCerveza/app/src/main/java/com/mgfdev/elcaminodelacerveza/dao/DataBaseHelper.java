package com.mgfdev.elcaminodelacerveza.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{

	private static final String dbname = "elcaminodb_ne.DB";

	public DataBaseHelper (Context context)
	{
		super (context, dbname, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String userTableScript = "CREATE TABLE USERS( " +
				"user_id" + " integer primary key autoincrement, " +
				"username" + " string ," +
				"email" + " string ," +
				"password" + " string, current_user  string)";

		String passportTableScript = "CREATE TABLE PASSPORT( " +
				"id" + " integer primary key autoincrement, " +
				"user_id" + " integer, " +
				"brewer" + " string, " +
				"sincronized" + " string, " +
				"date_created" + " string )";

		db.execSQL(userTableScript);
		db.execSQL(passportTableScript);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
}
