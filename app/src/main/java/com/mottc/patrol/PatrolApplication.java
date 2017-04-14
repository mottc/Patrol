package com.mottc.patrol;

import android.app.Application;
import android.content.Context;

import com.mottc.patrol.data.source.local.DaoMaster;
import com.mottc.patrol.data.source.local.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/13
 * Time: 14:38
 */
public class PatrolApplication extends Application {
    public static Context applicationContext;
    private static PatrolApplication instance;

    public DaoSession mDaoSession;


    public static PatrolApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        // 初始化环信sdk
//        init(applicationContext);
    }



    public DaoSession getDaoSession() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "task.db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        return mDaoSession;
    }

}
