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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.adapter.RecyclerViewAdapter;
import com.lwx.user.contracts.MainContract;
import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.User;
import com.lwx.user.presenter.MainPresenter;
import com.lwx.user.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements MainContract.View {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.flush)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    CircleImageView headerImageView;

    public static final String MATCH_NUM = "1";
    public static final int RESULTCODE = 2;
    private MainContract.Presenter presenter;
    private ImageLoader imageLoader;


    private RecyclerViewAdapter adapter;
    private List<Image> list;

    private Menu menu;
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
        toolbar.setTitle("");
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

    @Override
    public void onBackPressed() {

        moveTaskToBack(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (userMode == 0) {

            userMode = 1;
            menu.getItem(0).setTitle("随机推送");
            presenter.clearAndGetMoreRandomPicByNet(App.getInstance().getUid(), App.getInstance().getpullPicNum());
        } else {

            userMode = 0;
            menu.getItem(0).setTitle("用户推送");
            presenter.clearAndGetPicByNetWork(App.getInstance().getUid(),
                    App.getInstance().getToken(), App.getInstance().getpullPicNum());
        }

        return super.onOptionsItemSelected(item);

    }

    private void initNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.nav_dailytask){

                    Intent intent = new Intent(MainActivity.this,DailyTaskActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.nav_label) {


                    Intent intent = new Intent(MainActivity.this, HistoryLabelActivity.class);
                    //intent.putExtra(HistoryLabelActivity.USERID,App.getInstance().getUid());
                    startActivity(intent);

                } else if (id == R.id.nav_image) {

                    Intent intent = new Intent(MainActivity.this, HistoryImageActivity.class);
                    //intent.putExtra(HistoryImageActivity.USERID,App.getInstance().getUid());
                    startActivity(intent);
                } else if (id == R.id.nav_settting) {


                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);

                }else if(id == R.id.nav_feedback){

                    Intent intent = new Intent(MainActivity.this,FeedBackActivity.class);
                    startActivity(intent);

                }
                else if (id == R.id.nav_exit) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(LoginActivity.MATCH_NUM, App.getInstance().getUid());

                    Log.d(TAG, "exit login" + " " + App.getInstance().getUid());
                    startActivity(intent);
                    finish();
                }


                drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void init() {


        initNavigationView();
        initSwipeRefresh();
        initRecycleView();

        initUser();
        initPicture();

    }

    private int userMode;

    private void initSwipeRefresh() {

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (userMode == 0) {

                    presenter.clearAndGetPicByNetWork(App.getInstance().getUid()
                            , App.getInstance().getToken(), App.getInstance().getpullPicNum());
                } else {

                    presenter.clearAndGetMoreRandomPicByNet(App.getInstance().getUid(), App.getInstance().getpullPicNum());
                }

            }
        });
    }

    private void initUser() {


        Intent intent = getIntent();
        long uid = intent.getLongExtra(MATCH_NUM, -1);

        if (uid == -1) {

            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
            finish();
        } else {

            //App.getInstance().setUid(uid);
            presenter.getUser(uid);
        }

    }

    private void initPicture() {

        presenter.getPictures(App.getInstance().getUid(), App.getInstance().getToken(), App.getInstance().getpullPicNum());

    }

    @Override
    public MainContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUserLoadedFailed() {

        Toast.makeText(this, R.string.user_loaded_failed, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onUserLoadedSucceed(User user) {

        presenter.saveUser(user);
        //curUser = user;
        CircleImageView header = (CircleImageView) ((ViewGroup) navigationView.getHeaderView(0)).getChildAt(0);
        TextView userName = (TextView) ((ViewGroup) navigationView.getHeaderView(0)).getChildAt(1);
        LinearLayout linearLayout = (LinearLayout) navigationView.getHeaderView(0);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                //intent.putExtra(UserDetailActivity.USERID, App.getInstance().getUid());
                startActivityForResult(intent,REQUESTCODE);
            }
        });

        Log.d(TAG, "userloadsuccess! " + user.headPath);
        if(user.headPath == null || user.headPath.equals("")){

            imageLoader.loadImage(this,R.mipmap.ic_launcher,header);
        }
        else{

            //imageLoader.loadImage(this, user.headPath, header);
            Glide.with(this)
                    .load(user.headPath)
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .error(R.mipmap.ic_launcher)
                    .into(header);
        }

        userName.setText(user.nickName);

        //strenthenToolBar.setHeaderPicture(imageLoader,user.headPath);
        //nickNameText.setText(curUser.nickName);
    }

    @Override
    public void onImageLoadedSucceed(List<Image> imageList) {

        Log.d(TAG, "imageLoaded succeed!");

        for (int i = 0; i < imageList.size(); ++i) {

            Log.d(TAG, "uuid:" + imageList.get(i).uuid + " path:" + imageList.get(i).imagePath);
        }
        canScroll = true;

        addImages(imageList);

    }

    @Override
    public void clearAndSaveList(List<Image> imageList) {

        if (imageList != null) {

            this.list = imageList;
        }
        Log.d(TAG, "clear and save list");
        adapter.setData(this.list);
        adapter.notifyDataSetChanged();
    }

    private void addImages(List<Image> images) {

        this.list.addAll(images);
        //adapter.addData(images);
        adapter.notifyDataSetChanged();
    }

    private boolean canScroll;


    private void initRecycleView() {

//        if(imageList != null){
//
//            list.addAll(imageList);
//        }
        Log.d(TAG, "" + list.size());
        adapter = new RecyclerViewAdapter(this, this.list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (canScroll && isRecyclerViewBootom()) {

                    canScroll = false;

                    if (userMode == 0) {

                        startGetMorePicByNetWork();
                    } else {

                        presenter.getMoreRandomPicturesByNetWork(App.getInstance().getUid(), App.getInstance().getpullPicNum());
                    }

                }
            }
        });

    }

    @Override
    public void startGetMorePicByNetWork() {

        //showWaitingNetWork();
        presenter.getMorePicturesByNetWork(App.getInstance().getUid()
                , App.getInstance().getToken(), App.getInstance().getpullPicNum());

    }

    private int position;

    public static final int REQUESTCODE = 1;
    @Override
    public void jumpToImageDetailActivity(String uuid, int position) {

        Log.d(TAG,"position + " + position);
        this.position = position;
        Intent intent = new Intent(this, ImageDetailActivity.class);
        intent.putExtra(ImageDetailActivity.IMAGEUUID, uuid);
        startActivityForResult(intent,REQUESTCODE);

    }

    @Override
    public void showWaitingNetWork() {


    }

    @Override
    public void nonShowWaitingNetWork() {


    }

    @Override
    public void nonShowSwipe() {

        if (swipeRefresh.isRefreshing()) {

            swipeRefresh.setRefreshing(false);
        }

    }


    private boolean isRecyclerViewBootom() {

        if (recyclerView == null) {

            return false;
        }

        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {

            return true;
        }

        return false;

    }

//    @Override
//    public void onImageAddedSucceed(List<Image> imageList) {
//
//        addImages(imageList);
//    }


    @Override
    public void onLoadPicInDbError() {

        Toast.makeText(this, "读取缓存图片失败，下拉刷新试试吧...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageLoadedFailed() {

        canScroll = true;
    }

    @Override
    public void jumpToLoginActivityForTokenError() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.MATCH_NUM, App.getInstance().getUid());
        intent.putExtra(LoginActivity.ISAUTHFAILED, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTokenError() {

        Toast.makeText(this, R.string.token_auth_failed, Toast.LENGTH_SHORT).show();
    }

    public static final int RESULTCODE1 = 10;
    public static final int RESULTCODE2 = 20;
    public static final int RESULTCODE3 = 30;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"resultcode " + resultCode);
        switch (resultCode){


            case RESULTCODE:
                Log.d(TAG,"delete unSignedLabel");
                list.remove(position);
                Log.d(TAG,"position size " + list.size());
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,list.size()-position);
                break;

            case RESULTCODE1:
                String a = data.getStringExtra(UserDetailActivity.NICKNAME);
                TextView textView =  (TextView) ((ViewGroup) navigationView.getHeaderView(0)).getChildAt(1);
                Log.d(TAG,"a = " + a);
                textView.setText(a);
                break;
            case RESULTCODE2:
                String b = data.getStringExtra(UserDetailActivity.HEADERPATH);
                CircleImageView header = (CircleImageView) ((ViewGroup) navigationView.getHeaderView(0)).getChildAt(0);
                Glide.with(this)
                        .load(b)
                        .signature(new StringSignature(UUID.randomUUID().toString()))
                        .error(R.mipmap.ic_launcher)
                        .into(header);

            default:

                break;
        }
    }

}
