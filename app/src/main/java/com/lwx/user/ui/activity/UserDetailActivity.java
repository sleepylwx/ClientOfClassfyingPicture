package com.lwx.user.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.j256.ormlite.stmt.query.In;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.UserDetailContract;
import com.lwx.user.model.model.User;
import com.lwx.user.presenter.UserDetailPresenter;
import com.lwx.user.utils.ImageLoader;
import com.lwx.user.utils.PreferenceHelper;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UserDetailActivity extends AppCompatActivity implements UserDetailContract.View{


    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.header)CircleImageView header;
    @BindView(R.id.user1) TextView user;
    @BindView(R.id.nickname1)EditText nickName;
    @BindView(R.id.num1)TextView num;


    @BindView(R.id.plus_button)
    Button plusButton;
    @BindView(R.id.subtract_button)
    Button subtractButton;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowLayout;

    @OnClick(R.id.plus_button)
    public void onClick(){

        Log.d(TAG,"second_value11=" + value);
        OptionsPickerView options = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {



                String temp = secondMenu.get(options1).get(options2);

                for(int i = 0 ; i < favs.size(); ++i){

                    if(temp.equals(favs.get(i))){

                        return;
                    }
                }

                favs.add(temp);
                initFlow();

                StringBuffer sb = new StringBuffer(value);

                int res = 0;
                for(int i = 0; i < options1 ;++i){


                    res += numCounter.get(i);
                }

                res += options2;

                sb.setCharAt(res,'1');

                arr.add(res);
                value = sb.toString();
                Log.d(TAG,"second_value" + value);

            }
        }).build();

        options.setPicker(firstMenu,secondMenu);
        options.show();
    }

    @OnClick(R.id.subtract_button)
    public void onClic1k(){


        Set<Integer> set = flowLayout.getSelectedList();

        if(set.size() == 0){

            return;
        }
        StringBuffer sb = new StringBuffer(value);
        int count = 0;
        Set<Integer> set1 = new TreeSet<>(set);
        for(int i : set1){

            sb.setCharAt(arr.get(i-count),'0');
            favs.remove(i-count);
            arr.remove(i-count);
            ++count;
        }




        value = sb.toString();
        initFlow();
    }

    private UserDetailContract.Presenter presenter;

    private ImageLoader imageLoader;

    private User curUser;

    private boolean isCroped;

    private boolean headerPost;
    private boolean messagePost;
    private boolean headerNeedPost;
    private boolean messageNeedPost;

    private boolean canSaved;

    private List<String> firstMenu;
    private List<List<String>> secondMenu;

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

    public static final String TAG = "UserDetailActivity";
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(!canSaved){

            Toast.makeText(this,"网络错误，请回到主界面后再试",Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }

        curUser.nickName = nickName.getText().toString();
        curUser.favorite = value;

        Log.d(TAG,"value + " + value);
        if(!isCroped && (curUser.nickName.equals(temp.nickName)
                && curUser.favorite.equals(temp.favorite))){

            success();
            return super.onOptionsItemSelected(item);

        }
        if(isCroped){

            //curUser.headPath = path;
            headerNeedPost = true;

            try{

                File file = new Compressor(this)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(new File(path));
                path = file.getPath();
            }
            catch (Exception e){


            }
            finally {

                presenter.saveUserHeader(curUser.token,path);
            }



        }
        else{

            headerNeedPost = false;

        }

        if(!(curUser.nickName.equals(temp.nickName)
                && curUser.favorite.equals(temp.favorite))){

            messageNeedPost  = true;
            presenter.saveUserMessage(curUser);

        }
        else{

            messageNeedPost = false;
        }

        return super.onOptionsItemSelected(item);
    }
    private void init(){

        initMenuData();
        initToolbar();
        initUser();
        initHeader();

        //initFavorite();
    }

    private List<Integer> numCounter;

    private void initMenuData(){

        firstMenu = new ArrayList<>();
        secondMenu = new ArrayList<>();
        numCounter = new ArrayList<>();

        firstMenu.add("自然与生物");
        firstMenu.add("人文与历史");
        firstMenu.add("社会与生活");

        List<String> one = new ArrayList<>();
        one.add("风景");
        one.add("猫");
        one.add("狗");
        one.add("花");
        secondMenu.add(one);
        List<String> two = new ArrayList<>();
        two.add("历史文化");
        two.add("世界建筑");
        two.add("绘画工艺");
        two.add("文学戏剧");
        secondMenu.add(two);
        List<String> three = new ArrayList<>();
        three.add("交通运输");
        three.add("运动健康");
        three.add("职业教育");
        three.add("饮食活动");
        secondMenu.add(three);

        numCounter.add(4);
        numCounter.add(4);
        numCounter.add(4);
    }

    private List<Integer> arr;

    private String value;

    private List<String> favs;

    private void initFavorite(){


        //PreferenceHelper preferenceHelper = new PreferenceHelper();

        //value = preferenceHelper.getFavorite(App.getInstance().getUid()+"");

        arr = new ArrayList<>();
        favs = new ArrayList<>();
        Log.d(TAG,"value:=" + value);
        if(value == null || value.equals("")){

            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < numCounter.size(); ++i){

                for(int j = 0; j < numCounter.get(i); ++j){

                    sb.append('0');
                }
            }

            value = sb.toString();
        }

        for(int i = 0 ; i < value.length() ;++i){

            if(value.charAt(i) == '1'){

                arr.add(i);
                Log.d(TAG,"arr " + i);
            }
        }


        for(int i = 0 ; i < arr.size(); ++i){

            int f = getFirst(arr.get(i));
            int s = getSecond(arr.get(i));
            favs.add(secondMenu.get(f).get(s));
            Log.d(TAG,"fs " + f + " " + s);
        }

        Log.d(TAG,"value:ss="+value);
        initFlow();


    }


    private void initFlow(){


        flowLayout.setAdapter(new TagAdapter<String>(favs) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {


                TextView textView = (TextView) LayoutInflater.from(UserDetailActivity.this)
                        .inflate(R.layout.tv,flowLayout,false);
                textView.setText(o);
                return textView;
            }
        });

    }

    private int getFirst(int num){


        int res = 0;
        for(int i = 0 ; i < numCounter.size() ;++i){

            if(num < numCounter.get(i)){

                return res;
            }
            ++res;
            num -= numCounter.get(i);
        }

        return res;
    }

    private int getSecond(int num){

        for(int i = 0 ; i < numCounter.size(); ++i){


            if(num < numCounter.get(i)){

                return num;
            }

            num -= numCounter.get(i);

        }

        return num;
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
        temp.favorite = curUser.favorite;
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

        Log.d(TAG,"first_value"+ user.favorite);
        value = user.favorite;
        initFavorite();

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
