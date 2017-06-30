package com.lwx.user.contracts;

/**
 * Created by 36249 on 2017/4/4.
 */

public interface SplashContract {

    public interface View extends BaseContract.View<SplashContract.Presenter>{

        void jumpToLoginActivity(long uid,boolean isAuthFailed);
        void jumpToMainActivity(long uid);
        void onTokenAuthFailed();

        void onTokenInvalid();

        void dispatch(int param,long uid,boolean isAuthFailed);

    }

    public interface Presenter extends BaseContract.Presenter<SplashContract.View>{


        void doAutoLogin();
    }
}
