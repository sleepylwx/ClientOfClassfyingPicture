package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.contracts.SignUpContract;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/10.
 */

public class SignUpPresenter implements SignUpContract.Presenter {


    private SignUpContract.View context;
    private UserAgent userAgent;

    public static final String TAG = "SignUpPresenter";
    public SignUpPresenter(SignUpContract.View context){

        this.context = context;
        userAgent = UserAgentImpl.getInstance();
    }

    private static final String SIGNUPFAILED = "Register failed, username already exist!";
    @Override
    public void doSignUp(String user, String passwd) {

        userAgent.signUp(user,passwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        context.onSignUpSucceed(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if(SIGNUPFAILED.equals(e.getMessage())){

                            context.onUserHasExist();
                        }
                        else{

                            context.onNetWorkError();
                        }

                        Log.d(TAG,"doSignUp onError" + user + " " + passwd
                        +" message:" + e.getMessage());
                    }
                });
    }

    @Override
    public SignUpContract.View getView() {

        return context;
    }

}
