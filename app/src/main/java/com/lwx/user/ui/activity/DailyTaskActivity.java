package com.lwx.user.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.PreferenceHelper;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyTaskActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.notask)TextView noTask;
    @BindView(R.id.task)TextView task;
    @BindView(R.id.dotask)Button doTask;


    @OnClick(R.id.dotask)
    public void onClick(){

        boolean state = App.getInstance().isInTask();
        if(state){

            App.getInstance().setInTask(false);
            Toast.makeText(this,"暂停成功",Toast.LENGTH_SHORT).show();
        }
        else{

            App.getInstance().setInTask(true);
            App.getInstance().setTaskNum(num);
            App.getInstance().setFinishNum(preferenceHelper.getInt(FINISH+App.getInstance().getUid(),0));
            Toast.makeText(this,"开始打标签吧,当前进度(" + App.getInstance().finishNum + "/"
                    + App.getInstance().taskNum + ")",Toast.LENGTH_SHORT).show();
        }

        finish();
    }
    public static final String TASK = "TASK";
    public static final String TIME = "TIME";
    public static final String FINISH = "FINISH";

    private UserAgent userAgent;
    private PreferenceHelper preferenceHelper;
    private int num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);

        ButterKnife.bind(this);
        userAgent = UserAgentImpl.getInstance();
        preferenceHelper = new PreferenceHelper();
        init();


    }


    private void init(){

        initToolbar();
        initTask();
        initState();
    }

    private void initState(){

        int finish = preferenceHelper.getInt(FINISH + App.getInstance().getUid(),0);
        boolean state = App.getInstance().isInTask();
        if(state){

            doTask.setText("暂停任务");
            task.setText("今日任务：标记" + num + "张图片(双倍积分)"
                    + "\n                已完成" + finish + "张");

        }
        else{

            doTask.setText("开始任务");
        }
    }
    private void initToolbar(){

        toolbar.setTitle("日常任务");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void initTask(){

        int num;
        if(App.getInstance().getHaveTask()){

            Random random = new Random();
            num = 10 + random.nextInt(22);
            preferenceHelper.setInt(TASK+App.getInstance().getUid(),num);
        }
        else{

            num = preferenceHelper.getInt(TASK+App.getInstance().getUid(),-1);
        }
        if(num == -1){

            showNoTask();
            return;
        }


        showTask(num);
    }

    private void showNoTask(){

        App.getInstance().setHaveTask(false);
        noTask.setVisibility(View.VISIBLE);
        task.setVisibility(View.GONE);
        doTask.setVisibility(View.GONE);
    }
    private void showTask(int num){

        this.num = num;
        App.getInstance().setHaveTask(false);

        task.setText("今日任务：标记" + num + "张图片(双倍积分)"
         );
    }



}
