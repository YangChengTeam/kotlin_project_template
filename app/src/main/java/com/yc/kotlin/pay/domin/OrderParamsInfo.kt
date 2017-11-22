package com.yc.kotlin.pay.domin

import com.yc.kotlin.domin.kmapOf

/**
 * Created by zhangkai on 2017/11/17.
 */
data class OrderParamsInfo(val payUrl: String, var goodsId: String, var type: String, var
price: Float, val name:
String){
    var goodsNum: Int = 1  //商品数量
    var md5SignStr = "" //现代支付 微信 md5校验值

    var paywayName= "" //支付方式
    var isPayWaySplit = 1 //支付方式 划分标识

    var dsMoeny: Float = 0f

    fun getParams() : Map<String, String?>{
        return kmapOf("goods_id" to goodsId,
                "goods_num" to goodsNum.toString(),
                "is_payway_split" to isPayWaySplit.toString(),
                "payway_name" to paywayName,
                "price_total" to price.toString(),
                "ds_money" to dsMoeny.toString(),
                "type" to type,
                "user_id" to "135",
                "user_name" to "test",
                "title" to "test",
                "app_id" to "1",
                "goods_list" to "[{'good_id': 123, 'num':1}]"
        )
    }
}