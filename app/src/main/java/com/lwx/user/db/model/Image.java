package com.lwx.user.db.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

public class Image {

    @DatabaseField(columnName = "uid", id = true)
    public long uid;

    public String imagePath;

    public List<String> labels;


}
