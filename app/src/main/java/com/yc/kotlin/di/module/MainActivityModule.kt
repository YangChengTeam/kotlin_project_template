package com.yc.kotlin.di.module

import android.arch.lifecycle.ViewModelProviders
import com.yc.kotlin.ui.activitys.MainActivity
import com.yc.kotlin.ui.wdigets.adapters.MainAdapter
import com.yc.kotlin.viewmodel.NewsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by zhangkai on 2017/11/14.
 */
@Module
class MainActivityModule(private val activity: MainActivity) {
    @Provides
    @Singleton
    fun provideMainAdapter() = MainAdapter()

    @Provides
    @Singleton
    fun provideNewsViewModel(): NewsViewModel {
       return ViewModelProviders.of(activity).get(NewsViewModel::class.java)
    }
}