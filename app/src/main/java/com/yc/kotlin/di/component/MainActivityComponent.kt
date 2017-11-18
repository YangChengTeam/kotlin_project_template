package com.yc.kotlin.di.component

import com.yc.kotlin.di.module.MainActivityModule
import com.yc.kotlin.pay.di.module.PayAbsModule
import com.yc.kotlin.ui.activitys.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by zhangkai on 2017/11/14.
 */
@Singleton
@Component(modules = arrayOf(MainActivityModule::class, PayAbsModule::class))
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}