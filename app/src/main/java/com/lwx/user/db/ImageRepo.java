package com.lwx.user.db;



import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.Label;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/13.
 */

public interface ImageRepo {


    Observable<List<Image>> getAllPictures(long uid);

    Observable<List<Image>> getPictures(long uid,String label);

    Completable saveImage(long uid,Image image);


    Observable<Image> getImage(long uid,String imageId);


    Observable<List<Label>> getImageLabels(long uid,String imageId);

    /**
     *
     * @param imageId
     * @param labels
     * @return
     * 将从网络获取到的标签集存储到本地
     */
    Completable saveLabels(long uid,String imageId, List<String>labels);

    /**
     *
     * @param imageId
     * @param labels
     * @return
     * 将用户自定义的标签存储到本地
     */
    Completable saveLabel(long uid,String imageId,String labels);


    Completable saveImages(long uid,List<Image> images);

    /**
     *
     * @param uid 用户id
     * @param uuid 图片uuid
     * @return
     */
    Completable deleteImage(long uid,String uuid);

    Completable deleteAllImages(long uid);



    Completable saveLabeledImage(String uuid,long uid,List<String> labels);

    Observable<List<Image>> getAllLabeledImage(long uid);

    Observable<List<String>> getLabeledImageLabels(String uuid,long uid);

}
