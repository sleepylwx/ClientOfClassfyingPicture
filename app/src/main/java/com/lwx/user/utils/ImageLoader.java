package com.lwx.user.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

/**
 * Created by 36249 on 2017/4/8.
 */

public class ImageLoader {


    public void loadImage(Context context,int resouceId, ImageView view){

        Glide.with(context).load(resouceId)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    public static final String TAG = "ImageLoader";
    public void loadImage(Context context,String resourcePath,ImageView view){

        Log.d(TAG,view + " " + resourcePath);
        Glide.with(context).load(resourcePath).
                diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }


    public void loadImageWithListener(Context context,String resourcePath,ImageView view,RequestListener<String,GlideDrawable> listener){

        Glide.with(context).load(resourcePath).
                diskCacheStrategy(DiskCacheStrategy.ALL).listener(listener)
                .into(view);
    }
}
