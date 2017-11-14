package com.yc.kotlin.domin

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by zhangkai on 2017/11/13.
 */



@Entity(tableName = "news_info")
data class NewsInfo(var title: String){
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0
}


data class NewsInfoWrapper(var info: NewsInfo)