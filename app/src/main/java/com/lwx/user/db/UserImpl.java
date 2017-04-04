package com.lwx.user.db;

import com.j256.ormlite.dao.Dao;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by henry on 17-4-4.
 */

public class UserImpl implements UserRepo {

    private Dao<>

    public UserImpl(){

    }

    @Override
    public Observable<Boolean> saveToken(long uid, String token) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {

            }
        });
    }

    @Override
    public Observable<String> getToken(long uid) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

            }
        });
    }
}
