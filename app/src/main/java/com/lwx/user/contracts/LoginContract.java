package com.lwx.user.contracts;

import com.lwx.user.db.model.User;
import com.lwx.user.db.model.UserDetail;

import java.util.List;

/**
 * Created by 36249 on 2017/4/7.
 */

public interface LoginContract {

    interface View{

        void showNetWorkError();
        void showLoginSuccess();
        void onAllUserDetailLoaded(List<UserDetail> list);
    }

    interface Presenter{

        void loadAllUserDetails();
        void login(String user,String passwd);

    }
}
