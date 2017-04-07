package com.lwx.user.presenter;

import com.lwx.user.contracts.SplashContract;
import com.lwx.user.db.UserImpl;
import com.lwx.user.db.UserRepo;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.PreferenceHelper;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/4.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View context;
    private UserRepo userRepo;
    private UserAgent loginAgent;


    public SplashPresenter(SplashContract.View context) {

        this.context = context;
        userRepo = new UserImpl();
        loginAgent = UserAgentImpl.getInstance();

    }


    @Override
    public void doAutoLogin() {

        PreferenceHelper pHelper = new PreferenceHelper();
        int uid = pHelper.getLogInUID();
        if (uid == -1) {


            context.jumpToLoginActivity();
            return;
        }

        userRepo.getToken(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        if (s == null) {

                            context.jumpToLoginActivity();
                        } else {

                            loginAgent.auth(s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CompletableObserver() {

                                        @Override
                                        public void onSubscribe(Disposable d) {


                                        }

                                        @Override
                                        public void onComplete() {

                                            context.jumpToMainActivity();
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                            context.jumpToLoginActivity();
                                            e.printStackTrace();
                                        }

                                    });
                        }
                    }
                });

    }


}
