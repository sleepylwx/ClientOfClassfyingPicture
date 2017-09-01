package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.contracts.ImageDetailContract;
import com.lwx.user.model.CounterImpl;
import com.lwx.user.model.CounterRepo;
import com.lwx.user.model.model.Image;
import com.lwx.user.net.PictureAgent;
import com.lwx.user.net.PictureImpl;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.ConstStringMessages;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/5/10.
 */

public class ImageDetailPresenter implements ImageDetailContract.Presenter{


    private ImageDetailContract.View context;


    private PictureAgent pictureAgent;
    private UserAgent userAgent;
    private CounterRepo counterRepo;



    public static final String TAG = "ImageDetailPresenter";
    public ImageDetailPresenter(ImageDetailContract.View context,boolean isLabeled){

        this.context = context;


        this.pictureAgent = PictureImpl.getInstance();
        this.userAgent = UserAgentImpl.getInstance();
        counterRepo = CounterImpl.getInstance();

    }


    @Override
    public ImageDetailContract.View getView() {

        return context;
    }

    @Override
    public void getImage(long uid,String uuid) {


        pictureAgent.getSpecificPic(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Image>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Image image) {

                        Log.d(TAG,"getPicture by net success!");
                        context.onImageLoadSucceed(image);

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getPicture by net error!");

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }



    @Override
    public void getLabels(long uid,String uuid,boolean isLabeled) {

        if(isLabeled){


            pictureAgent.getPicMarkedLabels(uid,uuid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull List<String> strings) {

                            if(strings == null || strings.size() == 0){

                                Log.d(TAG,"getLabels on NetWork NUll or size == 0");

                            }
                            else{

                                Log.d(TAG,"getLabels by network success!");
                                context.onLabelsLoadSucceed(strings);

                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                            Log.d(TAG,"getLabels by network error!");
                        }

                        @Override
                        public void onComplete() {


                        }
                    });

            return;
        }


        pictureAgent.getPicTags(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {

                        if(strings == null || strings.size() == 0){

                            Log.d(TAG,"getLabels on NetWork NUll or size == 0");

                        }
                        else{

                            Log.d(TAG,"getLabels by network success!");
                            context.onLabelsLoadSucceed(strings);

                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getLabels by network error!");

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }





    @Override
    public void postSelectedLabels(String token,String uuid,long uid,List<String> labels) {

        pictureAgent.markMutiTags(token,uuid,labels)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"postLables succeed!");
                        context.onLabelsPostSucceed(labels);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,"postSelectedLabels onError " + e.getMessage());
                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }

                        Log.d(TAG,"postSelectedLabels onError" + labels.size() + labels.get(0) + labels.get(labels.size() - 1));
                        e.printStackTrace();
                        context.onNetWorkError();

                    }
                });
    }






    @Override
    public void finishTask(String token, int num) {

        userAgent.finishTask(token,num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {

                        Log.d(TAG,"finishTask");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void addPostTimeNum(long uid, int year, int month, int day) {

        counterRepo.addDayNum(uid,year,month,day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"addDayNum success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

        counterRepo.addMonthNum(uid,year,month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"addMonthNum success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        counterRepo.addYearNum(uid,year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"addYearNum success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }


    @Override
    public void getFirstLabel(String uuid) {

        pictureAgent.getFirstLabel(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {


                    }

                    @Override
                    public void onNext(@NonNull List<String> strings) {

                        if(strings == null || strings.size() == 0){

                            context.onFirstLabelGetFailed();
                        }
                        else{

                            context.onFirstLabelGetSuccess(strings);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if("none".equals(e.getMessage())){

                            context.onFirstLabelGetFailed();

                        }
                        else{

                            context.onFirstLabelGetFailed();
                            //context.onNetWorkError();
                        }

                    }


                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void removePicTag(String token, String uuid, String tag) {

        pictureAgent.removePicTag(token,uuid,tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {


                    }

                    @Override
                    public void onComplete() {

                        context.onRemovePicTagSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }

                        context.onNetWorkError();
                    }
                });
    }
}
