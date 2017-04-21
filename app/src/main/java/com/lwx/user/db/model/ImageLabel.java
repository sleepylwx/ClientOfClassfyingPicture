package com.lwx.user.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by henry on 17-4-21.
 */

@DatabaseTable(tableName = "ImageLabel")
public class ImageLabel {
    public static final String IMAGE_FIELD = "image_id";
    public static final String LABEL_FIELD = "label_id";

    public ImageLabel(){

    }

    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(columnName = IMAGE_FIELD)
    public Image image;

    @DatabaseField(columnName = LABEL_FIELD)
    public Label label;

}
