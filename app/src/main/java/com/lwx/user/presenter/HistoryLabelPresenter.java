package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.contracts.HistoryLabelContract;
import com.lwx.user.net.PictureAgent;
import com.lwx.user.net.PictureImpl;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/6/24.
 */

public class HistoryLabelPresenter implements HistoryLabelContract.Presenter {

    private HistoryLabelContract.View context;
    private PictureAgent pictureAgent;

    public static final String TAG = "HistoryLabelPresenter";

    public HistoryLabelPresenter(HistoryLabelContract.View context){

        this.context = context;
        this.pictureAgent = PictureImpl.getInstance();

    }

    @Override
    public HistoryLabelContract.View getView() {

        return context;
    }

    @Override
    public void getSignedLabels(long uid) {

        pictureAgent.getUserMarkedLabels(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {


                        if(strings == null || strings.size() == 0){

                            context.onSignedLabelsLoadedFailed();
                            Log.d(TAG,"getSignedLabels error labels null or empty!");
                            return;
                        }


                        context.onSignedLabelsLoadedSuccess(strings);
                        Log.d(TAG,"getSignedLabels success!");
                    }



                    @Override
                    public void onError(Throwable e) {

                        context.onSignedLabelsLoadedFailed();
                        context.onNetWorkError();
                        Log.d(TAG,"getSignedLabels error labels null or empty!");

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
