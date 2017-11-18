package com.yc.kotlin.pay.di.module

import android.app.Activity
import com.yc.kotlin.pay.IPayImpl
import com.yc.kotlin.pay.ways.H5PayImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by zhangkai on 2017/11/17.
 */
@Module
class PayImplModule(private val paywayName: String) {
    @Provides
    @Singleton
    fun providePayImpl(context: Activity): IPayImpl {
        var ipayImpl: IPayImpl
        when(paywayName){
            "h5" -> ipayImpl = H5PayImpl(context)
            else -> ipayImpl = H5PayImpl(context)
        }
        return ipayImpl
    }
}