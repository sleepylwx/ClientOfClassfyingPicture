package com.lwx.user.db.model;

import android.provider.ContactsContract;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.field.types.LongObjectType;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

@DatabaseTable(tableName = Image.IMAGE_TABLE_NAME)
public class Image {

    public static final String IMAGE_TABLE_NAME = "image";
    public static final String UID_FIELD = "uid";
    public static final String UUID_FIELD = "uuid";

    @DatabaseField(columnName = "id", generatedId = true)
    public Long id;

    @DatabaseField(columnName = UUID_FIELD, uniqueCombo = true)
    public String uuid;

    @DatabaseField(columnName = "imgPath")
    public String imagePath;

    @DatabaseField(columnName = UID_FIELD, uniqueCombo = true)
    public Long uid;

    @DatabaseField(columnName = "isLabeled")
    public boolean isLabled;

    public Image(Long uid, String uuid, String imagePath) {
        this.uuid = uuid;
        this.imagePath = imagePath;
        this.uid = uid;
        isLabled = false;

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
