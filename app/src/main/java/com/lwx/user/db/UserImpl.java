package com.lwx.user.db;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.dao.Dao;
import com.lwx.user.db.model.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by henry on 17-4-4.
 */

public class UserImpl implements UserRepo {

    private Dao<User, Long> userDao;

    public UserImpl(){
        try {
            userDao = DbHelper.getInstance().getDao(User.class);
        }catch (Exception e){
            XLog.e("SQL Exception");
        }
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<User>> observableEmitter) throws Exception {
                observableEmitter.onNext(userDao.queryForAll());
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public Observable<Boolean> deleteUser(User user) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                userDao.delete(user);
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public Observable<Boolean> saveUser(User user) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                userDao.create(user);
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public Observable<Boolean> saveToken(final long uid, final String token) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                User user = userDao.queryForId(uid);
                boolean isFresh = false;
                if(user == null) {
                    isFresh = true;
                    user = new User();
                }
                else
                    isFresh = false;

                user.uid = uid;
                user.token = token;

                userDao.create(user);

                e.onNext(isFresh);
                e.onComplete();

            }
        });
    }

    @Override
    public Observable<String> getToken(final long uid) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                User user = userDao.queryForId(uid);
                if(user != null)
                    e.onNext(user.token);
                else
                    e.onNext(null);
                e.onComplete();
            }
        });
    }
}
