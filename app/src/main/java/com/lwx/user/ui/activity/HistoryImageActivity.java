package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lwx.user.R;
import com.lwx.user.adapter.RecyclerViewAdapter;
import com.lwx.user.contracts.HistoryImageContract;
import com.lwx.user.db.model.Image;
import com.lwx.user.presenter.HistoryImagePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryImageActivity extends AppCompatActivity implements HistoryImageContract.View{


    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.toolbar_textview)TextView textView;
    @BindView(R.id.recyclerview)RecyclerView recyclerView;

    public static final String USERID = "1";

    private long uid;

    private RecyclerViewAdapter adapter;
    private List<Image> list;
    private HistoryImageContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_image);
        ButterKnife.bind(this);

        presenter = new HistoryImagePresenter(this);
        list = new ArrayList<>();

        init();
    }

    public HistoryImageActivity() {
        super();

        presenter = null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){

        initState();
        initToolBar();
        initRecyclerView();

        initPicture();
    }

    private void initState(){

        Intent intent = getIntent();
        uid = intent.getLongExtra(USERID,-1);
        if(uid == -1){

            Toast.makeText(this,"登录状态有误",Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    private void initToolBar(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        textView.setText("标记的图片");
    }

    private void initRecyclerView(){


        adapter = new RecyclerViewAdapter(this,this.list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void initPicture(){

        presenter.getLabeledImageInDb(uid);
    }

    @Override
    public void onImageLoadedSucceed(List<Image> imageList) {


        list.addAll(imageList);

        adapter.notifyDataSetChanged();
    }


    @Override
    public HistoryImageContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {


    }
}
