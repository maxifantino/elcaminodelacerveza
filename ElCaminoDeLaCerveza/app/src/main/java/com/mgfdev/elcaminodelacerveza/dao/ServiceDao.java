package com.mgfdev.elcaminodelacerveza.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.DateHelper;


public class ServiceDao {
	
	private static int RANK_TOTAL_NBR = 10;


	public User getUser (Context ctx, String username)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.query(true, "USER",new String[]{"user_id", "username", "password"},"username="+ username,
			null,
			null,
			null,
			null,
			null);
		User user=null;
		while (mCursor!= null && mCursor.getCount() > 0 && mCursor.moveToNext()){
			user = new User();
			user.setId(mCursor.getInt(0));
			user.setUsername(mCursor.getString(1));
			user.setPassword(mCursor.getString(2));
		}

		dbHelper.close();
		db.close();
		mCursor.close();
		return user;
	}

	public Map<String, String> getBrewers (Context ctx, int userId){
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.query(true, "PASSPORT",new String[]{"brewer", "created_date"},"user_id="+ userId,
				null,
				null,
				null,
				null,
				null);
		Map <String, String> brewers = new HashMap <String, String>();
		while (mCursor!= null && mCursor.getCount() > 0 && mCursor.moveToNext()){
			brewers.put(mCursor.getString(1), mCursor.getString(2));
		}

		dbHelper.close();
		db.close();
		mCursor.close();
		return brewers;
	}

	public void saveUser (Context ctx, User user)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues newRow = new ContentValues();
		newRow.put("user_name", user.getUsername());
		newRow.put("password", user.getPassword());
		db.insert("USER", null, newRow);
		db.close();
		dbHelper.close();
	}

	public void savePassportItem (Context ctx, int userId, String brewerName)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues newRow = new ContentValues();
		newRow.put("user_id", userId);
		newRow.put("brewer", brewerName);
		newRow.put("created_date", DateHelper.getDate(new Date()));
		db.insert("PASSPORT", null, newRow);
		db.close();
		dbHelper.close();
	}

}
