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


    /**
     * 获取某特定用户的所有被标记/未被标记的图片
     * @param uid 用户uid
     * @param isLabeled 获取的所有图片是否已经被标记
     * @return
     */
    Observable<List<Image>> getAllImages(long uid,boolean isLabeled);



    /**
     *
     * @param uid
     * @param imageId
     * @param isLabeled
     * @return
     */
    Observable<Image> getImage(long uid,String imageId,boolean isLabeled);


    /**
     *
     * @param uid
     * @param image
     * @param isLabeled
     * @return
     */
    Completable saveImage(long uid,Image image,boolean isLabeled);

    /**
     *
     * @param uid
     * @param images
     * @param isLabeled
     * @return
     */
    Completable saveImages(long uid,List<Image> images,boolean isLabeled);

    /**
     *
     * @param uid
     * @param imageId
     * @param isLabled
     * @return
     */
    //
    Observable<List<Label>> getLabelsByImage(long uid,String imageId,boolean isLabled);

    /**
     *
     * @param uid
     * @param isLabeled
     * @return
     */
    Observable<List<Label>> getAllLabels(long uid,boolean isLabeled);

    /**
     *
     * @param uid
     * @param imageId
     * @param labels
     * @param isLabeled
     * @return
     */
    //
    Completable saveLabels(long uid,String imageId, List<String>labels,boolean isLabeled);


    /**
     *
     * @param uid
     * @param imageId
     * @param labels
     * @param isLabeled
     * @return
     */
    //
    Completable saveLabel(long uid,String imageId,String labels,boolean isLabeled);


    /**
     *
     * @param uid
     * @param uuid
     * @param isLabeled
     * @return
     */
    Completable deleteImage(long uid,String uuid,boolean isLabeled);


    Completable deleteAllImages(long uid,boolean isLabeled);

    Completable deleteLabelByImage(long uid,String uuid,boolean isLabeled);

    Completable deleteAllLabels(long uid,boolean isLabeled);
    /**
     *
     * @param uid
     * @param label
     * @param isLabeled
     * @return
     */
    Observable<List<Image>> getImagesByLabel(long uid,String label,boolean isLabeled);

    Completable changeImageLabeledProperty(long uid,String uuid,boolean oddIsLabeled,boolean isLabeled);
}
