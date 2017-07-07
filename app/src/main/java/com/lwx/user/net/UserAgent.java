package com.lwx.user.net;

import com.lwx.user.model.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/4.
 */

public interface UserAgent {

    Observable<User> login(String username, String password);

    Completable auth(String token);

    Completable logout(String token);

    Observable<String> getNickName(Long uid);

    Completable setNickName(String token, String nickName);

    Completable signUp(String username,String password);

    Observable<List<String>> getMarkedTags(Long uid);

    Completable markPicTag(String token, String uuid, String tagName);

    Completable uploadHeadPic(String token, String absolutePath);

    Observable<String>getHeaderPath(long uid);

    Observable<User> getUserAllMessage(long uid,String token);

    Completable uploadUserMessage(User user);


    Completable postFeedBack(String token,String content);

    Observable<Integer> finishTask(String token,int num);
}
