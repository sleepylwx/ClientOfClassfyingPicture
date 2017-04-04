package com.lwx.user.contracts;

/**
 * Created by 36249 on 2017/4/4.
 */

public interface SplashContract {

    interface View{

        void jumpToLoginActivity();
        void jumpToMainActivity();
        void showNetworkFailure();

    }

    interface Presenter{


        void doAutoLogin();
    }
}
