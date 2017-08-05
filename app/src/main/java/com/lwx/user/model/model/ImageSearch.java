package com.lwx.user.model.model;

import java.util.List;

/**
 * Created by 36249 on 2017/8/5.
 */

public class ImageSearch {


    public Image image;
    public List<String> labels;

    public ImageSearch(Image image, List<String> labels) {

        this.image = image;
        this.labels = labels;

    }

    public ImageSearch() {

    }
}
