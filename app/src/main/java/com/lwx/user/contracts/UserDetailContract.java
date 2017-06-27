package com.lwx.user.contracts;

import com.lwx.user.db.model.User;

/**
 * Created by 36249 on 2017/6/26.
 */

public interface UserDetailContract {

    public interface View extends BaseContract.View<UserDetailContract.Presenter>,CheckTokenContract{

        void onUserGetSuccess(User user);
        void onUserGetNetWorkError();

        void onUserSaveMessageSuccess();
        void onUserSaveMessageError();

        void onUserSaveHeaderSuccess();
        void onUserSaveHeaderError();

    }

    public interface Presenter extends BaseContract.Presenter<UserDetailContract.View>{


        void getUser(long uid,String token);
        void saveUserMessage(User user);
        void saveUserHeader(String token,String path);

        void saveUserInDb(User user);

    }
}
