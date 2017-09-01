package com.lwx.user.contracts;

import com.lwx.user.model.model.Image;
import com.lwx.user.model.model.ImageSearch;
import com.lwx.user.model.model.User;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

public interface MainContract {

    public interface View extends BaseContract.View<MainContract.Presenter>,ToImageDetailContract,CheckTokenContract {


        void onUserLoadedSucceed(User user);
        void onUserLoadedFailed();

        void onImageLoadedSucceed(List<Image> imageList);


        void clearAndSaveList(List<Image> imageList);

        void onAllMarkedPictureLoaded(List<Image> images);
        //adapter

        void startGetMorePicByNetWork();

        //view


        void nonShowSwipe();



        void onImageLoadedFailed();



        void onImageSearchSucceed(List<Image> images);

        void onImageSearchFailed();

        void onGetImagesByLabelSucceed(List<Image> images);
        void onGetImagesByLabelFailed();
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

        void getAllMarkedPictures(long uid);



        void searchImages(String label, List<ImageSearch> imageSearchList);

        void getImagesLabels(List<Image> image,List<ImageSearch> imageSearchList);

        void getImagesByLabel(String label);
    }

}
