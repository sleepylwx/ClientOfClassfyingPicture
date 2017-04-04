package com.lwx.user.net;

import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/4.
 */

public interface LoginAgent {

    /**
     *
     * @param token
     * @return
     */
    Observable<Boolean> checkToken(String token);
}
