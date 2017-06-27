package com.lwx.user.contracts;

/**
 * Created by 36249 on 2017/6/26.
 */

public interface CheckTokenContract {

    void jumpToLoginActivityForTokenError();

    void onTokenError();
}
