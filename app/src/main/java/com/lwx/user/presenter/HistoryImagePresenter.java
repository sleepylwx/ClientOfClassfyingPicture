package com.lwx.user.presenter;

import com.lwx.user.contracts.HistoryImageContract;

/**
 * Created by 36249 on 2017/5/22.
 */

public class HistoryImagePresenter implements HistoryImageContract.Presenter{


    private HistoryImageContract.View context;


    public HistoryImagePresenter(HistoryImageContract.View context){

        this.context = context;
    }

    @Override
    public HistoryImageContract.View getView() {

        return context;
    }

    @Override
    public void getLabeledImageInDb(long uid) {


    }
}
