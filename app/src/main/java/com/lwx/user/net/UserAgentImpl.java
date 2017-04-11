package com.lwx.user.net;

import android.icu.lang.UScript;

import com.elvishew.xlog.XLog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lwx.user.db.model.User;
import com.lwx.user.net.rx.StringConverterFactory;
import com.lwx.user.net.rx.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by henry on 17-4-6.
 */
public class UserAgentImpl implements UserAgent{

    UserService userService = null;

    private static UserAgentImpl ourInstance = new UserAgentImpl();

    public static UserAgentImpl getInstance() {
        return ourInstance;
    }

    private UserAgentImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ttxs.ac.cn:8080/")
                .addConverterFactory(StringConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
    }

    public Observable<User> login(String username, String password){
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {
                try {
                    Response<String> response = userService.login(username, password).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onNext(new User(jsonObject.getLong("uid"), jsonObject.getString("token")));

                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });

    }

    public Completable auth(String token){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.auth(token).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                    }
                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public Completable logout(String token){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.logout(token).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public Observable<String> getNickName(long uid){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                try {
                    Response<String> response = userService.getNickName(uid).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onNext(jsonObject.getString("nickname"));
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public Completable setNickName(String token, String nickName){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.setNickName(token, nickName).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }
                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }


}
