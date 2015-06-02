package com.metis.meishuquan.manager.common;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

/**
 * Created by wangjin on 15/6/2.
 */
public class DBManager {

    private static final String DB_NAME = "MSQ";//db名
    private static final int DB_VERSION = 1; //db版本
    private static DBManager dbManager;
    public static DbUtils db;

    private static Context mContext;

    private DBManager(Context context) {
        mContext = context;
    }

    public static DBManager getInctance(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    public DbUtils createDB() {
        if (db == null) {
            DbUtils.DaoConfig config = new DbUtils.DaoConfig(mContext);
            config.setDbName(DB_NAME);
            config.setDbVersion(DB_VERSION);
            db = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的
        }
        return db;
    }
}
