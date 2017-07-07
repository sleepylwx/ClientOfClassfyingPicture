package com.lwx.user.presenter;

import android.util.Log;

import com.lwx.user.App;
import com.lwx.user.contracts.LoginContract;
import com.lwx.user.model.UserImpl;
import com.lwx.user.model.UserRepo;
import com.lwx.user.model.model.User;
import com.lwx.user.net.UserAgent;
import com.lwx.user.net.UserAgentImpl;
import com.lwx.user.utils.PreferenceHelper;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 36249 on 2017/4/4.
 */

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View context;

    private UserRepo userRepo;

    private UserAgent userAgent;

    public static final String TAG = "LoginPresenter";
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
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> users) {

                        context.onAllUsersLoaded(users);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG,"loadAllUsers onError");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public static final String USERPASSWDNORMATCH = "Login failed, password not match!";
    @Override
    public void login(String user, String passwd) {

        userAgent.login(user,passwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {


                        userRepo.saveUser(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {

                                        App.getInstance().setToken(user.token);
                                        App.getInstance().setUid(user.uid);
                                        setCurrentLoginUid(user.uid);
                                        context.onLoginSucceed(user.uid);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                        context.onSaveUserError();
                                        Log.d(TAG,"login onNext onError" + user + "  " + passwd );
                                    }
                                });

                    }

                    @Override
                    public void onError(Throwable t) {

                        Log.d(TAG,"login onError" + user + " " + passwd);
                        t.printStackTrace();

                        if(USERPASSWDNORMATCH.equals(t.getMessage())){

                            context.onLoginNotMatch();
                        }
                        else{

                            context.onNetWorkError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static final String TOKENAUTHFAILED = "Auth failed, token incorrect!";

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

                        App.getInstance().setToken(token);
                        App.getInstance().setUid(uid);
                        setCurrentLoginUid(uid);
                        context.onLoginSucceed(uid);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG,"login token error" + uid + " " + token);

                        if(TOKENAUTHFAILED.equals(e.getMessage())){

                            context.onTokenAuthFailed();
                        }
                        else{
                            context.onNetWorkError();
                        }
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


    private void setCurrentLoginUid(long uid){

        new PreferenceHelper().setLogInUID(uid);
    }
}
