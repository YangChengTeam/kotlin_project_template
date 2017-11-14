package com.yc.kotlin.di.module

import com.yc.kotlin.ui.activitys.MainActivity
import com.yc.kotlin.ui.wdigets.adapters.MainAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by zhangkai on 2017/11/14.
 */
@Module
class MainActivityModule() {
    @Provides
    @Singleton
    fun provideMainAdapter() = MainAdapter()
}