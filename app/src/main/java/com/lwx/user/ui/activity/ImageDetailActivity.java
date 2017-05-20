package com.lwx.user.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.ImageDetailContract;
import com.lwx.user.presenter.ImageDetailPresenter;
import com.lwx.user.utils.ImageLoader;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageDetailActivity extends Activity implements ImageDetailContract.View{

    public static final String IMAGEUUID = "IMAGEUUID";

    public static final int REQUESTCODE = 1;
    @BindView(R.id.photoview)PhotoView photoView;
    @BindView(R.id.flowlayout)TagFlowLayout flowLayout;
    @BindView(R.id.add_label)Button addLabel;
    @BindView(R.id.post_label)Button postLabel;

    @OnClick(R.id.add_label)
    public void onClick(){

        Intent intent = new Intent(this,AddLabelDialog.class);
        startActivityForResult(intent,REQUESTCODE);

    }

    @OnClick(R.id.post_label)
    public void onClick1(){


        Set<Integer> set = flowLayout.getSelectedList();
        if(set.size() < 1){

            Toast.makeText(this,R.string.selected_non_label,Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> selectedLabels = new ArrayList<>();
        for(Integer i : set){

            selectedLabels.add(curLables.get(i));
        }
        presenter.postSelectedLabels(App.getInstance().getToken(),uuid,selectedLabels);
    }
    private String uuid;
    private ImageDetailContract.Presenter presenter;
    private ImageLoader imageLoader;
    private List<String> curLables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        presenter = new ImageDetailPresenter(this);
        imageLoader = new ImageLoader();
        curLables = new ArrayList<>();
        init();

    }


    private void init(){

        Intent intent = getIntent();
        uuid = intent.getStringExtra(IMAGEUUID);
        initPhotoView(uuid);
        initLabels(uuid);
    }

    private void initPhotoView(String uuid){

        presenter.getImage(App.getInstance().getUid(),uuid);
    }

    private void initLabels(String uuid){

        presenter.getLabels(App.getInstance().getUid(),uuid);
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
    public void onImageLoadSucceed(String path) {

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
    }


    @Override
    public void onLabelsPostSucceed() {

        Toast.makeText(this,R.string.post_label_button,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onImageLabelAddedSucceed(String label) {

        presenter.saveImageLabel(label,uuid);
        onLabelsLoadSucceed(curLables);
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
}
