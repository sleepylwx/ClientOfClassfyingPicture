package com.lwx.user.net;

import com.lwx.user.db.model.Image;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by henry on 17-4-11.
 */

public interface PictureAgent {
    Observable<Image> getRandPic();
    Observable<List<String>> getPicTags(String uuid);
    Observable<List<Image>> getPicByUId(long uid,int num);

    /**
     *
     * @param labels
     * @return
     *
     * 提交用户打好的标签集
     */
    Completable postPicTags(List<String> labels);
}
