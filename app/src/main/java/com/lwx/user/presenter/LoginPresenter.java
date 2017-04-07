package com.lwx.user.presenter;

import android.content.Context;

import com.lwx.user.contracts.LoginContract;
import com.lwx.user.db.UserImpl;
import com.lwx.user.db.UserRepo;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/4.
 */

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View context;

    private UserRepo userDetailRepo;

    private UserAgent userAgent;
    public LoginPresenter(LoginContract.View context){

        this.context = context;
        userDetailRepo = new UserImpl();
        userAgent = UserAgentImpl.getInstance();
    }

    @Override
    public void loadAllUserDetails() {

        userDetailRepo.getAllUserDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->context.onAllUserDetailLoaded(s));

    }

    @Override
    public void login(String user, String passwd) {


    }
}
