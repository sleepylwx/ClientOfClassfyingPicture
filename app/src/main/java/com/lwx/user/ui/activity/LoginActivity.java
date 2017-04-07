package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lwx.user.R;
import com.lwx.user.contracts.LoginContract;
import com.lwx.user.db.model.UserDetail;
import com.lwx.user.presenter.LoginPresenter;
import com.lwx.user.ui.widget.StrengthenEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{


    @BindView(R.id.edittext_user)StrengthenEditText userEdittext;
    @BindView(R.id.edittext_passwd)StrengthenEditText passwdEdittext;
    @BindView(R.id.button_login)Button loginButton;
    @BindView(R.id.button_findpasswd)Button findPasswdButton;
    @BindView(R.id.button_register) Button registerButton;
    @BindView(R.id.circleImageView)CircleImageView circleImageView;
    @BindView(R.id.divider)View divider;


    private PopupWindow popupWindow;

    @OnClick(R.id.button_login)
    public void onClick(){

        String user = userEdittext.getText();
        String passwd = passwdEdittext.getText();
        presenter.login(user,passwd);

    }

    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);

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
    public void onAllUserDetailLoaded(List<UserDetail> list) {



    }
}
