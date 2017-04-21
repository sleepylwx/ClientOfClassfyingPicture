package com.lwx.user.db.model;

import android.provider.ContactsContract;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

@DatabaseTable(tableName = "image")
public class Image {

    public static final String UUID_FIELD = "uuid";


    @DatabaseField(id = true, columnName = UUID_FIELD)
    public String uuid;

    @DatabaseField(columnName = "imgPath")
    public String imagePath;

    public Image(String uuid, String imagePath) {
        this.uuid = uuid;
        this.imagePath = imagePath;
    }

    public Image(){
    }
}
