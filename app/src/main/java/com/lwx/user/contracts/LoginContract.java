package com.lwx.user.contracts;

import com.lwx.user.db.model.User;

import java.util.List;

/**
 * Created by 36249 on 2017/4/7.
 */

public interface LoginContract {

    public interface View extends BaseContract.View<LoginContract.Presenter>{

        void onAllUsersLoaded(List<User> list);
        void onUsersEmpty();
        void onLoginSucceed(long uid);
    }

    public interface Presenter extends BaseContract.Presenter<LoginContract.View>{

        void loadAllUsers();
        void login(String user,String passwd);
        void login(long uid,String token);
        void deleteUser(User user);
        void saveUser(User user);

    }
}
