package com.mgfdev.elcaminodelacerveza.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Maxi on 03/12/2017.
 */

public class SharedPreferenceManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context ctx;


    public SharedPreferenceManager(Context ctx){
        this.ctx = ctx;
        pref = ctx.getSharedPreferences("config", 0);
        editor = pref.edit();
    }

    public void storeValue(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringValue(String key){
        return pref.getString(key, null);
    }

    public void removeValue(String key){
        editor.remove(key);
        editor.commit();
    }

}
