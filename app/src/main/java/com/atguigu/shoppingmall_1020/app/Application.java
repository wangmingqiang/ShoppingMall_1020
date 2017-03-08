package com.atguigu.shoppingmall_1020.app;


import android.database.sqlite.SQLiteDatabase;

import com.atguigu.shoppingmall_1020.greendao.DaoMaster;
import com.atguigu.shoppingmall_1020.greendao.DaoSession;

/**
 * Created by wangmingqiang on 2017/3/8.
 */

public class Application extends android.app.Application {
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        //配置数据库
        setupDatabase();
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shop.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
