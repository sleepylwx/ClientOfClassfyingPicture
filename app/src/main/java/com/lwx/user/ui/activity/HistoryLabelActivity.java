package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import butterknife.OnClick;

public class HistoryLabelActivity extends AppCompatActivity implements HistoryLabelContract.View{

    public static final String USERID = "1";
    public static final String TAG = "HistoryLabelActivity";

    private HistoryLabelContract.Presenter presenter;
    @BindView(R.id.flowlayout)TagFlowLayout flowLayout;
    @BindView(R.id.textview_historylabel)TextView textView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tagcloud) TagCloudView tagCloudView;

    private TagsAdapter adapter;

    private List<String> labels;
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

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(0).setTitle("平面图");
        this.menu = menu;
        return true;
    }

    private int mode;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mode == 0){

            mode = 1;
            menu.getItem(0).setTitle("球形图");
            tagCloudView.setVisibility(View.GONE);
            flowLayout.setVisibility(View.VISIBLE);
        }
        else{

            mode = 0;
            menu.getItem(0).setTitle("平面图");
            flowLayout.setVisibility(View.GONE);
            tagCloudView.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);

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

        this.labels = labels;
        Log.d(TAG,"onSignedLabelsLoadedSuccess");

        if(mode == 0){

            tagCloudView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
        else{

            flowLayout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }


        adapter = new TagCloudAdapter(this,this.labels);
        tagCloudView.setAdapter(adapter);


        flowLayout.setAdapter(new TagAdapter<String >(this.labels) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView textView = (TextView) LayoutInflater.from(HistoryLabelActivity.this)
                        .inflate(R.layout.tv,flowLayout,false);
                textView.setText(o);
                return textView;
            }
        });

        flowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if(selectPosSet == null){
                    return;
                }
                if(selectPosSet.size() != 1){
                    return;
                }
                String ss = null;
                for(int i : selectPosSet){
                    ss = labels.get(i);
                }
                Intent intent = new Intent(HistoryLabelActivity.this,HistoryImageActivity.class);
                intent.putExtra(HistoryImageActivity.TITLE,ss);
                startActivity(intent);
                flowLayout.getAdapter().setSelectedList();
            }
        });

    }

    @Override
    public void jumpToHistoryImageActivity(String ss) {

        Intent intent = new Intent(HistoryLabelActivity.this,HistoryImageActivity.class);
        intent.putExtra(HistoryImageActivity.TITLE,ss);
        startActivity(intent);

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
