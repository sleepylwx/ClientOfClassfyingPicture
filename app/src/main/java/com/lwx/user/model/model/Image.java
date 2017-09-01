package com.lwx.user.model.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/4/13.
 */


public class Image {






    public String uuid;


    public String imagePath;


    public Long uid;




    public Image(Long uid, String uuid, String imagePath) {

        this.uuid = uuid;
        this.imagePath = imagePath;
        this.uid = uid;

    }

    public Image(Long uid, String uuid, String imagePath,boolean isLabled) {
        this.uuid = uuid;
        this.imagePath = imagePath;
        this.uid = uid;

    }

    public Image(){
    }

    @Override
    public String toString() {
        return "Image{" +
                "uuid='" + uuid + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

}

