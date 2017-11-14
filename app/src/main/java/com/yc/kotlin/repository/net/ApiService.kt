package com.yc.kotlin.repository.net

import com.yc.kotlin.domin.ResultInfo
import com.yc.kotlin.domin.NewsInfoWrapper
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by zhangkai on 2017/11/13.
 */

interface ApiService {
    @POST("news/info")
    fun getNewsInfo(@Body params: Map<String, String?>): Observable<ResultInfo<NewsInfoWrapper>>
}