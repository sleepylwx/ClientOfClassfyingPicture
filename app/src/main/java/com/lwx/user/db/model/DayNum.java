package com.lwx.user.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/7/5.
 */

@DatabaseTable(tableName = "daynum")
public class DayNum {

    @DatabaseField(columnName = "id",generatedId = true)
    public long id;

    @DatabaseField(columnName = "uid",uniqueCombo = true)
    public long uid;

    @DatabaseField(columnName = "year",uniqueCombo = true)
    public int year;

    @DatabaseField(columnName = "month",uniqueCombo = true)
    public int month;

    @DatabaseField(columnName = "day",uniqueCombo = true)
    public int day;

    @DatabaseField(columnName = "num")
    public int num;

    public DayNum(long uid, int year, int month, int day){

        this.uid = uid;
        this.year = year;
        this.month = month;
        this.day = day;
        num = 0;
    }

    public DayNum(){


    }

}
