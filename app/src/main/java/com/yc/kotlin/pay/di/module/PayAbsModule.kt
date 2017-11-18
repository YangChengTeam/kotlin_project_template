package com.yc.kotlin.pay.di.module

import android.app.Activity
import com.yc.kotlin.pay.IPayAbs
import com.yc.kotlin.pay.Pay1Abs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by zhangkai on 2017/11/17.
 */
@Module
class PayAbsModule(private val context: Activity) {
    @Provides
    @Singleton
    fun providePayAbs(): IPayAbs {
        return Pay1Abs(context)
    }

    @Provides
    @Singleton
    fun provideContext() = context
}