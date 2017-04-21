package com.lwx.user.db;



import com.lwx.user.db.model.Image;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/13.
 */

public interface ImageRepo {


    Observable<List<Image>> getAllPictures();

    Observable<List<Image>> getPictures(String label);

    Completable saveImage(Image image);

}
