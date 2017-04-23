package com.lwx.user.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.LoginContract;
import com.lwx.user.db.model.User;
import com.lwx.user.utils.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/8.
 */

public class ListPopWindowAdapter extends ArrayAdapter<User> {

    private int layout;
    private List<User> userList;
    private Context context;

    public ListPopWindowAdapter(Context context, @LayoutRes int layout, List<User> userList){

        super(context,layout,userList);
        this.context = context;
        this.layout = layout;
        this.userList = userList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        User user = userList.get(position);
        ViewHolder viewHolder;
        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(layout,null);
            viewHolder = new ViewHolder(convertView);
            viewHolder.imageView.setTag(position);
            convertView.setTag(viewHolder);
        }
        else{

            viewHolder = (ViewHolder)convertView.getTag();
        }
        //imageLoader.loadImage(context,user.headPath,viewHolder.circleImageView);
        Glide.with(context).load(user.headPath).into(viewHolder.circleImageView);
        viewHolder.textView.setText(user.user);
        viewHolder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.clear));
        //Glide.with(context).load(R.drawable.clear).into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onImageViewClicked(v);
            }
        });

        return convertView;
    }

    //
    //
    private void onImageViewClicked(View v){

        int index = (int)v.getTag();
        final User user = userList.get(index);
        userList.remove(index);

        ((LoginContract.View)(context)).getPresenter().deleteUser(user);

        if(userList.size() == 0){

            ((LoginContract.View)context).onUsersEmpty();
        }
        notifyDataSetChanged();
    }

    class ViewHolder{

        @BindView(R.id.item_list_pop_circleimageview)CircleImageView circleImageView;
        @BindView(R.id.item_list_pop_textview)TextView textView;
        @BindView(R.id.item_list_pop_imageview)ImageView imageView;

        public ViewHolder(View view){

            ButterKnife.bind(this,view);
        }
    }
}
