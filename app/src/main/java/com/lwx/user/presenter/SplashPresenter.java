package com.lwx.user.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;

import com.lwx.user.contracts.SplashContract;
import com.lwx.user.db.UserImpl;
import com.lwx.user.db.UserRepo;
import com.lwx.user.net.LoginAgent;
import com.lwx.user.ui.LoginActivity;
import com.lwx.user.utils.PreferenceHelper;

import org.reactivestreams.Subscriber;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/4.
 */

public class SplashPresenter implements SplashContract.Presenter{

    private SplashContract.View context;
    private UserRepo userRepo;
    private LoginAgent loginAgent;


    public SplashPresenter(SplashContract.View context){

        this.context = context;
        userRepo = new UserImpl();
        loginAgent = new LoginImpl();

    }



    @Override
    public void doAutoLogin() {

        PreferenceHelper pHelper = new PreferenceHelper();
        int uid = pHelper.getLogInUID();
        if(uid == -1){


            context.jumpToLoginActivity();
            return;
        }

        userRepo.getToken(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }


}
