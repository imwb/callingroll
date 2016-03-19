package com.example.wb.calling.activity;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

import cn.bmob.newim.BmobIM;

/**
 * Created by wb on 16/1/29.
 */
public class MyApp extends Application {
    public static final String APPID = "18e3c5746a84e4abcebb8ec8f5b96a0a";

    public static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("calling.db")
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                }
            });
    @Override
    public void onCreate() {
        super.onCreate();
        BmobIM.init(this);
        x.Ext.init(this);
    }
}
