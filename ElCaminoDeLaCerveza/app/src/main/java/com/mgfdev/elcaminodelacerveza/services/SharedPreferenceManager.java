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
    private static SharedPreferenceManager instance;

    public static SharedPreferenceManager getInstance(Context ctx){
        if (instance == null){
            instance = new SharedPreferenceManager(ctx);
        }
        return instance;
    }

    public SharedPreferenceManager(Context ctx){
        this.ctx = ctx;
        pref = ctx.getSharedPreferences("config", 0);
    }

    public void storeValue(String key, String value){
        pref.edit().putString(key, value).apply();
    }

    public String getStringValue(String key){
        String result;
        if (key .equals("meters")) {
            String aux =  pref.getString(key, "200");
            result = Integer.parseInt(aux)< 200 ? "200" : aux;
        }
        else{
            result =  pref.getString(key, null);
        }
        return result;
    }

}
