package com.lwx.user.contracts;

import com.lwx.user.model.model.Image;

import java.util.List;

/**
 * Created by 36249 on 2017/5/22.
 */

public interface HistoryImageContract {

    public interface View extends BaseContract.View<HistoryImageContract.Presenter>,ToImageDetailContract {

        void onImageLoadedSucceed(List<Image> imageList);
        void onImageLoadedFailed();

    }


    public interface Presenter extends  BaseContract.Presenter<HistoryImageContract.View>{


        void getMarkedPics(long uid,String title);
        void getFinishedImages(long uid);
    }
}
