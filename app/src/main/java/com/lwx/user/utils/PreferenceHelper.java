package com.lwx.user.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lwx.user.App;

/**
 * Created by 36249 on 2017/4/4.
 */

public class PreferenceHelper {

    private static final String LOGINUSERID = "LOGINUSERID";
    public void setLogInUID(int id){

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(LOGINUSERID,id);
        editor.apply();

    }

    public int getLogInUID(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return preferences.getInt(LOGINUSERID,-1);

    }
}
