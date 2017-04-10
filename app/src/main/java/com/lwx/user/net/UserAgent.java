package com.lwx.user.net;

import com.lwx.user.db.model.User;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/4.
 */

public interface UserAgent {

    Observable<User> login(String username, String password);

    Completable auth(String token);

    Completable logout(String token);

    Observable<String> getNickName(long uid);

    Completable setNickName(String token, String nickName);

    Completable signUp(String username,String password);
}
