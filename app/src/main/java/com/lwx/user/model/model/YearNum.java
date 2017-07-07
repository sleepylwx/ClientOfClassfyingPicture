package com.lwx.user.model.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/7/6.
 */

@DatabaseTable(tableName = "yearnum")
public class YearNum {


    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String YEAR = "year";

    public static final String NUM = "num";

    @DatabaseField(generatedId = true,columnName = ID)
    public long id;
    @DatabaseField(columnName = UID,uniqueCombo = true)
    public long uid;

    @DatabaseField(columnName = YEAR,uniqueCombo = true)
    public int year;

    @DatabaseField(columnName = NUM)
    public int num;

    public YearNum(long uid,int year){

        this.uid = uid;
        this.year = year;
        this.num = 0;
    }

    public YearNum(){


    }

}
