package com.lwx.user.contracts;

/**
 * Created by 36249 on 2017/4/4.
 */

public interface SplashContract {

    public interface View extends BaseContract.View<SplashContract.Presenter>{

        void jumpToLoginActivity(long uid,boolean isAuthFailed);
        void jumpToMainActivity(long uid);
        void onTokenAuthFailed();

    }

    public interface Presenter extends BaseContract.Presenter<SplashContract.View>{


        void doAutoLogin();
    }
}
