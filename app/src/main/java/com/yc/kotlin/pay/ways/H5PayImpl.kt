package com.yc.kotlin.pay.ways

import android.app.Activity
import com.yc.kotlin.pay.IPayAbs
import com.yc.kotlin.pay.IPayImpl
import com.yc.kotlin.pay.domin.OrderInfo

/**
 * Created by zhangkai on 2017/11/17.
 */
open class H5PayImpl : IPayImpl {
    constructor(mContext: Activity) : super(mContext)

    override fun pay(orderInfo: OrderInfo, payCallback: IPayAbs.IPayCallback) {

    }
}