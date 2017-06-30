package com.lwx.user.ui.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.lwx.user.R;
import com.lwx.user.contracts.SplashContract;
import com.lwx.user.presenter.SplashPresenter;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{


    private SplashContract.Presenter presenter;

    private long lastTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        lastTime = System.currentTimeMillis();

        presenter = new SplashPresenter(this);
        checkLogin();

    }



    @Override
    protected void onDestroy(){


        super.onDestroy();


        presenter = null;
    }

    private void checkLogin(){

        presenter.doAutoLogin();

    }

    public static final long WAIT_TIME = 3000;
    @Override
    public void dispatch(int param,long uid,boolean isAuthFailed) {


        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                long temp = System.currentTimeMillis() - lastTime;
                if(temp < WAIT_TIME){


                    SystemClock.sleep(WAIT_TIME - temp);
                }

                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        if(param == 0){

                            jumpToLoginActivity(uid,false);

                        }
                        else if (param == 1){

                            onTokenInvalid();
                            jumpToLoginActivity(uid,true);

                        }
                        else if(param == 2){

                            jumpToMainActivity(uid);
                        }
                        else if(param == 3){

                            onTokenAuthFailed();
                            jumpToLoginActivity(uid,isAuthFailed);
                        }
                        else{

                            onNetWorkError();
                            jumpToMainActivity(uid);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });


    }

    @Override
    public void jumpToLoginActivity(long uid,boolean isAuthFailed) {


        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra(LoginActivity.MATCH_NUM,uid);
        if(isAuthFailed){

            intent.putExtra(LoginActivity.ISAUTHFAILED,true);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void jumpToMainActivity(long uid) {

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(MainActivity.MATCH_NUM,uid);
        startActivity(intent);
        finish();
    }



    @Override
    public SplashContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenAuthFailed() {

        Toast.makeText(this,R.string.token_auth_failed,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenInvalid() {

        Toast.makeText(this,"登录状态失效，请重新登录",Toast.LENGTH_SHORT).show();
    }
}
