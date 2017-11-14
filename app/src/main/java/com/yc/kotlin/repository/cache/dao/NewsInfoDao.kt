package com.yc.kotlin.repository.cache.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.yc.kotlin.domin.NewsInfo

/**
 * Created by zhangkai on 2017/11/13.
 */
@Dao
interface NewsInfoDao {
    @Query("select * FROM news_info where id= :newsId")
    fun select(newsId: String): NewsInfo

    @Query("select * FROM news_info")
    fun loadAll(): LiveData<List<NewsInfo>>

    @Insert(onConflict = REPLACE)
    fun insert(newsInfos: List<NewsInfo>)
}