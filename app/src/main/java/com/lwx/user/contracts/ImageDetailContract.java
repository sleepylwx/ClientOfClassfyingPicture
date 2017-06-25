package com.lwx.user.contracts;

import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.Label;

import java.util.List;

/**
 * Created by 36249 on 2017/5/10.
 */

public interface ImageDetailContract {

    public interface View extends BaseContract.View<ImageDetailContract.Presenter>{


        void onImageLoadSucceed(Image image);
        void onLabelsLoadSucceed(List<String> labels);
        void onLabelsPostSucceed(List<String> labels);
        void onImageLabelAddedSucceed(String label);
        void onSignedLabelsLoadSucceed(List<Label> labels);
    }

    public interface Presenter extends BaseContract.Presenter<ImageDetailContract.View>{


        void getImage(long uid,String uuid);
        void getLabels(long uid,String uuid,boolean isLabeled);

        /**
         *
         * @param labels
         * 提交用户打好的标签
         */
        void postSelectedLabels(String token,String uuid,long uid,List<String> labels);

        /**
         *
         * @param label
         * 将用户自定义的label添加到本地数据库
         */
        void saveImageLabel(String label,String uuid);

        void getSignedLabels(long uid,String uuid);

        void changeUnSignedImageToSigned(long uid,String uuid);

        void saveSelectedLabelsByImage(long uid,Image image,List<String> labels);

    }

}
