package com.lwx.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.MainContract;
import com.lwx.user.db.model.Image;
import com.lwx.user.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 36249 on 2017/4/13.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    private Context context;
    private List<Image> imageList;

    private ImageLoader imageLoader;

    public static final String TAG = "RecyclerViewAdapter";
    public RecyclerViewAdapter(Context context,List<Image> imageList){

        this.context = context;
        this.imageList = imageList;
        this.imageLoader = new ImageLoader();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){


        MainContract.View main = (MainContract.View)context;
        RecyclerView recyclerView = main.getRecyclerView();
//        if(!recyclerView.canScrollVertically(1)){
//
//            ((MainContract.View)context).startGetMorePicByNetWork();
//
//        }

        Image image = imageList.get(position);
        imageLoader.loadImage(context,image.imagePath,holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainContract.View)context).jumpToImageDetailActivity(image.uuid);
            }
        });



    }

    @Override
    public int getItemCount(){

        return imageList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.imageView)ImageView mImageView;

        public ViewHolder(View view){

            super(view);
            ButterKnife.bind(this,view);

        }

    }
}
