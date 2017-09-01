package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.adapter.RecyclerViewAdapter;
import com.lwx.user.contracts.HistoryImageContract;
import com.lwx.user.model.model.Image;
import com.lwx.user.presenter.HistoryImagePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryImageActivity extends AppCompatActivity implements HistoryImageContract.View{


    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.toolbar_textview)TextView textView;
    @BindView(R.id.recyclerview)RecyclerView recyclerView;
    @BindView(R.id.textview_content_main1)TextView textView1;

    public static final String TITLE = "2";


    private String title;


    private RecyclerViewAdapter adapter;
    private List<Image> list;
    private HistoryImageContract.Presenter presenter;

    private Menu menu;
    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main1);
        ButterKnife.bind(this);

        presenter = new HistoryImagePresenter(this);
        list = new ArrayList<>();

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(!title.equals("标记过的图片")){

            return false;
        }
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mode == 0){

            clearAdapterData();

            mode = 1;

            textView1.setText("还没有已完成的图片喔~");
            presenter.getFinishedImages(App.getInstance().getUid());
            menu.getItem(0).setTitle("全部");
        }
        else{

            clearAdapterData();
            mode = 0;
            menu.getItem(0).setTitle("已完成");
            textView1.setText("还没有被标记的图片哦(⊙o⊙)");
            initPicture();
        }

        return super.onOptionsItemSelected(item);

    }

    private void clearAdapterData(){

        textView1.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        list = new ArrayList<>();
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
        adapter = null;
    }

    private void init(){

        initState();
        initToolBar();
        initRecyclerView();

        initPicture();
    }

    private void initState(){

        Intent intent = getIntent();


        title = intent.getStringExtra(TITLE);
        if(title == null){

            title = "标记过的图片";
        }


    }
    private void initToolBar(){

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        textView.setVisibility(View.GONE);
    }

    private void initRecyclerView(){


        adapter = new RecyclerViewAdapter(this,this.list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void initPicture(){

        presenter.getMarkedPics(App.getInstance().getUid(),title);
    }

    @Override
    public void onImageLoadedSucceed(List<Image> imageList) {

        recyclerView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.GONE);
        list.addAll(imageList);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onImageLoadedFailed() {

        recyclerView.setVisibility(View.GONE);
        textView1.setVisibility(View.VISIBLE);
    }

    @Override
    public HistoryImageContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();

    }

    private int position;
    @Override
    public void jumpToImageDetailActivity(String uuid,int position) {

        this.position = position;
        Intent intent = new Intent(this, ImageDetailActivity.class);
        intent.putExtra(ImageDetailActivity.IMAGEUUID,uuid);
        intent.putExtra(ImageDetailActivity.ISLABELED,true);
        intent.putExtra(ImageDetailActivity.TITLE,title);
        startActivityForResult(intent,REQUESTCODE);
    }

    public static final int RESULTCODE = 1;
    public static final int REQUESTCODE = 2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){

            case RESULTCODE:
                list.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,list.size()-position);
                if(list.size() == 0){

                    finish();
                    break;
                }

                break;
        }

    }
}
