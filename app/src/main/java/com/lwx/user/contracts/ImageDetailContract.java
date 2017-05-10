package com.lwx.user.contracts;

import com.lwx.user.db.model.Image;

import java.util.List;

/**
 * Created by 36249 on 2017/5/10.
 */

public interface ImageDetailContract {

    public interface View extends BaseContract.View<ImageDetailContract.Presenter>{


        void onImageLoadSucceed(String path);
        void onLabelsLoadSucceed(List<String> labels);
        void onLabelsPostSucceed();
        void onImageLabelAddedSucceed();
    }

    public interface Presenter extends BaseContract.Presenter<ImageDetailContract.View>{


        void getImage(String uuid);
        void getLabels(String uuid);
        void postLabels(List<String> labels);
        void addImageLabel(String label);
    }

}
