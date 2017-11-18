package com.yc.kotlin.pay

import android.app.Activity
import com.yc.kotlin.pay.domin.OrderInfo

/**
 * Created by zhangkai on 2017/11/17.
 */
open abstract class IPayImpl(protected var mContext: Activity) {
    abstract fun pay(orderInfo: OrderInfo, payCallback: IPayAbs.IPayCallback)
}