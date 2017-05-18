package com.lwx.user.debug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elvishew.xlog.XLog;
import com.lwx.user.R;
import com.lwx.user.db.ImageImpl;
import com.lwx.user.db.UserImpl;
import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.User;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DebugActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClick(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAgent userAgent = UserAgentImpl.getInstance();
                userAgent.login("testuser1","pass1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(@NonNull User user) throws Exception {
                                userAgent.uploadHeadPic(user.token, "/storage/emulated/0/header.png")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe();
                            }
                        });
            }
        }).start();
/*
        ArrayList arrayList = new ArrayList();
        arrayList.add("SAASD");
        ImageImpl image = ImageImpl.getInstance();
        image.saveImage(new Image("DDD", "DDDD")).subscribe();*/
    }
}
