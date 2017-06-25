package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.contracts.HistoryImageContract;
import com.lwx.user.contracts.HistoryLabelContract;
import com.lwx.user.db.ImageImpl;
import com.lwx.user.db.ImageRepo;
import com.lwx.user.db.model.Label;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/6/24.
 */

public class HistoryLabelPresenter implements HistoryLabelContract.Presenter {

    private HistoryLabelContract.View context;
    private ImageRepo imageRepo;

    public static final String TAG = "HistoryLabelPresenter";

    public HistoryLabelPresenter(HistoryLabelContract.View context){

        this.context = context;
        imageRepo = ImageImpl.getInstance();

    }

    @Override
    public HistoryLabelContract.View getView() {

        return context;
    }

    @Override
    public void getSignedLabels(long uid) {

        imageRepo.getAllLabels(uid,true)
                .map(new Function<List<Label>, List<String>>() {

                    @Override
                    public List<String> apply(@NonNull List<Label> labels) throws Exception {

                        List<String> temp = new ArrayList<String>();

                        for(int i = 0; i < labels.size(); ++i){

                            temp.add(labels.get(i).label);
                        }

                        return temp;
                    }
                })
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
                        Log.d(TAG,"getSignedLabels error labels null or empty!");

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
