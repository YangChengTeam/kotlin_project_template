package com.yc.kotlin

import android.app.Application
import com.yc.kotlin.di.compoent.DaggerNetComponent
import com.yc.kotlin.di.compoent.NetComponent
import com.yc.kotlin.di.module.AppModule
import com.yc.kotlin.di.module.NetModule
import com.yc.kotlin.domin.Config
import com.yc.kotlin.repository.cache.AppDataBase
import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by zhangkai on 2017/11/10.
 */

class App : Application() {

    @Inject lateinit var retrofit: Retrofit
    @Inject lateinit var appDatabase: AppDataBase

    val component: NetComponent by lazy {
        DaggerNetComponent
                .builder().appModule(AppModule(this)).netModule(NetModule(Config.baseUrl))
                .build()
    }

    companion object {
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        instance = this
    }
}