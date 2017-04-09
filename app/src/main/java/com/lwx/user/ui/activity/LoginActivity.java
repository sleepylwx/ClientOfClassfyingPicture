package com.lwx.user.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.j256.ormlite.stmt.query.In;
import com.lwx.user.R;
import com.lwx.user.adapter.ListPopWindowAdapter;
import com.lwx.user.contracts.LoginContract;
import com.lwx.user.db.model.User;
import com.lwx.user.presenter.LoginPresenter;
import com.lwx.user.ui.widget.StrengthenEditText;
import com.lwx.user.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{


    @BindView(R.id.edittext_user)StrengthenEditText userEdittext;
    @BindView(R.id.edittext_passwd)StrengthenEditText passwdEdittext;
    @BindView(R.id.button_login)Button loginButton;
    @BindView(R.id.button_findpasswd)Button findPasswdButton;
    @BindView(R.id.button_register) Button registerButton;
    @BindView(R.id.circleImageView)CircleImageView circleImageView;
    @BindView(R.id.divider)View divider;


    private PopupWindow popupWindow;
    private ListView listView;
    private int initialState = -1;
    private List<User> users;
    private String curToken;
    private boolean editState;

    private boolean isClicked;
    private ImageLoader imageLoader;
    public static final String MATCH_NUM = "1";
    private static final String MASKED_PASSWD = "111111111111";
    @OnClick(R.id.button_login)
    public void onClick(){

        String user = userEdittext.getText();
        String passwd = passwdEdittext.getText();
        if(curToken == null){

            presenter.login(user,passwd);
        }
        else{

            presenter.login(curToken);
        }

    }

    @OnClick(R.id.button_register)
    public void onClick1(){

        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_findpasswd)
    public void onClick2(){


        Intent intent = new Intent(this,FindPasswdActivity.class);
        startActivity(intent);

    }

    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);
        imageLoader = new ImageLoader();
        init();

    }

    @Override
    protected void onDestroy(){

        presenter = null;
        super.onDestroy();
    }

    private void init(){


        configurePopWindow();
        configureStrenghtenEditText();
        initState();
        presenter.loadAllUsers();

    }

    private void initState(){

        Intent intent = getIntent();
        initialState = intent.getIntExtra(MATCH_NUM,-1);

    }
    private void configurePopWindow(){


        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_list,null);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        listView = (ListView)view.findViewById(R.id.listview_popwindow);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User cur = (User)listView.getAdapter().getItem(position);
                userEdittext.setText(cur.user);


                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                ImageView imageView = userEdittext.getComponentItem(1);
                imageLoader.loadImage(LoginActivity.this,R.drawable.arrowdown,imageView);
                isClicked = false;
            }
        });
    }

    private void configureStrenghtenEditText(){

        List<ImageView> list = new ArrayList<>();
        list.add(new ImageView(this));
        list.add(new ImageView(this));
        imageLoader.loadImage(this,R.drawable.clear,list.get(0));
        imageLoader.loadImage(this,R.drawable.arrowdown,list.get(1));


        userEdittext.setComponent(list);
        userEdittext.setComponentPadding(10);
        userEdittext.setEditTextPaddingLeft(10);
        userEdittext.setRightComponentRightPadding(20);
        userEdittext.setDefaultTheme();
        userEdittext.setHint("用户名");
        userEdittext.setInputLength(18);

        userEdittext.setComponentItemVisibility(0,View.GONE);
        userEdittext.setComponentItemOnClickListener(1, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!isClicked){

                    imageLoader.loadImage(LoginActivity.this,R.drawable.arrowup,userEdittext.getComponentItem(1));

                    isClicked = true;

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(userEdittext.getWindowToken(),0);

                    //SystemClock.sleep(100);

                    popupWindow.showAsDropDown(divider);


                }
                else{

                    imageLoader.loadImage(LoginActivity.this,R.drawable.arrowdown,userEdittext.getComponentItem(1));

                    isClicked = false;

                    popupWindow.dismiss();
                }

            }
        });

        userEdittext.setComponentItemOnClickListener(0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userEdittext.setText("");
            }
        });

        userEdittext.setEditTextOnFocusedChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){

                    userEdittext.setComponentItemVisibility(0,View.GONE);

                }
                else{

                    if(!userEdittext.getText().equals("")){

                        userEdittext.setComponentItemVisibility(0,View.VISIBLE);

                    }
                }

            }
        });

        userEdittext.setEditTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(popupWindow != null && popupWindow.isShowing()){

                    popupWindow.dismiss();


                }
            }
        });

        userEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {




                String str = s.toString();

                if(str.equals("")){

                    userEdittext.setComponentItemVisibility(0,View.GONE);
                }
                else{

                    userEdittext.setComponentItemVisibility(0,View.VISIBLE);
                }

                if(users == null){

                    return;
                }


                Observable.create(new ObservableOnSubscribe<User>() {


                    @Override
                    public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {

                        int flag = -1;
                        for(int i = 0; i < users.size(); ++i){

                            if(str.equals(users.get(i).user)){

                                flag = i;
                                break;
                            }
                        }
                        if(flag == -1){


                            e.onNext(null);
                        }
                        else{

                            e.onNext(users.get(flag));
                        }
                        e.onComplete();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {

                                if(user == null){

                                    curToken = null;
                                    editState = false;
                                    imageLoader.loadImage(LoginActivity.this,R.mipmap.ic_launcher,circleImageView);
                                }
                                else{

                                    editState = true;
                                    curToken = user.token;

                                    imageLoader.loadImage(LoginActivity.this,user.headPath,circleImageView);

                                    passwdEdittext.setText(MASKED_PASSWD);




                                }

                            }
                        });
            }

        });

        List<ImageView> list1 = new ArrayList<>();
        list1.add(new ImageView(this));
        imageLoader.loadImage(this,R.drawable.clear,list1.get(0));
        passwdEdittext.setComponent(list1);
        passwdEdittext.setComponentPadding(10);
        passwdEdittext.setEditTextPaddingLeft(10);
        passwdEdittext.setRightComponentRightPadding(20);
        passwdEdittext.setDefaultTheme();
        passwdEdittext.setHint("密码");
        passwdEdittext.setEditTextInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwdEdittext.setInputLength(18);
        passwdEdittext.setComponentItemVisibility(0,View.GONE);

        passwdEdittext.setComponentItemOnClickListener(0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwdEdittext.setText("");
            }
        });

        passwdEdittext.setEditTextOnFocusedChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){

                    passwdEdittext.setComponentItemVisibility(0,View.GONE);

                }
                else{

                    if(!passwdEdittext.getText().equals("")){

                        passwdEdittext.setComponentItemVisibility(0,View.VISIBLE);

                    }
                }
            }
        });

        passwdEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {




                String str = s.toString();

                if(str.equals("")){

                    passwdEdittext.setComponentItemVisibility(0,View.GONE);
                }
                else{

                    passwdEdittext.setComponentItemVisibility(0,View.VISIBLE);
                }


                if(editState){

                    editState = false;
                    return;
                }
                if(curToken != null){


                    curToken = null;
                    passwdEdittext.setText("");

                }


            }
        });
    }



    @Override
    public void showNetWorkError() {

        Toast.makeText(this,"网络出现问题了噢...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginSuccess() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAllUsersLoaded(List<User> list) {


        listView.setAdapter(new ListPopWindowAdapter(this,R.layout.item_popwindow,list));
        users = list;

        if(initialState > 0){

            for(int i = 0 ; i < users.size(); ++i){

                if(users.get(i).uid == initialState){

                    userEdittext.setText(users.get(i).user);
                }
            }
        }
    }

    @Override
    public void onUsersEmpty() {

        userEdittext.setComponentItemVisibility(1,View.GONE);
        popupWindow.dismiss();
    }

    @Override
    public LoginContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onLoginSucceed() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
