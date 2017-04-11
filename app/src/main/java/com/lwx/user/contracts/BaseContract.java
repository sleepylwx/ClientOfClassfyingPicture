package com.lwx.user.contracts;

/**
 * Created by 36249 on 2017/4/8.
 */

public interface BaseContract {

    public interface View<T>{

        T getPresenter();
        void onNetWorkError();
    }

    public interface Presenter<T>{

        T getView();
    }
}
