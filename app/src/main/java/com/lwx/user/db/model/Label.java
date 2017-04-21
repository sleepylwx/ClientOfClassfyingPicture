package com.lwx.user.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */

public class Label {

    public static final String LABEL_FIELD = "label";

    @DatabaseField(id = true,columnName = LABEL_FIELD)
    public String label;

    @DatabaseField(columnName = "uid")
    public Long userUID;

    public Label(){
    }

    public Label(String label, Long userUID) {
        this.label = label;
        this.userUID = userUID;
    }
}
