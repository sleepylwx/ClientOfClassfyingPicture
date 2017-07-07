package com.lwx.user.contracts;

import com.lwx.user.model.model.Image;

import java.util.List;

/**
 * Created by 36249 on 2017/5/10.
 */

public interface ImageDetailContract {

    public interface View extends BaseContract.View<ImageDetailContract.Presenter>,CheckTokenContract{


        void onImageLoadSucceed(Image image);
        void onLabelsLoadSucceed(List<String> labels);
        void onLabelsPostSucceed(List<String> labels);
        void onImageLabelAddedSucceed(String label);
        //void onSignedLabelsLoadSucceed(List<Label> labels);
        void onChangeUnSignedImageToSignedSuccess();
        void onSaveSelectedLabelsByImageSuccess();

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

        //void getSignedLabels(long uid,String uuid);

        void changeUnSignedImageToSigned(long uid,String uuid,boolean oddLabeledState,boolean isLabeled);

        void saveSelectedLabelsByImage(long uid,Image image,List<String> labels);

        void finishTask(String token,int num);

        void addPostTimeNum(long uid,int year,int month,int day);

    }

}
