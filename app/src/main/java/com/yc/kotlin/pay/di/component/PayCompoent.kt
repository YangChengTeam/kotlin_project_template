package com.yc.kotlin.pay.di.component

import com.yc.kotlin.pay.IPayAbs
import com.yc.kotlin.pay.Pay1Abs
import com.yc.kotlin.pay.di.module.PayAbsModule
import com.yc.kotlin.pay.di.module.PayImplModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by zhangkai on 2017/11/17.
 */
@Singleton
@Component(modules = arrayOf(PayAbsModule::class, PayImplModule::class))
interface PayComponent {
    fun inject(iPayAbs: IPayAbs)
}