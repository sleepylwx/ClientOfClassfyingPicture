package com.lwx.user.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.adapter.RecyclerViewAdapter;
import com.lwx.user.contracts.MainContract;
import com.lwx.user.model.model.Image;
import com.lwx.user.model.model.ImageSearch;
import com.lwx.user.model.model.User;
import com.lwx.user.presenter.MainPresenter;
import com.lwx.user.utils.ImageLoader;
import com.lwx.user.utils.PreferenceHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.bottomview)
    BottomNavigationBar bottomNavigationView;
    @BindView(R.id.header_edit)
    EditText editText;
    @BindView(R.id.searchbutton)
    Button searchButton;

    @OnClick(R.id.searchbutton)
    public void onClick(){

        String str = editText.getText().toString();
        if(str == null || str.isEmpty()){

            Toast.makeText(this,"请输入喜好标签",Toast.LENGTH_SHORT).show();
            return;
        }
        presenter.getImagesByLabel(str);

    }
    public static final String MATCH_NUM = "1";
    public static final int RESULTCODE = 2;
    private MainContract.Presenter presenter;
    private ImageLoader imageLoader;


    private RecyclerViewAdapter adapter;
    private PreferenceHelper preferenceHelper;
    private List<Image> list;
    private List<ImageSearch> imageSearchList;
    private Set<String> set;


    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);
        imageLoader = new ImageLoader();
        list = new ArrayList<>();
        imageSearchList = new ArrayList<>();
        set = new HashSet<>();
        preferenceHelper = new PreferenceHelper();
        //headerImageView =strenthenToolBar.getHeaderView();

        //


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

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{

            moveTaskToBack(false);
        }
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
                else if(id == R.id.nav_history){

                    Intent intent = new Intent(MainActivity.this,HistoryStatisticsActivity.class);
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
                }

                else if(id == R.id.nav_feedback){

                    Intent intent = new Intent(MainActivity.this,FeedBackActivity.class);
                    startActivity(intent);

                }
                else if (id == R.id.nav_exit) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(LoginActivity.MATCH_NUM, App.getInstance().getUid());

                    Log.d(TAG, "exit login" + " " + App.getInstance().getUid());

                    new PreferenceHelper().deleteLogInUID();
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


        detectNewTask();

        initToolBar();
        initNavigationView();
        initSwipeRefresh();
        initRecycleView();
        initBottomNavigationView();
        initUser();
        initSet();
        //initPicture();

    }

    private InputMethodManager manager;
    private TextWatcher textWatcher;
    private void initToolBar(){


        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                editText.findFocus();
                manager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = s.toString();
                if((s == null || s.length() == 0 )){

                    adapter.setData(MainActivity.this.list);
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setEnabled(true);
                    canScroll = true;
                    return;
                }

                swipeRefresh.setEnabled(false);
                presenter.searchImages(str,imageSearchList);
            }
        };

        editText.addTextChangedListener(textWatcher);


    }

    @Override
    public void onImageSearchSucceed(List<Image> images) {

        adapter.setData(images);
        adapter.notifyDataSetChanged();
    }

    private void initBottomNavigationView(){

        //bottomNavigationView.setAutoHideEnabled(false);
        bottomNavigationView.addItem(new BottomNavigationItem(R.drawable.first,"画像"))
                .addItem(new BottomNavigationItem(R.drawable.third,"关键字"))
                .addItem(new BottomNavigationItem(R.drawable.second,"随机"))
                .initialise();

        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {


                canScroll = true;
                editText.setFocusable(false);
               // InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(manager.isActive()){

                    manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
                }

                editText.removeTextChangedListener(textWatcher);
                editText.setText("");
                //canScroll = true;
                if(position == 0){

                    userMode = 0;
                    swipeRefresh.setEnabled(true);
                    searchButton.setVisibility(View.INVISIBLE);
                    presenter.clearAndGetPicByNetWork(App.getInstance().getUid(),
                                App.getInstance().getToken(), App.getInstance().getpullPicNum());

                    //editText.removeTextChangedListener(textWatcher);
                    editText.addTextChangedListener(textWatcher);
                }
                else if(position == 2){

                    userMode = 1;
                    swipeRefresh.setEnabled(true);
                    searchButton.setVisibility(View.INVISIBLE);
                   // editText.removeTextChangedListener(textWatcher);
                    editText.addTextChangedListener(textWatcher);

                    presenter.clearAndGetMoreRandomPicByNet(App.getInstance().getUid(), App.getInstance().getpullPicNum());
                }
                else{

                    swipeRefresh.setEnabled(false);
                    searchButton.setVisibility(View.VISIBLE);
                    //editText.removeTextChangedListener(textWatcher);

                    adapter.setData(new ArrayList<>());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });



    }
    private void initSet(){

        presenter.getAllMarkedPictures(App.getInstance().getUid());
    }

    @Override
    public void onAllMarkedPictureLoaded(List<Image> images) {

        for(int i = 0; i < images.size(); ++i){

            set.add(images.get(i).uuid);
        }

        Log.d(TAG,"setNum " + set.size());

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


    }

    @Override
    public void onImageLoadedSucceed(List<Image> imageList) {


        Log.d(TAG, "imageLoaded succeed!");
        List<Image> temp = new ArrayList<>();
        for (int i = 0; i < imageList.size(); ++i) {

            Log.d(TAG, "uuid:" + imageList.get(i).uuid + " path:" + imageList.get(i).imagePath);

            if(!set.contains(imageList.get(i).uuid)){


                temp.add(imageList.get(i));
            }

        }
        canScroll = true;
        Log.d(TAG,"tempSize " + temp.size() + " " + imageList.size() );
        addImages(temp);

    }



    @Override
    public void clearAndSaveList(List<Image> imageList) {

        if(imageList == null){

            return;
        }

        List<Image> images = new ArrayList<>();

        for(int i = 0; i < imageList.size() ; ++i){

            if(!set.contains(imageList.get(i).uuid)){

                images.add(imageList.get(i));
            }
        }

        this.list = images;
        Log.d(TAG, "clear and save list");
        adapter.setData(this.list);
        adapter.notifyDataSetChanged();

        canScroll = true;

        imageSearchList = new ArrayList<>();
        presenter.getImagesLabels(this.list,imageSearchList);

    }

    private void addImages(List<Image> images) {

        this.list.addAll(images);
        //adapter.addData(images);


        adapter.notifyDataSetChanged();

        presenter.getImagesLabels(images,imageSearchList);
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

                        if(adapter.getData() == list){

                            presenter.getMoreRandomPicturesByNetWork(App.getInstance().getUid(), App.getInstance().getpullPicNum());
                        }
                    }

                }
            }
        });

    }

    @Override
    public void startGetMorePicByNetWork() {

        //showWaitingNetWork();
        if(adapter.getData() != list){

            return;
        }
        presenter.getMoreRandomPicturesByNetWork(App.getInstance().getUid(),App.getInstance().getpullPicNum());

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
                set.add(list.get(position).uuid);
                Log.d(TAG,"setNum " + set.size());

//                Iterator<Image> it = list.iterator();
//                String str = list.get(position).uuid;
//                canScroll = false;
//                while(it.hasNext()){
//
//                    String temp = it.next().uuid;
//                    if(str.equals(temp)){
//
//                        it.remove();
//                    }
//                }
//                canScroll = true;
                int count = 0;
                String str = list.get(position).uuid;
                int num = list.size();
                for(int i = 0; i < num; ++i){


                    String temp = list.get(i-count).uuid;
                    if(str.equals(temp)){

                        list.remove(i-count);
                        imageSearchList.remove(i-count);
                        adapter.notifyItemRemoved(i-count);
                        adapter.notifyItemRangeChanged(i-count,list.size()-(i-count));
                        ++count;
                    }


                }
                //list.remove(position);
                //Log.d(TAG,"position size " + list.size());
                //imageSearchList.remove(position);
                //adapter.notifyDataSetChanged();
                //adapter.notifyItemRemoved(position);
                //adapter.notifyItemRangeChanged(position,list.size()-position);
                break;

            case RESULTCODE1:
                updateNickName(data);
                break;
            case RESULTCODE2:
                updateHeader(data);
                break;
            case RESULTCODE3:
                updateNickName(data);
                updateHeader(data);
                break;
            default:

                break;
        }
    }

    private void updateNickName(Intent data){


        String a = data.getStringExtra(UserDetailActivity.NICKNAME);
        TextView textView =  (TextView) ((ViewGroup) navigationView.getHeaderView(0)).getChildAt(1);
        Log.d(TAG,"a = " + a);
        textView.setText(a);

    }


    private void updateHeader(Intent data){

        String b = data.getStringExtra(UserDetailActivity.HEADERPATH);
        CircleImageView header = (CircleImageView) ((ViewGroup) navigationView.getHeaderView(0)).getChildAt(0);
        Glide.with(this)
                .load(b)
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .error(R.mipmap.ic_launcher)
                .into(header);
    }


    private void detectNewTask(){

        long curTime = System.currentTimeMillis();
        long curDay = curTime / (1000* 60*60*24);
        long lastDay = preferenceHelper.getLong(DailyTaskActivity.TIME+App.getInstance().getUid(),-1);
        Log.d("days",lastDay + " " + curDay + " " + curTime);

        if(lastDay == -1 || curDay - lastDay > 0){


            //preferenceHelper.setLong(DailyTaskActivity.TIME+App.getInstance().getUid(),curDay);
            App.getInstance().setHaveTask(true);

        }

        else{

            App.getInstance().setHaveTask(false);
        }
    }


    @Override
    public void onImageSearchFailed() {

        Toast.makeText(this,"没有找到标记该标签的图片",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetImagesByLabelSucceed(List<Image> images) {

        adapter.setData(images);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetImagesByLabelFailed() {

        onImageSearchFailed();
    }
}
