package com.github.jiawei.intelligent_parking_system.utils;

import org.xutils.DbManager;
import org.xutils.x;

public class MyDbManager  {

    public static int Version=1;

    public static DbManager.DaoConfig daoConfig=new DbManager.DaoConfig().setDbName("data.db")
            .setDbVersion(Version).setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    //开启WAL
                    db.getDatabase().enableWriteAheadLogging();
                }
            }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                }
            });

    public static DbManager getDbManger(){
        return x.getDb(daoConfig);
    }
}
