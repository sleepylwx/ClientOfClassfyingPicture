package com.lwx.user.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lwx.user.R;
import com.lwx.user.contracts.SignUpContract;
import com.lwx.user.presenter.SignUpPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View{


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.user) EditText userEdit;
    @BindView(R.id.user_check) TextView userCheck;
    @BindView(R.id.passwd) EditText passwdEdit;
    @BindView(R.id.passwd_check) TextView passwdCheck;
    @BindView(R.id.passwd_two) EditText passwdTwoEdit;
    @BindView(R.id.passwd_two_check) TextView passwdTwoCheck;
    @BindView(R.id.submit) Button submit;

    @OnClick(R.id.submit)
    public void onClick(){

        String user = userEdit.getText().toString();
        String passwd = passwdEdit.getText().toString();
        String passwdTwo = passwdTwoEdit.getText().toString();

        if(user.isEmpty()){

            Toast.makeText(this,"用户名未输入",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwd.isEmpty()){

            Toast.makeText(this,"密码未输入",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwdTwo.isEmpty()){

            Toast.makeText(this,"重复密码未输入",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!userNameOK){

            Toast.makeText(this,"用户名格式有误",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!passwordOK){

            Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
        }


        if(passwd.equals(passwdTwo)){

            presenter.doSignUp(user,passwd);
        }
        else{

            Toast.makeText(this, R.string.passwd_repeat_error, Toast.LENGTH_SHORT).show();
        }

    }

    private SignUpContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);


        presenter = new SignUpPresenter(this);
        init();




    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
        presenter = null;
    }

    private void init(){

        initToolbar();
        initEditTexts();


    }

    private void initToolbar(){

        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v->finish());
        //toolbar.setTitle(R.string.sign_up);

    }

    private boolean userNameOK;
    private boolean passwordOK;
    private void initEditTexts(){


        userEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = s.toString();
                if(!str.contains("@") || str.startsWith("@")){

                    userCheck.setText(R.string.user_format_error);
                    userNameOK = false;
                }
                else{
                    userNameOK = true;
                    userCheck.setText(R.string.user_format_right);
                }

            }
        });

        passwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        passwdTwoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String passwd = passwdEdit.getText().toString();
                String passwdRepeat = s.toString();

                if(passwd.equals(passwdRepeat)){

                    passwdTwoCheck.setText(R.string.passwd_repeat_right);
                    passwordOK = true;
                }
                else{



                    passwordOK = false;
                    passwdTwoCheck.setText(R.string.passwd_repeat_error);
                }
            }
        });
    }


    @Override
    public void onSignUpSucceed(String user) {

        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra(LoginActivity.SIGN_UP_STATE,user);
        startActivity(intent);

    }

    @Override
    public SignUpContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUserHasExist() {

        Toast.makeText(this,"用户名已存在...",Toast.LENGTH_SHORT).show();
    }
}
