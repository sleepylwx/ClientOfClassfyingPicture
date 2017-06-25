package com.lwx.user.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by 36249 on 2017/4/13.
 */
@DatabaseTable(tableName = "label")
public class Label {

    public static final String ID_FIELD = "id";
    public static final String LABEL_FIELD = "label";
    public static final String UID_FIELD = "uid";
    public static final String POST_FIELD = "isPost";

    @DatabaseField(generatedId = true,columnName = ID_FIELD)
    public long id;
    @DatabaseField(columnName = LABEL_FIELD,uniqueCombo = true)
    public String label;

    @DatabaseField(columnName = UID_FIELD,uniqueCombo = true)
    public Long uid;

    @DatabaseField(columnName = POST_FIELD,uniqueCombo = true)
    public boolean isPost;

    @DatabaseField(columnName = "counter")
    public long counter;

    public Label(){


    }

    public Label(String label, Long userUID) {
        this.label = label;
        this.uid = userUID;
        this.isPost = false;
        counter = 0;
    }

    public Label(String label, Long userUID,boolean isPost) {

        this.label = label;
        this.uid = userUID;
        this.isPost = isPost;
        counter = 0;
    }


}
