package com.lwx.user.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lwx.user.utils.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 36249 on 2017/4/19.
 */

public class StrenthenToolBar extends RelativeLayout {

    private CircleImageView circleImageView;

    private ImageView imageView;


    private Context context;

    public StrenthenToolBar(Context context, AttributeSet attrs){

        this(context,attrs,0);
    }

    public StrenthenToolBar(Context context,AttributeSet attrs,int defStyleAttr){

        super(context,attrs,defStyleAttr);

        circleImageView = new CircleImageView(context);
        imageView = new ImageView(context);
        this.context = context;
        Glide.with(context).load(android.R.drawable.arrow_down_float).into(imageView);
    }

    @Override
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed,int l,int t,int r,int b){


        super.onLayout(changed,l,t,r,b);
        RelativeLayout.LayoutParams lp = ((RelativeLayout.LayoutParams)circleImageView.getLayoutParams());
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        lp.leftMargin = (int)(10*density);
        lp.topMargin = (int)(10*density);
        lp.bottomMargin = (int)(10*density);

        lp.leftMargin = (int)(10*density);
        lp.topMargin = (int)(10*density);
        lp.bottomMargin = (int)(10*density);

        imageView.setPadding((int)(5*density),(int)(5*density),(int)(5*density),(int)(5*density));


    }

    public void setHeaderPicture(ImageLoader imageLoader, String path){

        imageLoader.loadImage(context,path,circleImageView);
    }


    public CircleImageView getHeaderView(){

        return circleImageView;
    }
}
