package com.lwx.user.model.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/4/13.
 */

@DatabaseTable(tableName = "image")
public class Image {

    public static final String ID_FIELD = "id";
    public static final String UID_FIELD = "uid";
    public static final String UUID_FIELD = "uuid";
    public static final String ISLABLED_FIELD = "isLabeled";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    public Long id;

    @DatabaseField(columnName = UUID_FIELD, uniqueCombo = true)
    public String uuid;

    @DatabaseField(columnName = "imgPath")
    public String imagePath;

    @DatabaseField(columnName = UID_FIELD, uniqueCombo = true)
    public Long uid;

    @DatabaseField(columnName =ISLABLED_FIELD,uniqueCombo = true)
    public boolean isLabeled;

    public Image(Long uid, String uuid, String imagePath) {

        this.uuid = uuid;
        this.imagePath = imagePath;
        this.uid = uid;
        this.isLabeled = false;
    }

    public Image(Long uid, String uuid, String imagePath,boolean isLabled) {
        this.uuid = uuid;
        this.imagePath = imagePath;
        this.uid = uid;
        this.isLabeled = isLabled;

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

