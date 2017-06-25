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
import com.lwx.user.utils.ConstStringMessages;

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

    private String nickName;
    private String headerPath;
    @Override
    public void getUser(long uid) {



        userAgent.getNickName(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                        Log.d(TAG,"getNickName success!");
                        nickName = s;
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getNickName failed");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        userAgent.getHeaderPath(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                        headerPath = s;
                        Log.d(TAG,"getHeaderPath success!");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"getHeaderPath failed");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        userRepo.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(User user) {

                        if(nickName != null){

                            user.nickName = nickName;
                        }
                        if(headerPath != null){

                            user.headPath = headerPath;
                        }
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
    public void getPictures(long uid,String token,int num) {

        pictureAgent.getPicByToken(token,num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Image> images) {

                        Log.d(TAG,"getPictures by network success!");
                        context.onImageLoadedSucceed(images);
                        addImageInDb(uid,images,"getPictures");
                    }

                    @Override
                    public void onError(Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){


                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }

                        Log.d(TAG,"getPictures by network error!");
                        imageRepo.getAllImages(uid,false)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<Image>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(List<Image> images) {

                                        Log.d(TAG,"getPictures in Db success!");
                                        if(images == null || images.size() == 0){

                                            Log.d(TAG,"getPictures in Db null or size = 0!");
                                            context.onLoadPicInDbError();
                                        }
                                        else{

                                            context.onImageLoadedSucceed(images);
                                        }

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                        Log.d(TAG,"getPictures in Db error!");
                                        context.onLoadPicInDbError();
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
    public void getMorePicturesByNetWork(long uid,String token,int num) {

        pictureAgent.getPicByToken(token,num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Image> imageList) {

                        Log.d(TAG,"getMorePicturesByNetWork by network success!");
                        //context.nonShowWaitingNetWork();
                        //context.nonShowSwipe();
                        context.onImageLoadedSucceed(imageList);
                        addImageInDb(uid,imageList,"getMorePicturesByNetWork");
                    }

                    @Override
                    public void onError(Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }
                        Log.d(TAG,"getMorePictures onError" + uid + " " + num);
                        e.printStackTrace();
                        context.onImageLoadedFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void clearAndGetPicByNetWork(long uid,String token, int num) {

        pictureAgent.getPicByToken(token,num)
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

                        deleteAndSaveImageInDb(uid,imageList,"clearAndGetPicByNetWork");
                    }

                    @Override
                    public void onError(Throwable e) {


                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }
                        Log.d(TAG,"clearAndGetMorePictures onError" + uid + " " + num);
                        e.printStackTrace();
                        context.nonShowSwipe();
                        context.onNetWorkError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


//    @Override
//    public void getRandomPicsByNetWork(int num) {
//
//        pictureAgent.getRandPic(num)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Image>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Image> images) {
//
//                        Log.d(TAG,"getRandomPicsPicturesByNew onNext");
//                        context.onImageLoadedSucceed(images);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Log.d(TAG,"getRandomPicsByNet onError");
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
    public void clearAndGetMoreRandomPicByNet(long uid,int num) {

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
                        deleteAndSaveImageInDb(uid,images,"clearAndGetMoreRandomPicByNet");

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"clearAndGetMoreRandomPicByNet onError");
                        e.printStackTrace();
                        context.nonShowSwipe();
                        context.onNetWorkError();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getMoreRandomPicturesByNetWork(long uid,int num) {

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
                        addImageInDb(uid,images,"getMoreRandomPicturesByNetWork");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"getMoreRandomPicturesByNetWork onError");
                        //
                        context.onImageLoadedFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void addImageInDb(long uid,List<Image> images,String funcName){

        imageRepo.saveImages(uid,images,false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,funcName + " save db success!");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,funcName + " save db error");

                    }
                });
    }
    private void deleteAndSaveImageInDb(long uid,List<Image> images,String funcName){

        imageRepo.deleteAllImages(uid,false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,funcName + " delete db success!");
                        imageRepo.saveImages(uid,images,false)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {

                                        Log.d(TAG,funcName + " save new db success!");
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                        Log.d(TAG,funcName + " save new db failed");
                                    }
                                });

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,funcName + " delete db error!");
                    }
                });
    }
}

