package com.lwx.user.net;

import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.ImageLabel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by henry on 17-4-11.
 */

public interface PictureAgent {
    Observable<Image> getRandPic();
    Observable<List<String>> getPicTags(String uuid);

    /**
     * 获取指定数目的Picture
     * @param num
     * @return
     */
    Observable<List<String>> getRandPic(Integer num);

    Observable<List<Image>> getPicByToken(String token, Integer num);

    /**
     * 根据用户喜好来推荐n张图片
     * @param token
     * @param num
     * @return
     */
    Observable<List<String>> getUserLikePics(String token, Integer num);

    /**
     * 针对一个用户token的一张图片uuid打List所指定的标签
     * @param token
     * @param uuid
     * @param tags
     * @return
     */
    Completable markMutiTags(String token, String uuid, List<String> tags);
}
