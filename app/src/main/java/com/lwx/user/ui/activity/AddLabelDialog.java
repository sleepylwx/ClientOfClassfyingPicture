package com.lwx.user.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.lwx.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 36249 on 2017/5/17.
 */

public class AddLabelDialog extends AppCompatActivity {


    @BindView(R.id.label_edittext)
    EditText editText;
    @BindView(R.id.add_label_button)
    Button button;
    @BindView(R.id.cancel_button)
    Button cancelButton;

    public static final String LABEL = "label";

    @OnClick(R.id.add_label_button)
    public void onClick() {

        String label = editText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(LABEL,label);
        setResult(ImageDetailActivity.RESULTCODE,intent);
        finish();
    }

    @OnClick(R.id.cancel_button)
    public void onClick1(){

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label_dialog);
        ButterKnife.bind(this);
    }


}
