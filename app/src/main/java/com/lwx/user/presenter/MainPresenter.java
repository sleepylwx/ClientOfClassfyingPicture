package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.App;
import com.lwx.user.contracts.MainContract;
import com.lwx.user.db.ImageImpl;
import com.lwx.user.db.ImageRepo;
import com.lwx.user.db.UserImpl;
import com.lwx.user.db.UserRepo;
import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.User;
import com.lwx.user.net.PictureAgent;
import com.lwx.user.net.PictureImpl;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/13.
 */

public class MainPresenter implements MainContract.Presenter {


    private MainContract.View context;
    private UserRepo userRepo;
    private UserAgent userAgent;
    private PictureAgent pictureAgent;
    private ImageRepo imageRepo;

    public static final String TAG = "MainPresenter";

    public MainPresenter(MainContract.View context){

        this.context = context;
        userRepo = new UserImpl();
        userAgent = UserAgentImpl.getInstance();
        pictureAgent = PictureImpl.getInstance();
        imageRepo = ImageImpl.getInstance();

    }
    @Override
    public MainContract.View getView() {

        return context;
    }

    @Override
    public void getUser(long uid) {

        userRepo.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(User user) {

                        context.onUserLoadedSucceed(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"getUser onError" + uid);
                        context.onUserLoadedFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getPictures(int num) {

        imageRepo.getAllPictures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(List<Image> images) {

                        Log.d(TAG,"getPictures onNext");
                        if(images == null || images.size() == 0){

                            pictureAgent.getPicByToken(App.getInstance().getToken(),num)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<List<Image>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(List<Image> imagess) {

                                            Log.d(TAG,"getPictures onNext onNext");
                                            imageRepo.saveImages(imagess)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new CompletableObserver() {
                                                        @Override
                                                        public void onSubscribe(@NonNull Disposable d) {

                                                        }

                                                        @Override
                                                        public void onComplete() {

                                                            Log.d(TAG,"NetWork Images store succeed!");
                                                        }

                                                        @Override
                                                        public void onError(@NonNull Throwable e) {

                                                            Log.d(TAG,"NetWork Images Store failed!");
                                                        }
                                                    });
                                            context.onImageLoadedSucceed(imagess);

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                            context.onNetWorkError();
                                            Log.d(TAG,"getPictures network onError");
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                        else{

                            context.onImageLoadedSucceed(images);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"getPictures onError" );
                        context.onImageLoadedSucceed(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void getMorePicturesByNetWork(long uid,int num) {

        pictureAgent.getPicByToken(App.getInstance().getToken(),num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Image> imageList) {


                        //context.nonShowWaitingNetWork();
                        context.nonShowSwipe();
                        context.onImageLoadedSucceed(imageList);

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getMorePictures onError" + uid + " " + num);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void clearAndGetMorePicByNetWork(long uid, int num) {

        pictureAgent.getPicByToken(App.getInstance().getToken(),num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Image> imageList) {

                        Log.d(TAG,"clearAndGetMorePictures onNext");
                        //context.nonShowWaitingNetWork();
                        context.nonShowSwipe();
                        context.clearAndSaveList(imageList);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"clearAndGetMorePictures onError" + uid + " " + num);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void getRandomPicsByNetWork(int num) {

        pictureAgent.getRandPic(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Image> images) {

                        Log.d(TAG,"getRandomPicsPicturesByNew onNext");
                        context.onImageLoadedSucceed(images);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getRandomPicsByNet onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void clearAndGetMoreRandomPicByNet(int num) {

        pictureAgent.getRandPic(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Image> images) {

                        Log.d(TAG,"clearAndGetMoreRandomPicByNet on Next");
                        //context.nonShowWaitingNetWork();
                        context.nonShowSwipe();
                        context.clearAndSaveList(images);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"clearAndGetMoreRandomPicByNet onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getMoreRandomPicturesByNetWork(int num) {

        pictureAgent.getRandPic(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Image> images) {

                        Log.d(TAG,"getMoreRandomPicturesByNetWork onNext");
                        context.onImageLoadedSucceed(images);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"getMoreRandomPicturesByNetWork onError");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

