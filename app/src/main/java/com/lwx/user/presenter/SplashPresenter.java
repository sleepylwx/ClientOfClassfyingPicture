package com.lwx.user.presenter;

import android.os.SystemClock;
import android.util.Log;

import com.lwx.user.App;
import com.lwx.user.contracts.SplashContract;
import com.lwx.user.model.UserImpl;
import com.lwx.user.model.UserRepo;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.PreferenceHelper;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
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
            //checkTimeMatched(startTime);
            context.dispatch(0,-1,false);
            return;
        }

        userRepo.getToken(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {

                        if (s == null || s.equals("")) {

                            //checkTimeMatched(startTime);
                            context.dispatch(1,uid,false);

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

                                            isAuthFailed = false;
                                            //checkTimeMatched(startTime);
                                            App.getInstance().setToken(s);
                                            App.getInstance().setUid(uid);
                                            context.dispatch(2,uid,false);

                                        }

                                        @Override
                                        public void onError(Throwable e) {




                                            if(TOKENAUTHFAILED.equals(e.getMessage())){

                                                isAuthFailed = true;
                                                context.dispatch(3,uid,isAuthFailed);
                                                Log.d(TAG,"Auth failed, token incorrect!");
                                            }
                                            else{

                                                isAuthFailed = false;

                                                App.getInstance().setUid(uid);
                                                App.getInstance().setToken(s);
                                                context.dispatch(4,uid,false);
                                                Log.d(TAG,"Auth failed,network error!");
                                            }

                                            e.printStackTrace();
                                        }

                                    });
                        }
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {

                        context.dispatch(1,uid,false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void checkTimeMatched(long startTime){


        long minus = System.currentTimeMillis() - startTime;
        if(minus < WAIT_TIME){

            Log.d(TAG,"checkTimeMatched " + (WAIT_TIME - minus));
            SystemClock.sleep(WAIT_TIME - minus);
        }
    }

    @Override
    public SplashContract.View getView() {

        return context;
    }
}
