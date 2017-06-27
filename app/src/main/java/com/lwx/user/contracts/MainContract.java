package com.lwx.user.contracts;

import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.User;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

public interface MainContract {

    public interface View extends BaseContract.View<MainContract.Presenter>,ToImageDetailContract,CheckTokenContract {


        void onUserLoadedSucceed(User user);
        void onUserLoadedFailed();

        void onImageLoadedSucceed(List<Image> imageList);
        //void onImageAddedSucceed(List<Image> imageList);

        void clearAndSaveList(List<Image> imageList);

        //adapter

        void startGetMorePicByNetWork();

        //view

        void showWaitingNetWork();
        void nonShowWaitingNetWork();
        void nonShowSwipe();


        void onLoadPicInDbError();

        void onImageLoadedFailed();


    }

    public interface Presenter extends BaseContract.Presenter<MainContract.View>{

        void getUser(long uid);
        void saveUser(User user);
        void getPictures(long uid,String token,int num);

        void getMorePicturesByNetWork(long uid,String token,int num);
        void clearAndGetPicByNetWork(long uid,String token,int num);

        //void getRandomPicsByNetWork(int num);
        void clearAndGetMoreRandomPicByNet(long uid,int num);

        void getMoreRandomPicturesByNetWork(long uid,int num);


    }

}
