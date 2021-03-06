package com.lwx.user.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.ImageDetailContract;
import com.lwx.user.model.model.Image;
import com.lwx.user.presenter.ImageDetailPresenter;
import com.lwx.user.utils.ImageLoader;
import com.lwx.user.utils.PreferenceHelper;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageDetailActivity extends Activity implements ImageDetailContract.View{

    public static final String IMAGEUUID = "IMAGEUUID";
    public static final String ISLABELED = "ISLABELED";
    public static final String TITLE = "TITLE";

    public static final int REQUESTCODE = 1;
    @BindView(R.id.photoview)PhotoView photoView;
    @BindView(R.id.flowlayout)TagFlowLayout flowLayout;
    @BindView(R.id.add_label)Button addLabel;
    @BindView(R.id.post_label)Button postLabel;
    @BindView(R.id.textview_bottom)TextView textViewSecond;
    @BindView(R.id.textview_first)TextView textViewFirst;
    @BindView(R.id.flowlayoutfirst)TagFlowLayout flowLayoutFirst;

    @OnClick(R.id.add_label)
    public void onClick(){

        Intent intent = new Intent(this,AddLabelDialog.class);
        startActivityForResult(intent,REQUESTCODE);

    }

    Set<Integer> set;
    Set<Integer> setFirst;

    @OnClick(R.id.post_label)
    public void onClick1(){


        set = flowLayout.getSelectedList();
        setFirst = flowLayoutFirst.getSelectedList();

        if(setFirst.size() + set.size() < 1){

            Toast.makeText(this,R.string.selected_non_label,Toast.LENGTH_SHORT).show();
            return;
        }

        if(isLabeled){

            removePicTags();
        }
        else{

            post();
        }



    }
    private String uuid;
    private ImageDetailContract.Presenter presenter;
    private ImageLoader imageLoader;
    private List<String> curLables;
    private Image curImage;
    private String title;

    private PreferenceHelper preferenceHelper;

    private boolean isLabeled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        uuid = intent.getStringExtra(IMAGEUUID);
        isLabeled = intent.getBooleanExtra(ISLABELED,false);
        title = intent.getStringExtra(TITLE);

        preferenceHelper = new PreferenceHelper();
        presenter = new ImageDetailPresenter(this,isLabeled);
        imageLoader = new ImageLoader();
        curLables = new ArrayList<>();
        init();

    }

    @Override
    protected void onDestroy() {

        Log.d(TAG,"onDestroy");
        super.onDestroy();
        presenter = null;
    }

    private void removePicTags(){


        for(int i = 0; i < curLables.size(); ++i){

            presenter.removePicTag(App.getInstance().getToken(),uuid,curLables.get(i));
        }

    }

    private void post(){


        List<String> selectedLabels = new ArrayList<>();
        for(Integer i : set){

            selectedLabels.add(curLables.get(i));
        }

        for(Integer i : setFirst){

            selectedLabels.add(firstLabels.get(i));
        }
        presenter.postSelectedLabels(App.getInstance().getToken(),uuid,App.getInstance().getUid(),selectedLabels);

    }

    private void init(){




        initTextView();
        initPhotoView(uuid);
        initLabels(uuid);


        initFirstLabel(uuid);
    }

    private void initFirstLabel(String uuid){


        presenter.getFirstLabel(uuid);
    }

    private void initTextView(){

        if(isLabeled){

            textViewSecond.setText("我的标记：");
        }
        else{

            textViewSecond.setText("标签：");
        }
    }

    private void initPhotoView(String uuid){

        presenter.getImage(App.getInstance().getUid(),uuid);
    }

    private void initLabels(String uuid){

        presenter.getLabels(App.getInstance().getUid(),uuid,isLabeled);
    }



    public static final String TAG = "ImageDetailActivity";
    @Override
    public void onLabelsLoadSucceed(List<String> labels) {

        for(int i = 0; i < labels.size() ;++i){

            Log.d(TAG,labels.get(i));
        }
        curLables = labels;


        flowLayout.setAdapter(new TagAdapter<String >(labels) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {

                TextView textView = (TextView) LayoutInflater.from(ImageDetailActivity.this)
                        .inflate(R.layout.tv,flowLayout,false);
                textView.setText(o);
                return textView;
            }
        });



        flowLayout.setVisibility(View.VISIBLE);
        textViewSecond.setVisibility(View.VISIBLE);

    }

    @Override
    public ImageDetailContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        Toast.makeText(this,R.string.network_error,Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onImageLoadSucceed(Image image) {

        String path = image.imagePath;
        imageLoader.loadImageWithListener(this, path, photoView, new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                //photoLoadSucceed = false;
                addLabel.setEnabled(false);
                postLabel.setEnabled(false);
                Toast.makeText(ImageDetailActivity.this,"图片加载失败",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                //photoLoadSucceed = true;

                addLabel.setEnabled(true);
                postLabel.setEnabled(true);
                return false;
            }
        });

        curImage = image;
    }

    private List<String> postedLabels;
    private List<String> tempLabels;
    @Override
    public void onLabelsPostSucceed(List<String> labels) {

        StringBuffer stringBuffer = new StringBuffer("提交标签成功");


        if(App.getInstance().isInTask()){

            ++App.getInstance().finishNum;
            stringBuffer.append(",任务进度(");
            stringBuffer.append(App.getInstance().finishNum);
            stringBuffer.append("/");
            stringBuffer.append(App.getInstance().taskNum);
            stringBuffer.append(")");

        }
        if(App.getInstance().isInTask() && App.getInstance().finishNum >=  App.getInstance().taskNum){

            presenter.finishTask(App.getInstance().getToken(),App.getInstance().finishNum);
            App.getInstance().finishNum = 0;
            App.getInstance().taskNum = -1;
            App.getInstance().setInTask(false);
            preferenceHelper.setInt(DailyTaskActivity.FINISH+App.getInstance().getUid(),0);
            preferenceHelper.setInt(DailyTaskActivity.TASK + App.getInstance().getUid(),-1);
            stringBuffer = new StringBuffer();
            stringBuffer.append("提交标签成功,恭喜您，日常任务完成!");

        }
        else{

            preferenceHelper.setInt(DailyTaskActivity.FINISH+App.getInstance().getUid(),App.getInstance().finishNum);
        }
        Toast.makeText(this,stringBuffer.toString(),Toast.LENGTH_SHORT).show();

        postedLabels = labels;
        tempLabels = labels;

        Calendar date = Calendar.getInstance();
        presenter.addPostTimeNum(App.getInstance().getUid(),
                date.get(Calendar.YEAR),date.get(Calendar.MONTH)+1,date.get(Calendar.DAY_OF_MONTH));

        jumpToMainActivity();
    }




    private void jumpToMainActivity() {


        if(!isLabeled){

            Log.d(TAG,"onLabelsPostSucceed unSigned");

            Intent intent = new Intent();
            setResult(MainActivity.RESULTCODE,intent);
            finish();
        }
        else{


            if(title == null || title.equals("")){

                finish();
                return;
            }
            if(title.equals("标记过的图片")){

                finish();
                return;
            }

            int flag = 0;
            for(int i = 0; i < tempLabels.size(); ++i){

                if(tempLabels.get(i).equals(title)){

                    flag = 1;
                    break;
                }
            }
            if(flag == 0){

                Intent intent = new Intent();
                setResult(HistoryImageActivity.RESULTCODE,intent);
                finish();
            }
            else{

                finish();
            }


        }

    }

    @Override
    public void onImageLabelAddedSucceed(String label) {


        onLabelsLoadSucceed(curLables);
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

    public static final int RESULTCODE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){

            case RESULTCODE:
                Log.d(TAG,"receive label");
                String label = data.getStringExtra(AddLabelDialog.LABEL);
                if(label == null || label.isEmpty()){

                    return;

                }
                for(int i = 0; i <curLables.size();++i){

                    if(curLables.get(i).equals(label)){

                        return;
                    }
                }
                curLables.add(label);
                onImageLabelAddedSucceed(label);
                break;
        }
    }

    private List<String> firstLabels;

    @Override
    public void onFirstLabelGetSuccess(List<String> list) {

        textViewFirst.setVisibility(View.VISIBLE);
        flowLayoutFirst.setVisibility(View.VISIBLE);

        firstLabels = list;

        initFlowLayoutFirst(list);
    }

    private void initFlowLayoutFirst(List<String> list){

        flowLayoutFirst.setAdapter(new TagAdapter<String >(list) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {

                TextView textView = (TextView) LayoutInflater.from(ImageDetailActivity.this)
                        .inflate(R.layout.tv,flowLayout,false);
                textView.setText(o);
                return textView;
            }
        });

    }

    @Override
    public void onFirstLabelGetFailed() {

    }

    private int counter = 0;

    @Override
    public void onRemovePicTagSuccess() {

        ++counter;

        if(counter == curLables.size()){

            post();
        }
    }
}
