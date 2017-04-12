package com.lwx.user.net;

import io.reactivex.Observable;

/**
 * Created by henry on 17-4-11.
 */

public interface PictureAgent {
    Observable<String> getRandPic();
    Observable<String> getPicTags(String uuid);
}
