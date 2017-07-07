package com.lwx.user.model.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/7/6.
 */

@DatabaseTable(tableName = "monthnum")
public class MonthNum {

    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String YEAR = "year";
    public static final String MONTH = "month";

    public static final String NUM = "num";

    @DatabaseField(columnName = ID,generatedId = true)
    public long id;

    @DatabaseField(columnName = UID,uniqueCombo = true)
    public long uid;

    @DatabaseField(columnName = YEAR,uniqueCombo = true)
    public int year;

    @DatabaseField(columnName = MONTH,uniqueCombo = true)
    public int month;

    @DatabaseField(columnName = NUM)
    public int num;

    public MonthNum(long uid, int year, int month){

        this.uid = uid;
        this.year = year;
        this.month = month;
        num = 0;
    }

    public MonthNum(){


    }

}
