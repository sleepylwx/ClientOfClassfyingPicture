package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.contracts.HistoryStatisticsContract;
import com.lwx.user.model.ImageImpl;
import com.lwx.user.model.ImageRepo;
import com.lwx.user.model.model.Pair;
import com.lwx.user.net.PictureAgent;
import com.lwx.user.net.PictureImpl;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/7/5.
 */

public class HistoryStatisticsPresenter implements HistoryStatisticsContract.Presenter {


    private HistoryStatisticsContract.View context;
    private PictureAgent pictureAgent;
    private ImageRepo imageRepo;
    public HistoryStatisticsPresenter(HistoryStatisticsContract.View context){

        this.context = context;
        pictureAgent = PictureImpl.getInstance();
        imageRepo = ImageImpl.getInstance();
    }

    @Override
    public HistoryStatisticsContract.View getView() {
        return context;
    }

    @Override
    public void getTimeNum(long uid, int kind, Calendar start,Calendar end) {

        imageRepo.getTimeNum(uid,kind,start,end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Integer> integers) {

                        Log.d("HistoryStatistics","onNextSuccess");
                        context.onGetTimeNumSuccess(integers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getLabelsNum(long uid) {

        pictureAgent.getLabelsNum(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Pair>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Pair> list) {

                        context.onGetLabelsNumSuccess(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        context.onNetWorkError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTotalNum(long uid) {

        pictureAgent.getTotalLabelsNum(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {

                        context.onGetTotalNumSuccess(integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        context.onNetWorkError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
