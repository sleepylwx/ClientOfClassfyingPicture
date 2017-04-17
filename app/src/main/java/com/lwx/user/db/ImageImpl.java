package com.lwx.user.db;

import com.lwx.user.db.model.Image;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/13.
 */

public class ImageImpl implements ImageRepo {

    @Override
    public Observable<List<Image>> getPictures(String label) {
        return null;
    }

    @Override
    public Observable<List<Image>> getAllPictures() {
        return null;
    }
}
