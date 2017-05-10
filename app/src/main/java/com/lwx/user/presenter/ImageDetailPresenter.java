package com.lwx.user.presenter;

import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;

import com.lwx.user.contracts.ImageDetailContract;
import com.lwx.user.db.ImageImpl;
import com.lwx.user.db.ImageRepo;
import com.lwx.user.db.LabelImpl;
import com.lwx.user.db.LabelRepo;
import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.Label;
import com.lwx.user.net.PictureAgent;
import com.lwx.user.net.PictureImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/5/10.
 */

public class ImageDetailPresenter implements ImageDetailContract.Presenter{


    private ImageDetailContract.View context;
    private ImageRepo imageRepo;
    private LabelRepo labelRepo;
    private PictureAgent pictureAgent;

    private String imageId;
    private Image image;

    public static final String TAG = "ImageDetailPresenter";
    public ImageDetailPresenter(ImageDetailContract.View context){

        this.context = context;
        this.imageRepo = ImageImpl.getInstance();
        this.labelRepo = LabelImpl.getInstance();
        this.pictureAgent = PictureImpl.getInstance();

    }


    @Override
    public ImageDetailContract.View getView() {

        return context;
    }

    @Override
    public void getImage(String uuid) {

        this.imageId = uuid;
        imageRepo.getImage(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Image>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Image image) {

                        ImageDetailPresenter.this.image = image;
                        context.onImageLoadSucceed(image.imagePath);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getImage onError");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getLabels(String uuid) {

        imageRepo.getImageLabels(uuid)
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

                        context.onLabelsLoadSucceed(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getLabel onError 数据库中没有" + uuid + "的标签");
                        pictureAgent.getPicTags(uuid)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<String>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(List<String> strings) {

                                        context.onLabelsLoadSucceed(strings);
                                        saveLabels(imageId,strings);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                        Log.d(TAG,"getLabel onError Double");
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

    private void saveLabels(String imageId,List<String> strings){

        imageRepo.saveLabels(imageId,strings);
    }

    @Override
    public void postLabels(List<String> labels) {


    }

    @Override
    public void addImageLabel(String label) {


    }
}