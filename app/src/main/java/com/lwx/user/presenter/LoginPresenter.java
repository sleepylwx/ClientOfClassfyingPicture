package com.lwx.user.presenter;

import android.content.Context;

import com.lwx.user.contracts.LoginContract;
import com.lwx.user.db.UserImpl;
import com.lwx.user.db.UserRepo;
import com.lwx.user.db.model.User;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.SafeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/4.
 */

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View context;

    private UserRepo userRepo;

    private UserAgent userAgent;
    public LoginPresenter(LoginContract.View context){

        this.context = context;
        userRepo = new UserImpl();
        userAgent = UserAgentImpl.getInstance();
    }

    @Override
    public void loadAllUsers() {

        userRepo.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->context.onAllUsersLoaded(s));

    }

    @Override
    public void login(String user, String passwd) {

        userAgent.login(user,passwd)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<User>(){

                    @Override
                    public void onSubscribe(Disposable d) {
                        d.dispose();
                    }

                    @Override
                    public void onNext(User user) {


                        userRepo.saveUser(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                                        if(aBoolean){

                                            context.onLoginSucceed(user.uid);
                                        }
                                        else{

                                            //
                                        }
                                        //
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable t) {

                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void login(long uid,String token) {

        userAgent.auth(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {


                    }

                    @Override
                    public void onComplete() {

                        context.onLoginSucceed(uid);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        e.printStackTrace();
                    }
                });

    }

    @Override
    public void deleteUser(User user) {

        userRepo.deleteUser(user);
    }

    @Override
    public void saveUser(User user) {

        userRepo.saveUser(user);
    }

    @Override
    public LoginContract.View getView() {
        return context;
    }
}
