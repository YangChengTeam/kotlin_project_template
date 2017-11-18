package com.yc.kotlin.di.module

import android.arch.persistence.room.Room
import com.yc.kotlin.App
import com.yc.kotlin.repository.cache.AppDataBase
import dagger.Module
import javax.inject.Singleton
import dagger.Provides


/**
 * Created by zhangkai on 2017/11/10.
 */
@Module
class AppModule(private val app: App) {
    @Provides
    @Singleton
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideDataBase() : AppDataBase {
        return Room.databaseBuilder(app, AppDataBase::class.java, app.packageName).build()
    }
}