package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lwx.user.R;
import com.lwx.user.adapter.RecyclerViewAdapter;
import com.lwx.user.contracts.MainContract;
import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.User;
import com.lwx.user.presenter.MainPresenter;
import com.lwx.user.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements MainContract.View{


    @BindView(R.id.headerImageView) ImageView headerImageView;
    @BindView(R.id.headerText1) TextView nickNameText;
    @BindView(R.id.flush) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    public static final String MATCH_NUM = "1";
    private MainContract.Presenter presenter;
    private ImageLoader imageLoader;

    private User curUser;
    private RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);
        imageLoader = new ImageLoader();

        init();


    }

    private void init(){

        initUser();
        initPicture();

    }
    private void initUser(){


        Intent intent = getIntent();
        int uid = intent.getIntExtra(MATCH_NUM,-1);

        if(uid ==-1){

            Intent intent1 = new Intent(this,LoginActivity.class);
            startActivity(intent1);
            finish();
        }
        else{

            presenter.getUser(uid);
        }

    }

    private void initPicture(){

        presenter.getPictures();

    }

    @Override
    public MainContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUserLoadedFailed() {

        Toast.makeText(this,R.string.user_loaded_failed,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserLoadedSucceed(User user) {

        curUser = user;
        imageLoader.loadImage(this,curUser.headPath,headerImageView);
        nickNameText.setText(curUser.nickName);
    }

    @Override
    public void onImageLoadedSucceed(List<Image> imageList) {

        initRecycleView(imageList == null ? new ArrayList<Image>() : imageList);
    }

    private void initRecycleView(List<Image> imageList){

        adapter = new RecyclerViewAdapter(this,imageList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }


}
