package com.lwx.user.contracts;

import com.lwx.user.db.model.User;

import java.util.List;

/**
 * Created by 36249 on 2017/4/7.
 */

public interface LoginContract {

    interface View extends BaseContract.View<LoginContract.Presenter>{

        void showNetWorkError();
        void showLoginSuccess();
        void onAllUsersLoaded(List<User> list);
        void onUsersEmpty();
        void onLoginSucceed();
    }

    interface Presenter{

        void loadAllUsers();
        void login(String user,String passwd);
        void login(String token);
        void deleteUser(User user);
        void saveUser(User user);

    }
}
