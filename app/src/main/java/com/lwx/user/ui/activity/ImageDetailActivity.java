package com.lwx.user.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
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
import butterknife.OnClick;

public class ImageDetailActivity extends AppCompatActivity implements ImageDetailContract.View{

    public static final String IMAGEUUID = "IMAGEUUID";

    @BindView(R.id.photoview)PhotoView photoView;
    @BindView(R.id.flowlayout)TagFlowLayout flowLayout;
    @BindView(R.id.add_label)Button addLabel;
    @BindView(R.id.post_label)Button postLabel;

    @OnClick(R.id.add_label)
    public void onClick(){

        Intent intent = new Intent(this,AddLabelDialog.class);
        startActivity(intent);

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
        presenter.postSelectedLabels(selectedLabels);
    }
    private String uuid;
    private ImageDetailContract.Presenter presenter;
    private ImageLoader imageLoader;
    private List<String> curLables;
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

        curLables = labels;
        flowLayout.setAdapter(new TagAdapter<String >(labels) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {

                TextView textView = (TextView) LayoutInflater.from(ImageDetailActivity.this)
                        .inflate(R.layout.label_view,flowLayout,false);
                textView.setText(o);
                return textView;
            }
        });

        addLabel.setVisibility(View.VISIBLE);
        postLabel.setVisibility(View.VISIBLE);
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

        Toast.makeText(this,R.string.post_label_button,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onImageLabelAddedSucceed(String label) {

        presenter.saveImageLabel(label);
        onLabelsLoadSucceed(curLables);
    }




    public class AddLabelDialog extends AppCompatActivity {

        @BindView(R.id.label_edittext)EditText editText;
        @BindView(R.id.add_label_button) Button button;

        @OnClick(R.id.add_label_button)
        public void onClick(){

            String label = editText.getText().toString();
            curLables.add(label);
            onImageLabelAddedSucceed(label);
            finish();
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_label_dialog);
        }
    }

}
