package com.lwx.user.contracts;

import com.lwx.user.db.model.Image;

import java.util.List;

/**
 * Created by 36249 on 2017/5/22.
 */

public class HistoryImageContract {

    public interface View extends BaseContract.View<HistoryImageContract.Presenter>{

        void onImageLoadedSucceed(List<Image> imageList);

    }


    public interface Presenter extends  BaseContract.Presenter<HistoryImageContract.View>{


        void getLabeledImageInDb(long uid);

    }
}
