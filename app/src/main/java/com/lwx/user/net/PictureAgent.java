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
     * @return
     * 作用跟getRandPic()类似，不过一次随机获得多张图片
     */
    Observable<List<Image>> getRandPictures();
    /**
     *
     * @param labels
     * @return
     *
     * 提交用户打好的标签集
     */
    Completable postPicTags(String token, List<String> labels);
}
