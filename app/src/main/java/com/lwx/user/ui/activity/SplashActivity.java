package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.lwx.user.R;
import com.lwx.user.contracts.SplashContract;
import com.lwx.user.presenter.SplashPresenter;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{


    private SplashContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        presenter = new SplashPresenter(this);
        checkLogin();
    }

    @Override
    protected void onDestroy(){

        presenter = null;
        super.onDestroy();

    }

    private void checkLogin(){

        presenter.doAutoLogin();

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
