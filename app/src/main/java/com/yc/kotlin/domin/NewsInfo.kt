package com.yc.kotlin.domin

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by zhangkai on 2017/11/13.
 */


@Entity(tableName = "news_info")
data class NewsInfo(var title: String) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeLong(id)
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0

    constructor(parcel: Parcel) : this(parcel.readString()) {
        id = parcel.readLong()
    }

    companion object CREATOR : Parcelable.Creator<NewsInfo> {
        override fun createFromParcel(parcel: Parcel): NewsInfo {
            return NewsInfo(parcel)
        }

        override fun newArray(size: Int): Array<NewsInfo?> {
            return arrayOfNulls(size)
        }
    }
}

data class NewsInfoWrapper(var list: List<NewsInfo>)