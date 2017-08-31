package com.lwx.user.model.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 36249 on 2017/4/4.
 */

@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(columnName = "uid", id = true)
    public long uid;

    @DatabaseField(columnName = "token")
    public String token;

    @DatabaseField(columnName = "user")
    public String user;

    @DatabaseField(columnName = "headPath")
    public String headPath;

    @DatabaseField(columnName = "nickName")
    public String nickName;

    @DatabaseField(columnName = "num")
    public int num;

    @DatabaseField(columnName = "favorite")
    public String favorite;



    public Object extra;
    public User(long uid, String token, String user, String nickName) {
        this.uid = uid;
        this.token = token;
        this.user = user;
        this.nickName = nickName;
        num = 0;
    }

    public User(){}

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", token='" + token + '\'' +
                ", user='" + user + '\'' +
                ", headPath='" + headPath + '\'' +
                ", nickName='" + nickName + '\'' +
                ",favorite='" + favorite + '\'' +
                ",object = '" + (String)extra + '\''+
                '}';
    }
}
