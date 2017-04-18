package com.lwx.user.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

@DatabaseTable(tableName = "image")
public class Image {

    @DatabaseField(columnName = "uuid", id = true)
    public String uuid;

    @DatabaseField(columnName = "imgPath")
    public String imagePath;

    public List<String> labels;

    public Image(String uuid, String imagePath, List<String> labels) {
        this.uuid = uuid;
        this.imagePath = imagePath;
        this.labels = labels;
    }
}
