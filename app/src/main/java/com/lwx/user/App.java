package com.lwx.user;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by 36249 on 2017/4/4.
 */

public class App extends Application {

    private String token;

    private  static App sInstance;


    @Override
    public void onCreate() {

        super.onCreate();
        sInstance = this;
    }

    public static App getInstance(){

        return sInstance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }
}
