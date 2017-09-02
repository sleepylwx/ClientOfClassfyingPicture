package com.lwx.user.model;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.query.Exists;
import com.lwx.user.model.model.User;

import java.util.List;
import java.util.concurrent.Exchanger;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
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
                List<User> users = userDao.queryForAll();
                observableEmitter.onNext(users);
                XLog.v("(数据库)获取全部用户:" + users);
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public Completable deleteUser(User user) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                userDao.delete(user);
                XLog.v("(数据库)删除用户:" + user);
                e.onComplete();
            }
        });
    }

    @Override
    public Completable saveUser(User user) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                userDao.createOrUpdate(user);
                XLog.v("(数据库)保存用户:" + user);
                e.onComplete();
            }
        });
    }

    @Override
    public Observable<User> getUser(long uid) {
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {
                User user = userDao.queryForId(uid);
                XLog.v("(数据库)获取用户:" +  user);
                e.onNext(user);
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

                XLog.v("(数据库)保存用户:" +  user);
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

                try{

                    User user = userDao.queryForId(uid);
                    if(user != null)
                        e.onNext(user.token);
                    else
                        e.onNext(null);
                    XLog.v("(数据库)获取Token:" +  (user == null ? "空" : user.token));
                    e.onComplete();

                }
                catch (Exception ex){

                    e.onError(ex);
                }

            }
        });
    }
}
