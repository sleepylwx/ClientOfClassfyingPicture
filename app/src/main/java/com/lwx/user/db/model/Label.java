package com.lwx.user.db.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

public class Label {

    @DatabaseField(id = true, columnName = "id")
    public Long id;

    @DatabaseField
    public String label;

    public List<Long> imageUids;

}
