package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

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
    public void jumpToLoginActivity() {

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void jumpToMainActivity() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
