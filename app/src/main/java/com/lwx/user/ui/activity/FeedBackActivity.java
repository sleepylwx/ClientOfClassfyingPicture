package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.ConstStringMessages;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedBackActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.feedbackedit)EditText editText;
    @BindView(R.id.feedbackbutton)Button button;

    @OnClick(R.id.feedbackbutton)
    public void onClick(){

        String text = editText.getText().toString();
        UserAgent userAgent = UserAgentImpl.getInstance();

        userAgent.postFeedBack(App.getInstance().getToken(),text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        onFeedBackPostSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            Toast.makeText(FeedBackActivity.this, R.string.token_auth_failed, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(FeedBackActivity.this, LoginActivity.class);
                            intent.putExtra(LoginActivity.MATCH_NUM, App.getInstance().getUid());
                            intent.putExtra(LoginActivity.ISAUTHFAILED, true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                            return;
                        }
                        onFeedBackPostFailed();
                    }
                });
    }

    private void onFeedBackPostSuccess(){

        finish();
    }

    private void onFeedBackPostFailed(){

        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        init();
    }


    private void init(){

        initToolbar();

    }

    private void initToolbar(){


        toolbar.setTitle("问题反馈");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}

