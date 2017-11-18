package com.yc.kotlin.pay.domin

import com.google.gson.annotations.SerializedName

/**
 * Created by zhangkai on 2017/11/17.
 */


data class OrderInfo(@SerializedName("money") var
                     price: Float, val name:
                     String, @SerializedName("viptype") var goodsId: String, @SerializedName("order_sn") var
                     orderSn: String, val message:
                     String,@SerializedName("params") var payInfo: Map<String, String?>)