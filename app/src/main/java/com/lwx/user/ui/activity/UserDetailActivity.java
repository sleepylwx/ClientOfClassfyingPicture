package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.UserDetailContract;
import com.lwx.user.db.model.User;
import com.lwx.user.presenter.UserDetailPresenter;
import com.lwx.user.utils.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class UserDetailActivity extends AppCompatActivity implements UserDetailContract.View{


    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.header)CircleImageView header;
    @BindView(R.id.user1) TextView user;
    @BindView(R.id.nickname1)EditText nickName;
    @BindView(R.id.num1)TextView num;
    @BindView(R.id.favorite1)EditText favorite1;
    @BindView(R.id.favorite2)EditText favorite2;
    @BindView(R.id.favorite3)EditText favorite3;

    private UserDetailContract.Presenter presenter;

    private ImageLoader imageLoader;

    private User curUser;

    private boolean isCroped;

    private boolean headerPost;
    private boolean messagePost;
    private boolean headerNeedPost;
    private boolean messageNeedPost;

    private boolean canSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);

        presenter = new UserDetailPresenter(this);
        imageLoader = new ImageLoader();
        isCroped = false;

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        menu.getItem(0).setTitle("保存");
        return true;
    }

    private User temp;
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(!canSaved){

            Toast.makeText(this,"网络错误，请回到主界面后再试",Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }

        curUser.nickName = nickName.getText().toString();
        curUser.favorite1 = favorite1.getText().toString();
        curUser.favorite2 = favorite2.getText().toString();
        curUser.favorite3 = favorite3.getText().toString();

        if(!isCroped && (curUser.nickName.equals(temp.nickName)
                && curUser.favorite1.equals(temp.favorite1)
                && curUser.favorite2.equals(temp.favorite2)
                && curUser.favorite3.equals(temp.favorite3))){

            success();
            return super.onOptionsItemSelected(item);

        }
        if(isCroped){

            //curUser.headPath = path;
            headerNeedPost = true;
            presenter.saveUserHeader(curUser.token,path);
        }
        else{

            headerNeedPost = false;

        }

        if(!(curUser.nickName.equals(temp.nickName)
                && curUser.favorite1.equals(temp.favorite1)
                && curUser.favorite2.equals(temp.favorite2)
                && curUser.favorite3.equals(temp.favorite3))){

            messageNeedPost  = true;
            presenter.saveUserMessage(curUser);

        }
        else{

            messageNeedPost = false;
        }

        return super.onOptionsItemSelected(item);
    }
    private void init(){

        initToolbar();
        initUser();
        initHeader();

    }

    private void initUser(){

        long uid = App.getInstance().getUid();
        String token = App.getInstance().getToken();
        presenter.getUser(uid,token);


    }
    private void initHeader(){

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startUploadActivity();
            }
        });
    }

    String path;
    private void startUploadActivity(){

        RxGalleryFinalApi.getInstance(this)
                .onCrop(true)//是否裁剪
                .openGalleryRadioImgDefault(new RxBusResultSubscriber() {
                    @Override
                    protected void onEvent(Object o) throws Exception {

                    }
                })
                .onCropImageResult(new IRadioImageCheckedListener() {
                    @Override
                    public void cropAfter(Object t) {

                        isCroped = true;
                        path = ((File)t).getAbsolutePath();
                        imageLoader.loadImage(UserDetailActivity.this,path,header);

                    }

                    @Override
                    public boolean isActivityFinish() {
                        return true;
                    }
                });

    }
    private void initToolbar(){

        toolbar.setTitle("用户资料");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    public void onUserGetSuccess(User user) {

        if(user.extra == null || ((String)user.extra).isEmpty()){

            canSaved = false;
        }
        else{
            canSaved = true;
        }
        curUser = user;
        temp = new User();
        temp.nickName = curUser.nickName;
        temp.favorite1 = curUser.favorite1;
        temp.favorite2 = curUser.favorite2;
        temp.favorite3 = curUser.favorite3;
        temp.headPath = curUser.headPath;

        if(user.headPath == null || user.headPath.equals("")){

            imageLoader.loadImage(this,R.mipmap.ic_launcher,header);
        }
        else{

            //imageLoader.loadImage(this,user.headPath,header);
            Glide.with(this)
                    .load(user.headPath)
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .error(R.mipmap.ic_launcher)
                    .into(header);
        }
        this.user.setText(user.user);
        this.nickName.setText(user.nickName);
        this.num.setText("" + user.num);
        this.favorite1.setText(user.favorite1);
        this.favorite2.setText(user.favorite2);
        this.favorite3.setText(user.favorite3);


    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public UserDetailContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onUserGetNetWorkError() {

        onNetWorkError();
    }

    public static final String NICKNAME = "NICKNAME";
    @Override
    public void onUserSaveMessageSuccess() {

        messagePost = true;

        if(messagePost && !headerNeedPost){

            success();
            presenter.saveUserInDb(curUser);
        }

        if(messagePost && headerPost){

            success();
            presenter.saveUserInDb(curUser);
        }

    }

    public static final String HEADERPATH = "HEADERPATH";
    private void success(){

        Toast.makeText(this,"用户资料保存成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        if(messagePost && !headerNeedPost){

            intent.putExtra(NICKNAME,curUser.nickName);
            setResult(MainActivity.RESULTCODE1,intent);
        }
        else if(headerPost && !messageNeedPost){

            intent.putExtra(HEADERPATH,curUser.headPath);
            setResult(MainActivity.RESULTCODE2,intent);
        }
        else{

            intent.putExtra(HEADERPATH,curUser.headPath);
            intent.putExtra(NICKNAME,curUser.nickName);
            setResult(MainActivity.RESULTCODE3,intent);
        }
        finish();
    }

    @Override
    public void onUserSaveHeaderSuccess() {


        headerPost = true;

        if(headerPost && !messageNeedPost){

            success();
            presenter.saveUserInDb(curUser);
        }
        if(messagePost && headerPost){

            success();
            presenter.saveUserInDb(curUser);
        }
    }

    @Override
    public void onUserSaveMessageError(){

        messagePost = false;
        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUserSaveHeaderError() {

        headerPost = false;
        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();
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
}
