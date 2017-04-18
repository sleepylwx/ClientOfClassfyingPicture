package com.lwx.user.presenter;

import com.lwx.user.contracts.MainContract;
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

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public MainPresenter(MainContract.View context){

        this.context = context;
        userRepo = new UserImpl();
        userAgent = UserAgentImpl.getInstance();
        pictureAgent = PictureImpl.getInstance();
        imageRepo = new ImageImpl();

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

                        d.dispose();
                    }

                    @Override
                    public void onNext(User user) {

                        context.onUserLoadedSucceed(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                        context.onUserLoadedFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getPictures() {

        imageRepo.getAllPictures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        d.dispose();
                    }

                    @Override
                    public void onNext(List<Image> images) {

                        context.onImageLoadedSucceed(images);
                    }

                    @Override
                    public void onError(Throwable e) {

                        context.onImageLoadedSucceed(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
