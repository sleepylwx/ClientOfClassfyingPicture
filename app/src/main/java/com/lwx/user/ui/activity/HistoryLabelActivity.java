package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.adapter.TagCloudAdapter;
import com.lwx.user.contracts.HistoryLabelContract;
import com.lwx.user.presenter.HistoryLabelPresenter;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.moxun.tagcloudlib.view.TagsAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryLabelActivity extends AppCompatActivity implements HistoryLabelContract.View{

    public static final String USERID = "1";
    public static final String TAG = "HistoryLabelActivity";

    private HistoryLabelContract.Presenter presenter;
    //@BindView(R.id.flowlayout)TagFlowLayout flowLayout;
    @BindView(R.id.textview_historylabel)TextView textView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tagcloud) TagCloudView tagCloudView;

    private TagsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_label);
        ButterKnife.bind(this);
        presenter = new HistoryLabelPresenter(this);

        init();
    }

    @Override
    protected void onStart() {

        super.onStart();
        initLabel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;

    }

    private void init(){

        initToolbar();
        initLabel();

    }

    private void initToolbar(){

        toolbar.setTitle("标记过的标签");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }
    private void initLabel(){


        presenter.getSignedLabels(App.getInstance().getUid());
    }

    @Override
    public void onSignedLabelsLoadedSuccess(List<String> labels) {

        Log.d(TAG,"onSignedLabelsLoadedSuccess");
        tagCloudView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);

        adapter = new TagCloudAdapter(labels);
        tagCloudView.setAdapter(adapter);

    }

    @Override
    public void onSignedLabelsLoadedFailed() {

        Log.d(TAG,"onSignedLabelsLoadedFailed");
        tagCloudView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public HistoryLabelContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();

    }
}
