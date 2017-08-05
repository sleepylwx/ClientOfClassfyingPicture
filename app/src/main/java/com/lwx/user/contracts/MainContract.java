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

        void onAllPictureLoadedInDBSuccess(List<Image> images);

        void onImageLoadedDBSucceed(List<Image> images);

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

        void getAllPicturesInDb(long uid,boolean isLabeled);

        void savePicturesInDb(long uid,List<Image> images);

        void refreshUnLabeledImageDb(long uid,List<Image> images);

        void searchImages(String label, List<ImageSearch> imageSearchList);

        void getImagesLabels(List<Image> image,List<ImageSearch> imageSearchList);

        void getImagesByLabel(String label);
    }

}
