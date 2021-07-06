package com.borf.bookman;

import android.app.Application;
import android.content.Context;

import com.borf.bookman.bean.DaoMaster;
import com.borf.bookman.bean.DaoSession;

import org.greenrobot.greendao.database.Database;

public class MyApplication extends Application {
    private static Context mContext;
    private static DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initGreenDao();

    }
    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bookman-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
    static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getInstance() {
        return mContext;
    }
}
