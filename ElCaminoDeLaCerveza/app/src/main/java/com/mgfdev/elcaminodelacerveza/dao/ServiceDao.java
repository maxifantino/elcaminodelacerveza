package com.mgfdev.elcaminodelacerveza.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mgfdev.elcaminodelacerveza.dto.Passport;
import com.mgfdev.elcaminodelacerveza.dto.PassportItem;
import com.mgfdev.elcaminodelacerveza.dto.PassportItemDto;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.DateHelper;


public class ServiceDao {
	
	private static int RANK_TOTAL_NBR = 10;

    public User getLoggedUser (Context ctx){
        DataBaseHelper dbHelper = new DataBaseHelper(ctx);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor mCursor = null;
       try{
		    mCursor = db.query(true, "USERS",new String[]{"user_id", "username", "email", "password"},"current_user='y'",
				   null,
				   null,
				   null,
				   null,
				   null);
	   }
	   catch (Exception e){
		   e.printStackTrace();
	   }
        User user=null;
        while (mCursor!= null && mCursor.getCount() > 0 && mCursor.moveToNext()){
            user = new User();
            user.setId(mCursor.getInt(0));
            user.setUsername(mCursor.getString(1));
			user.setEmail(mCursor.getString(2));
            user.setPassword(mCursor.getString(3));
        }

        dbHelper.close();
        db.close();
        mCursor.close();
        return user;
    }


	public void markUserAsLogged (Context ctx, Integer id){
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int result = -1;
		String strFilter = (MessageFormat.format( "user_id={0}", "'" + id + "'"));
		ContentValues args = new ContentValues();
		args.put("current_user", "y");
		try{
			result = db.update("USERS", args, strFilter, null);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		dbHelper.close();
		db.close();
	}



	public boolean doLogout (Context ctx, User user){
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int result = -1;
		String strFilter = (MessageFormat.format( "username={0} and password={1}", "'" + user.getUsername() + "'", "'"+user.getPassword()+"'"));
		ContentValues args = new ContentValues();
		args.put("current_user", "n");
		try{
			result = db.update("USERS", args, strFilter, null);
		}
		catch (Exception e){
			e.printStackTrace();
		}

		db.close();
		return result > 0 ;
	}

	public User getUser (Context ctx, String username)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		User user=null;
		Cursor mCursor = null;
		try{
			mCursor = db.query(true, "USERS",new String[]{"user_id", "username", "password"},"username="+ "'" + username + "'",
					null,
					null,
					null,
					null,
					null);
			while (mCursor!= null && mCursor.getCount() > 0 && mCursor.moveToNext()){
				user = new User();
				user.setId(mCursor.getInt(0));
				user.setUsername(mCursor.getString(1));
				user.setPassword(mCursor.getString(2));
			}
		}

		catch (Exception e) {
			Log.e("serviceDao", "Error getting user " + e.getMessage());
		}
		dbHelper.close();
		db.close();
		if (mCursor !=null) {
			mCursor.close();
		}
		return user;
	}

	public void saveUser (Context ctx, User user)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// update old users
		Cursor mCursor = null;
		try{
			mCursor = db.rawQuery(" UPDATE USERS  set current_user = 'n'",null) ;
		}
		catch (Exception e){
			e.printStackTrace();
		}

        mCursor.close();
		// check if user wasn't logged before

        // create new user
        ContentValues newRow = new ContentValues();
		newRow.put("username", user.getUsername());
		newRow.put("password", user.getPassword());
		newRow.put("email", user.getEmail());
        newRow.put("current_user", "y");

        Long userID = db.insert("USERS", null, newRow);
		user.setId(userID.intValue());
		db.close();
		dbHelper.close();
	}

	public void savePassport (Context ctx, int userId, Passport passport){
		for (PassportItem item: passport.getBrewers()) {
			Integer count = item.getVisitsCount();
			// como solo importa la ultima fecha, directamente inserto todas las ocurrencias con la misma.
			// el valor real se encuentra en el server
			while (count >= 1){
				Long passportId = this.savePassportItem(ctx, userId, item.getBrewerName(), item.getLastVisit());
				sincronyzePassportItem(ctx,passportId);
				count --;
			}
		}
	}

	public Long savePassportItem (Context ctx, int userId, String brewerName, Date dateVisit)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues newRow = new ContentValues();
		dateVisit = dateVisit == null?new Date(): dateVisit;
		newRow.put("user_id", userId);
		newRow.put("brewer", brewerName);
		newRow.put("sincronized", "n");
		newRow.put("date_created",  DateHelper.getDate(dateVisit));
		Long passportId = db.insert("PASSPORT", null, newRow);
		db.close();
		dbHelper.close();
		return passportId;
	}

	public void sincronyzePassportItem (Context ctx, long passportId)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try{
			ContentValues values = new ContentValues();
			values.put("sincronized", "y");
			String[] whereArgs = {String.valueOf(passportId)};

			db.update("PASSPORT", values, "id = ?", whereArgs);

		}
		catch (Exception e){
			e.printStackTrace();
		}
		db.close();
		dbHelper.close();
	}

	public List<PassportItemDto> getDesyncronizedItems (Context ctx, int userId)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor mCursor = null;
		List<PassportItemDto> items = new ArrayList<PassportItemDto>();
		try{
			mCursor = db.query(true, "PASSPORT",new String[]{"id, user_id, brewer, sincronized, date_created"}," sincronized= 'n' and user_id=" + userId ,
					null,
					null,
					null,
					null,
					null);
			while (mCursor!= null && mCursor.getCount() > 0 && mCursor.moveToNext()){
				PassportItemDto item = new PassportItemDto();
				item.setPassportItem(mCursor.getInt(0));
				item.setBrewerName(mCursor.getString(2));
				item.setUserId(mCursor.getInt(1));
				item.setSincronized(mCursor.getString(3));
				item.setDateVisit(mCursor.getString(4));

				items.add(item);
			}
		}
		catch (Exception e) {
			Log.e("serviceDao", "Error getting user " + e.getMessage());
		}
		dbHelper.close();
		db.close();
		if (mCursor !=null) {
			mCursor.close();
		}
		return items;
	}


	public void sincronyzePassport (Context ctx, int userId)
	{
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// update old users
		Cursor mCursor = null;
		try{
			mCursor = db.rawQuery(" UPDATE PASSPORT  set sincronized = 'y' where sincronized= 'n' and user_id=" + userId,null) ;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		db.close();
		dbHelper.close();
	}


	public Passport getPassport (Context ctx, int userId){
		Passport passport =  new Passport();
		DataBaseHelper dbHelper = new DataBaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.query(false, "PASSPORT",new String[]{"brewer", "date_created"},"user_id="+ userId,
				null,
				null,
				null,
				null,
				null);
		while (mCursor!= null && mCursor.getCount() > 0 && mCursor.moveToNext()){
			passport.addBrewer(mCursor.getString(0), DateHelper.getDate(mCursor.getString(1)));
		}

		dbHelper.close();
		db.close();
		mCursor.close();
		return passport;
	}
}
