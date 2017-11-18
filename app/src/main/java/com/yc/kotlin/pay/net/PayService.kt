package com.yc.kotlin.pay.net

import com.yc.kotlin.domin.Config
import com.yc.kotlin.domin.ResultInfo
import com.yc.kotlin.pay.domin.OrderInfo
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Created by zhangkai on 2017/11/17.
 */
interface PayService {
    @Headers("${Config.ISSGIN}: true")
    @POST
    fun getOrderInfo(@Url url: String, @Body params: Map<String, String?>): Observable<ResultInfo<OrderInfo>>
}