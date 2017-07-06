package com.lwx.user.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/7/6.
 */

@DatabaseTable(tableName = "monthnum")
public class MonthNum {

    @DatabaseField(columnName = "id",generatedId = true)
    public long id;

    @DatabaseField(columnName = "uid",uniqueCombo = true)
    public long uid;

    @DatabaseField(columnName = "year",uniqueCombo = true)
    public int year;

    @DatabaseField(columnName = "month",uniqueCombo = true)
    public int month;

    @DatabaseField(columnName = "num")
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
