package com.lwx.user.contracts;

import android.support.v7.widget.RecyclerView;

import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.User;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

public interface MainContract {

    public interface View extends BaseContract.View<MainContract.Presenter>{


        void onUserLoadedSucceed(User user);
        void onUserLoadedFailed();

        void onImageLoadedSucceed(List<Image> imageList);
        void clearAndSaveList(List<Image> imageList);

        //adapter

        RecyclerView getRecyclerView();
        void startGetMorePicByNetWork(int num);
        void jumpToImageDetailActivity(String uuid);
        //view

        void showWaitingNetWork();
        void nonShowWaitingNetWork();
        void nonShowSwipe();
    }

    public interface Presenter extends BaseContract.Presenter<MainContract.View>{

        void getUser(long uid);
        void getPictures();

        void getMorePicturesByNetWork(long uid,int num);
        void clearAndGetMorePicByNetWork(long uid,int num);
    }

}
