package com.borf.bookman;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.borf.bookman.bean.DaoMaster;
import com.borf.bookman.bean.DaoSession;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler;

import org.greenrobot.greendao.database.Database;

public class MyApplication extends Application {
    public static boolean openSkinMake = false;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static DaoSession daoSession;
    public static Context getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initGreenDao();

        QMUILog.setDelegete(new QMUILog.QMUILogDelegate() {
            @Override
            public void e(String tag, String msg, Object... obj) {
                Log.e(tag, msg);
            }

            @Override
            public void w(String tag, String msg, Object... obj) {
                Log.w(tag, msg);
            }

            @Override
            public void i(String tag, String msg, Object... obj) {

            }

            @Override
            public void d(String tag, String msg, Object... obj) {

            }

            @Override
            public void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {

            }
        });

//        QDUpgradeManager.getInstance(this).check();
        QMUISwipeBackActivityManager.init(this);
//        QMUIQQFaceCompiler.setDefaultQQFaceManager(QDQQFaceManager.getInstance());
//        QDSkinManager.install(this);
//        QMUISkinMaker.init(context,
//                new String[]{"com.qmuiteam.qmuidemo"},
//                new String[]{"app_skin_"}, R.attr.class);
    }
    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bookman-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
    public static DaoSession getDaoSession() {
        return daoSession;
    }

}
