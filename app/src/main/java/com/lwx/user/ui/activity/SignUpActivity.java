package com.lwx.user.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private void init(){

        initToolbar();
        initEditTexts();


    }

    private void initToolbar(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v->finish());


    }

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

            }
        });
    }

    @Override
    public void onSignUpSucceed(String user) {

    }
}
