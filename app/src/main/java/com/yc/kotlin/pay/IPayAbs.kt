package com.yc.kotlin.pay

import android.app.Activity
import android.support.annotation.CallSuper
import com.yc.kotlin.pay.di.component.DaggerPayComponent
import com.yc.kotlin.pay.di.module.PayAbsModule
import com.yc.kotlin.pay.di.module.PayImplModule
import com.yc.kotlin.pay.domin.OrderInfo
import com.yc.kotlin.pay.domin.OrderParamsInfo
import javax.inject.Inject

/**
 * Created by zhangkai on 2017/11/17.
 */
abstract class IPayAbs(protected var mContext: Activity) {

    @Inject lateinit var ipayImpl: IPayImpl

    @Override
    @CallSuper
    open fun pay(orderParams: OrderParamsInfo, payCallback: IPayCallback){
        DaggerPayComponent.builder().payAbsModule(PayAbsModule(mContext)).payImplModule(PayImplModule(orderParams
                .paywayName)).build()
                .inject(this)
    }


    interface IPayCallback {
        fun onSuccess(orderInfo: OrderInfo)
        fun onFailure(orderInfo: OrderInfo)
    }
}

