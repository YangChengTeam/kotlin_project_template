package com.yc.kotlin.pay

import android.app.Activity
import com.yc.kotlin.domin.Config
import com.yc.kotlin.domin.retrofit
import com.yc.kotlin.pay.domin.OrderParamsInfo
import com.yc.kotlin.pay.net.PayService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by zhangkai on 2017/11/17.
 */
class Pay1Abs : IPayAbs {
    constructor(mContext: Activity) : super(mContext)

    val payService by lazy {
        retrofit.create(PayService::class.java)
    }

    override fun pay(orderParams: OrderParamsInfo, payCallback: IPayCallback) {
        super.pay(orderParams, payCallback)
        payService.getOrderInfo(orderParams.payUrl, orderParams.getParams()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                if(it.code == Config.HTTP_STAUTS_OK){
                    ipayImpl.pay(it.data, payCallback)
                }
        }, {

        })
    }

}