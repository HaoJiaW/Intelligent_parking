package com.github.jiawei.intelligent_parking_system.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// android中提供了SQLiteOpenHelper这个类来帮助你管理数据库
public class DBHelper extends SQLiteOpenHelper {

    public static final String DataBaseName="USER.DB";
    public static final int DB_VERSION=1;


    public DBHelper(Context context){
        super(context,DataBaseName,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        //获取创建表格的SQL
           String userSQL =DBHelper.UserTable.getCreatUserInfoSQL();
        //Z执行SQL
        db.execSQL(userSQL);

     /*   db.execSQL("CREATE TABLE IF NOT EXISTS person(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name VARCHAR,age INTEGER, info TEXT)");//建表，对数据库进行操作等*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+ DBHelper.UserTable.TBL_NAME);
        onCreate(db);
    }


    public static final class UserTable {
        public static final String TBL_NAME = "USERINFO";
        public static final String COL_ID = "ID";
        public static final String COL_NUMBER= "NUMBER";
        public static final String COL_PASSWORD= "PASSWORD";

        public static String getCreatUserInfoSQL() {
            String sql = "CREATE TABLE IF NOT EXISTS "
                    + TBL_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_NUMBER + " TEXT,"
                    + COL_PASSWORD + " TEXT,"
                    + ")";
            return sql;
        }
    }
}
