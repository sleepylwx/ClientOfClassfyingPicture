package com.lwx.user.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by 36249 on 2017/4/8.
 */

public class ImageLoader {

    public void loadImage(Context context,int resouceId, ImageView view){

        Glide.with(context).load(resouceId).into(view);
    }

    public void loadImage(Context context,String resourcePath,ImageView view){

        Glide.with(context).load(resourcePath).into(view);
    }
}
