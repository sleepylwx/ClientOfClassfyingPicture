package com.lwx.user.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lwx.user.App;

/**
 * Created by 36249 on 2017/4/4.
 */

public class PreferenceHelper {

    private static final String LOGINUSERID = "LOGINUSERID";
    public void setLogInUID(long id){

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(LOGINUSERID,id);
        editor.apply();

    }

    public long getLogInUID(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return preferences.getLong(LOGINUSERID,-1);

    }

    public int getInt(String tag,int defaultValue){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return preferences.getInt(tag,defaultValue);
    }

    public void setInt(String tag,int value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(tag,value);
        editor.apply();
    }

    public boolean getBoolean(String tag,boolean defaultValue){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return preferences.getBoolean(tag,defaultValue);
    }

    public void setBoolean(String tag,boolean value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(tag,value);
        editor.apply();
    }

    public long getLong(String tag,long defValue){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return preferences.getLong(tag,defValue);
    }

    public void setLong(String tag,long value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(tag,value);
        editor.apply();
    }

    public static final String FAVORITE = "FAVORITE";

    public void setFavorite(String tag,String value){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FAVORITE+tag,value);
        editor.apply();

    }

    public String getFavorite(String tag){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return preferences.getString(FAVORITE+tag,null);
    }

}
