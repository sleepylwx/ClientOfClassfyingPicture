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




        void onFirstLabelGetSuccess(List<String> list);

        void onFirstLabelGetFailed();

        void onRemovePicTagSuccess();
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



        void finishTask(String token,int num);

        void addPostTimeNum(long uid,int year,int month,int day);

        void getFirstLabel(String uuid);

        void removePicTag(String token,String uuid,String tag);
    }

}
