package com.lwx.user.contracts;

/**
 * Created by 36249 on 2017/4/10.
 */

public interface SignUpContract {

    public interface View extends BaseContract.View<SignUpContract.Presenter>{

        void onSignUpSucceed(String user);

    }

    public interface Presenter extends BaseContract.Presenter<SignUpContract.View>{


        void doSignUp(String user,String passwd);
    }
}
