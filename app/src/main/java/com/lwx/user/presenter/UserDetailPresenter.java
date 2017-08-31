package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.contracts.UserDetailContract;
import com.lwx.user.model.UserImpl;
import com.lwx.user.model.UserRepo;
import com.lwx.user.model.model.User;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.ConstStringMessages;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/6/26.
 */

public class UserDetailPresenter implements UserDetailContract.Presenter {


    private UserDetailContract.View context;
    private UserAgent userAgent;
    private UserRepo userRepo;

    public UserDetailPresenter(UserDetailContract.View context){

        this.context = context;
        userAgent = UserAgentImpl.getInstance();
        userRepo = new UserImpl();

    }

    @Override
    public void getUser(long uid,String token) {


//        userRepo.getUser(uid)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<User>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(User user) {
//
//                        Log.d("UserDetailPresenter","db success");
//
//                        context.onUserGetSuccess(user);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Log.d("UserDetailPresenter","db error");
//
//                        context.onUserGetNetWorkError();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

        userAgent.getUserAllMessage(uid,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull User user) {

                        Log.d("UserDetailPresenter","network success");
                        context.onUserGetSuccess(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }
                        context.onNetWorkError();
                        Log.d("UserDetailPresenter","network error");
                        Log.d("UserDetailPresenter"," a + " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public UserDetailContract.View getView() {
        return context;
    }

    @Override
    public void saveUserMessage(User user) {

        userAgent.uploadUserMessage(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d("UserDetailPresenter","message success ");

                        context.onUserSaveMessageSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }

                        Log.d("UserDetailPresenter","message error ");

                        context.onUserSaveMessageError();
                    }
                });
    }

    @Override
    public void saveUserHeader(String token,String path) {

        userAgent.uploadHeadPic(token,path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d("UserDetailPresenter","header success ");

                        context.onUserSaveHeaderSuccess();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if(ConstStringMessages.TOKEN_ERROR.equals(e.getMessage())){

                            context.onTokenError();
                            context.jumpToLoginActivityForTokenError();
                            return;
                        }
                        Log.d("UserDetailPresenter","header error ");

                        context.onUserSaveHeaderError();
                    }

                });
    }


    @Override
    public void saveUserInDb(User user) {

        userRepo.saveUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {


                    }

                    @Override
                    public void onComplete() {

                        Log.d("UserDetailPresenter","db save success!");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d("UserDetailPresenter","db save error!");

                    }
                });
    }
}
