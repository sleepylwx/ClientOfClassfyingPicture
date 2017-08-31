package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.contracts.HistoryImageContract;
import com.lwx.user.model.ImageImpl;
import com.lwx.user.model.ImageRepo;
import com.lwx.user.model.model.Image;
import com.lwx.user.net.PictureAgent;
import com.lwx.user.net.PictureImpl;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/5/22.
 */

public class HistoryImagePresenter implements HistoryImageContract.Presenter{

    public static final String TAG = "HistoryImagePresenter";

    private HistoryImageContract.View context;
    private ImageRepo imageRepo;
    private PictureAgent pictureAgent;

    public HistoryImagePresenter(HistoryImageContract.View context){

        this.context = context;
        imageRepo = ImageImpl.getInstance();
        pictureAgent = PictureImpl.getInstance();
    }

    @Override
    public HistoryImageContract.View getView() {

        return context;
    }

    @Override
    public void getLabeledImagesInDb(long uid,String title) {

        if(title.equals("标记过的图片")){

            imageRepo.getAllImages(uid,true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Image>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<Image> images) {

                            if(images == null || images.size() == 0){


                                Log.d(TAG,"getLabeledImagesInDb empty or null");
                                context.onImageLoadedFailed();
                                return;

                            }
                            Log.d(TAG,"getLabeledImagesInDb success!");
                            context.onImageLoadedSucceed(images);
                        }

                        @Override
                        public void onError(Throwable e) {

                            context.onImageLoadedFailed();
                            Log.d(TAG,"getLabeledImagesInDb error!");
                            e.printStackTrace();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
        else{

            imageRepo.getImagesByLabel(uid,title,true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Image>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<Image> images) {

                            context.onImageLoadedSucceed(images);
                            Log.d(TAG,"getLabeledImagesInDb success!");

                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.d(TAG,"getLabeledImagesInDb error!");
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }


    @Override
    public void getFinishedImages(long uid) {

        pictureAgent.getFinishedPics(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Image> images) {

                        if(images == null || images.equals("")){

                            context.onImageLoadedFailed();
                            return;
                        }
                        context.onImageLoadedSucceed(images);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        context.onImageLoadedFailed();
                        context.onNetWorkError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
