package com.yc.kotlin.repository.cache

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.yc.kotlin.domin.NewsInfo
import com.yc.kotlin.repository.cache.dao.NewsInfoDao

/**
 * Created by zhangkai on 2017/11/13.
 */


@Database(entities = arrayOf(NewsInfo::class), version = 1)
abstract class AppDataBase  : RoomDatabase() {
    abstract fun newsInfoDao(): NewsInfoDao
}