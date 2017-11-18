package com.yc.kotlin.di.component

import com.yc.kotlin.App
import com.yc.kotlin.di.module.AppModule
import com.yc.kotlin.di.module.NetModule
import dagger.Component
import javax.inject.Singleton


/**
 * Created by zhangkai on 2017/11/10.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetModule::class))
interface NetComponent {
    fun inject(app: App)
}