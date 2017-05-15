package com.lwx.user.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.query.In;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.adapter.RecyclerViewAdapter;
import com.lwx.user.contracts.MainContract;
import com.lwx.user.db.ImageRepo;
import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.User;
import com.lwx.user.presenter.MainPresenter;
import com.lwx.user.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements MainContract.View{


    //@BindView(R.id.headerImageView) ImageView headerImageView;
    //@BindView(R.id.headerText1) TextView nickNameText;
    //@BindView(R.id.toolbar) StrenthenToolBar strenthenToolBar;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.nav_view)NavigationView navigationView;
    @BindView(R.id.flush) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.drawer_layout)DrawerLayout drawerLayout;
    CircleImageView headerImageView;

    public static final String MATCH_NUM = "1";
    private MainContract.Presenter presenter;
    private ImageLoader imageLoader;

    private User curUser;
    private RecyclerViewAdapter adapter;
    private List<Image> list;
    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);
        imageLoader = new ImageLoader();
        list = new ArrayList<>();
        //headerImageView =strenthenToolBar.getHeaderView();

        //
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //
        init();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        presenter = null;
    }

    private void initNavigationView(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_camera) {
                    // Handle the camera action
                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }

                drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void init(){

        initNavigationView();
        initSwipeRefresh();
        initUser();
        initPicture();

    }

    private void initSwipeRefresh(){

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                presenter.clearAndGetMorePicByNetWork(App.getInstance().getUid(),App.getInstance().getpullPicNum());
            }
        });
    }
    private void initUser(){


        Intent intent = getIntent();
        long uid = intent.getLongExtra(MATCH_NUM,-1);

        if(uid ==-1){

            Intent intent1 = new Intent(this,LoginActivity.class);
            startActivity(intent1);
            finish();
        }
        else{

            App.getInstance().setUid(uid);
            presenter.getUser(uid);
        }

    }

    private void initPicture(){

        presenter.getPictures(App.getInstance().getpullPicNum());

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
    public RecyclerView getRecyclerView() {


        return recyclerView;
    }

    @Override
    public void onUserLoadedSucceed(User user) {

        curUser = user;
        CircleImageView header = (CircleImageView)((ViewGroup)navigationView.getHeaderView(0)).getChildAt(0);
        TextView userName = (TextView)((ViewGroup)navigationView.getHeaderView(0)).getChildAt(1);
        imageLoader.loadImage(this,user.headPath,header);
        userName.setText(user.nickName);

        //strenthenToolBar.setHeaderPicture(imageLoader,user.headPath);
        //nickNameText.setText(curUser.nickName);
    }

    @Override
    public void onImageLoadedSucceed(List<Image> imageList) {

        Log.d(TAG,"imageLoaded succeed!");
        initRecycleView(imageList);
    }

    @Override
    public void clearAndSaveList(List<Image> imageList) {

        if(imageList != null){

            this.list = imageList;
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecycleView(List<Image> imageList){

        if(imageList != null){

            list.addAll(imageList);
        }
        Log.d(TAG,"" + list.size());
        adapter = new RecyclerViewAdapter(this,list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void startGetMorePicByNetWork() {

        showWaitingNetWork();
        presenter.getMorePicturesByNetWork(App.getInstance().getUid(),App.getInstance().getpullPicNum());

    }

    @Override
    public void jumpToImageDetailActivity(String uuid) {

        Intent intent = new Intent(this, ImageDetailActivity.class);
        intent.putExtra(ImageDetailActivity.IMAGEUUID,uuid);
        startActivity(intent);

    }

    @Override
    public void showWaitingNetWork() {


    }

    @Override
    public void nonShowWaitingNetWork() {


    }

    @Override
    public void nonShowSwipe() {

        if(swipeRefresh.isRefreshing()){

            swipeRefresh.setRefreshing(false);
        }

    }


}
