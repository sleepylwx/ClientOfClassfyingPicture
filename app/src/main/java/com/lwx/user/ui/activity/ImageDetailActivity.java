package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.lwx.user.R;
import com.lwx.user.contracts.ImageDetailContract;
import com.lwx.user.presenter.ImageDetailPresenter;
import com.lwx.user.utils.ImageLoader;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.List;

import butterknife.BindView;

public class ImageDetailActivity extends AppCompatActivity implements ImageDetailContract.View{

    public static final String IMAGEUUID = "IMAGEUUID";

    @BindView(R.id.photoview)PhotoView photoView;
    @BindView(R.id.flowlayout)FlowLayout flowLayout;

    private String uuid;
    private ImageDetailContract.Presenter presenter;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        presenter = new ImageDetailPresenter(this);
        imageLoader = new ImageLoader();
        init();

    }

    private void init(){

        Intent intent = getIntent();
        uuid = intent.getStringExtra(IMAGEUUID);
        initPhotoView(uuid);
        initLabels(uuid);
    }

    private void initPhotoView(String uuid){

        presenter.getImage(uuid);
    }

    private void initLabels(String uuid){

        presenter.getLabels(uuid);
    }

    @Override
    public void onLabelsLoadSucceed(List<String> labels) {


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

        imageLoader.loadImage(this,path,photoView);
    }


    @Override
    public void onLabelsPostSucceed() {

    }

    @Override
    public void onImageLabelAddedSucceed() {

    }
}
