package com.lwx.user.db;

import com.lwx.user.db.model.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/4.
 */

public interface UserRepo {

    /**
     *
     *
     * @param uid
     * @return token which has stored in the server ,null if not
     */
    Observable<String> getToken(long uid);

    /**
     * if the token has in the db ,override it
     *
     * @param uid
     * @param token
     * @return
     */
    Observable<Boolean> saveToken(long uid,String token);

    Observable<List<User>> getAllUsers();

    Observable<Boolean> deleteUser(User user);

    Observable<Boolean> saveUser(User user);
}
