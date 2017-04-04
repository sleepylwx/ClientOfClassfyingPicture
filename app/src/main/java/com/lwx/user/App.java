package com.lwx.user;

import android.app.Application;
import android.support.annotation.NonNull;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

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

        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);

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
