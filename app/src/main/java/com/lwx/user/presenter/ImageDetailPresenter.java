package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.App;
import com.lwx.user.contracts.ImageDetailContract;
import com.lwx.user.model.ImageImpl;
import com.lwx.user.model.ImageRepo;
import com.lwx.user.model.model.Image;
import com.lwx.user.model.model.Label;
import com.lwx.user.net.PictureAgent;
import com.lwx.user.net.PictureImpl;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.ConstStringMessages;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/5/10.
 */

public class ImageDetailPresenter implements ImageDetailContract.Presenter{


    private ImageDetailContract.View context;
    private ImageRepo imageRepo;

    private PictureAgent pictureAgent;
    private UserAgent userAgent;
    private boolean isLabeled;



    public static final String TAG = "ImageDetailPresenter";
    public ImageDetailPresenter(ImageDetailContract.View context,boolean isLabeled){

        this.context = context;
        this.imageRepo = ImageImpl.getInstance();

        this.pictureAgent = PictureImpl.getInstance();
        this.userAgent = UserAgentImpl.getInstance();
        this.isLabeled = isLabeled;
    }


    @Override
    public ImageDetailContract.View getView() {

        return context;
    }

    @Override
    public void getImage(long uid,String uuid) {

        //this.imageId = uuid;
        imageRepo.getImage(uid,uuid,isLabeled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Image>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Image image) {


                        Log.d(TAG,"getImage success!");

                        context.onImageLoadSucceed(image);

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getImage onError");
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
                                        saveImage(uid,image,isLabeled);

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
                    public void onComplete() {

                    }
                });

    }



    @Override
    public void getLabels(long uid,String uuid,boolean isLabeled) {

        if(isLabeled){

            getLabelsInDb(uid,uuid,isLabeled);
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
                            getLabelsInDb(uid,uuid,isLabeled);
                        }
                        else{

                            Log.d(TAG,"getLabels by network success!");
                            context.onLabelsLoadSucceed(strings);
                            removeAndSaveLabels(uid,uuid,strings,isLabeled);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getLabels by network error!");
                        getLabelsInDb(uid,uuid,false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void getLabelsInDb(long uid,String uuid,boolean isLabeled){


        imageRepo.getLabelsByImage(uid,uuid,isLabeled)
                .map(new Function<List<Label>, List<String>>() {

                    @Override
                    public List<String> apply(List<Label> labels){

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

                        Log.d(TAG,"getLabels in Db success!");
                        context.onLabelsLoadSucceed(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getLabel onError 数据库中没有" + uuid + "的标签");

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void removeAndSaveLabels(long uid,String imageId,List<String> strings,boolean isLabeled){

        imageRepo.deleteLabelByImage(uid,imageId,isLabeled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"removeAndsaveunSignedLabels delete success!");

                        imageRepo.saveLabels(uid,imageId,strings,isLabeled)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {


                                    }

                                    @Override
                                    public void onComplete() {

                                        Log.d(TAG,"removeAndsaveunSignedLabels saveLabels success");
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                        Log.d(TAG,"removeAndsaveunSignedLabels saveLabels error");
                                    }
                                });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,"removeAndsaveunSignedLabels delete error!");
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
    public void saveImageLabel(String label,String uuid) {

        imageRepo.saveLabel(App.getInstance().getUid(),uuid,label,false)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"saveImageLabel success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,"saveImageLabel onError");
                    }
                });

    }


    private void saveImage(long uid,Image image,boolean isLabeled){

        imageRepo.saveImage(uid,image,isLabeled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"saveImage success!");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,"saveImage error!");
                    }
                });



    }


//    @Override
//    public void getSignedLabels(long uid, String uuid) {
//
//        imageRepo.getLabelsByImage(uid,uuid,true)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Label>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Label> labels) {
//
//                        Log.d(TAG,"getSignedLabels success");
//                        context.onSignedLabelsLoadSucceed(labels);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Log.d(TAG,"getSignedLabels error");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    @Override
    public void changeUnSignedImageToSigned(long uid, String uuid,boolean oddLabeledState,boolean isLabeled) {

        //image的Labeled属性切换后后 它的label自动清除
        imageRepo.changeImageLabeledProperty(uid,uuid,oddLabeledState,isLabeled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"removeThisImage success");
                        context.onChangeUnSignedImageToSignedSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,"removeThisImage error " + e.getMessage());
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void saveSelectedLabelsByImage(long uid, Image image, List<String> labels) {

        imageRepo.saveLabels(uid,image.uuid,labels,true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"saveSelectedImageAndLabels second success");
                        context.onSaveSelectedLabelsByImageSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,"saveSelectedImageAndLabels second error");
                        e.printStackTrace();

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

        imageRepo.addDayNum(uid,year,month,day)
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

        imageRepo.addMonthNum(uid,year,month)
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
        imageRepo.addYearNum(uid,year)
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
}
