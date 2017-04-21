package com.lwx.user;

import android.app.Application;
import android.support.annotation.NonNull;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by 36249 on 2017/4/4.
 */

public class App extends Application {

    private String token;

    private  static App sInstance;

    public static final String BASE_URL = "http://ttxs.ac.cn:8088/";

    @Override
    public void onCreate() {

        super.onCreate();
        sInstance = this;

        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG ? LogLevel.ALL             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
                        : LogLevel.NONE)
                .tag("CNSOFTBEI")                                         // Specify TAG, default: "X-LOG"
                .t()                                                   // Enable thread info, disabled by default
                .st(2)                                                 // Enable stack trace info with depth 2, disabled by default
                .b()                                                   // Enable border, disabled by default
                .build();

        XLog.init(config);

        RxJavaPlugins.setErrorHandler(t->{XLog.e("Unhandled Exception By RxJava" , t);});
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
