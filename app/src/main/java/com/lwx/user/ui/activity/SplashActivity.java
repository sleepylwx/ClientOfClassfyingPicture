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
    public void jumpToLoginActivity() {

        Intent intent = new Intent(this,LoginActivity.class);
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

}
