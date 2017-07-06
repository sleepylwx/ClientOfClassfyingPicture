package com.lwx.user.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/7/6.
 */

@DatabaseTable(tableName = "yearnum")
public class YearNum {


    @DatabaseField(generatedId = true,columnName = "id")
    public long id;
    @DatabaseField(columnName = "uid",uniqueCombo = true)
    public long uid;

    @DatabaseField(columnName = "year",uniqueCombo = true)
    public int year;

    @DatabaseField(columnName = "num")
    public int num;

    public YearNum(long uid,int year){

        this.uid = uid;
        this.year = year;
        this.num = 0;
    }

    public YearNum(){


    }

}
