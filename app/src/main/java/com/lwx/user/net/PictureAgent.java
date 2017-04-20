package com.lwx.user.net;

import com.lwx.user.db.model.Image;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by henry on 17-4-11.
 */

public interface PictureAgent {
    Observable<Image> getRandPic();
    Observable<List<String>> getPicTags(String uuid);

}
