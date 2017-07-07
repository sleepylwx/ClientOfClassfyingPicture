package com.lwx.user.model.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by henry on 17-4-21.
 */

@DatabaseTable(tableName = "imagelabel")
public class ImageLabel {

    public static final String ID_FIELD = "id";
    public static final String IMAGE_FIELD = "image_id";
    public static final String LABEL_FIELD = "label_id";

    public ImageLabel(){

    }

    public ImageLabel(Image image, Label label) {
        this.image = image;
        this.label = label;
    }

    @DatabaseField(generatedId = true,columnName = ID_FIELD)
    public Long id;

    @DatabaseField(columnName = IMAGE_FIELD, uniqueCombo = true, foreign = true)
    public Image image;

    @DatabaseField(columnName = LABEL_FIELD, uniqueCombo = true, foreign = true)
    public Label label;



}
