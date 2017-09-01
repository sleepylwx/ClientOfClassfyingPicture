package com.lwx.user.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lwx.user.App;
import com.lwx.user.model.model.DayNum;
import com.lwx.user.model.model.MonthNum;
import com.lwx.user.model.model.User;
import com.lwx.user.model.model.YearNum;


/**
 * Created by henry on 17-2-16.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper {
    public static String DB_NAME = "cnsoftbei";
    public static int    VER_1   = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VER_1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, DayNum.class);
            TableUtils.createTable(connectionSource, MonthNum.class);
            TableUtils.createTable(connectionSource, YearNum.class);
            XLog.v("数据库：创建数据表成功!");
        } catch (Exception e){
            XLog.e("数据库： 创建表时失败！" , e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private static DbHelper mHelper = null;

    synchronized public static DbHelper getInstance(){
        if(mHelper == null)
            return mHelper = new DbHelper(App.getInstance());
        return mHelper;
    }
}
