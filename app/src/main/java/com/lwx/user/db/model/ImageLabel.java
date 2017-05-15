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
    public static final String UPLOADED_FIELD = "upload";
    public ImageLabel(){

    }

    public ImageLabel(Image image, Label label) {
        this.image = image;
        this.label = label;
    }

    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(columnName = IMAGE_FIELD, uniqueCombo = true, foreign = true)
    public Image image;

    @DatabaseField(columnName = LABEL_FIELD, uniqueCombo = true, foreign = true)
    public Label label;

    @DatabaseField(columnName = UPLOADED_FIELD)
    public Boolean isUpload;

}
