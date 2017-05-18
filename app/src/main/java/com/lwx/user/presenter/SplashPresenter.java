package com.lwx.user.presenter;

import android.os.SystemClock;
import android.util.Log;

import com.lwx.user.App;
import com.lwx.user.contracts.SplashContract;
import com.lwx.user.db.UserImpl;
import com.lwx.user.db.UserRepo;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.PreferenceHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
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

    private static final long WAIT_TIME = 2000;
    public static final String TAG = "SplashPresenter";

    public static final String TOKENAUTHFAILED = "Auth failed, token incorrect!";

    public SplashPresenter(SplashContract.View context) {

        this.context = context;
        userRepo = new UserImpl();
        loginAgent = UserAgentImpl.getInstance();

    }


    private boolean isAuthFailed;

    @Override
    public void doAutoLogin() {

        long startTime = System.currentTimeMillis();

        PreferenceHelper pHelper = new PreferenceHelper();
        long uid = pHelper.getLogInUID();
        if (uid == -1) {

            Log.d(TAG,"auto login error : no uid has logined");
            checkTimeMatched(startTime);
            context.jumpToLoginActivity(-1,false);
            return;
        }

        userRepo.getToken(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        if (s == null) {

                            checkTimeMatched(startTime);
                            context.jumpToLoginActivity(uid,true);
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

                                            isAuthFailed = true;
                                            checkTimeMatched(startTime);
                                            App.getInstance().setToken(s);
                                            App.getInstance().setUid(uid);
                                            context.jumpToMainActivity(uid);
                                        }

                                        @Override
                                        public void onError(Throwable e) {


                                            checkTimeMatched(startTime);

                                            if(TOKENAUTHFAILED.equals(e.getMessage())){

                                                isAuthFailed = true;
                                                context.onTokenAuthFailed();

                                            }
                                            else{

                                                context.onNetWorkError();
                                            }
                                            context.jumpToLoginActivity(uid,isAuthFailed);
                                            Log.d(TAG,"Auth failed, token incorrect!");
                                            e.printStackTrace();
                                        }

                                    });
                        }
                    }
                });

    }

    private void checkTimeMatched(long startTime){


        long minus = System.currentTimeMillis() - startTime;
        if(minus < WAIT_TIME){

            SystemClock.sleep(WAIT_TIME - minus);
        }
    }

    @Override
    public SplashContract.View getView() {

        return context;
    }
}
