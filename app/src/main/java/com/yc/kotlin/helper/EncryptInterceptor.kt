package com.yc.kotlin.helper

import com.yc.kotlin.domin.Config
import com.yc.kotlin.domin.Goagal
import com.yc.kotlin.utils.Encrypt
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import java.nio.charset.Charset
import okhttp3.ResponseBody




/**
 * Created by zhangkai on 2017/11/14.
 */
class EncryptInterceptor : Interceptor {
    companion object {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
    }

    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain?.request()
        val response = chain?.proceed(request)
        request?.header(Config.ISSGIN)?.let {
            val body = request?.body()
            request = request?.newBuilder()!!
                    .post(RequestBody.create(MEDIA_TYPE,  Encrypt.encodeForHttp(body.toString())))
                    .build()

            val responseBody = ResponseBody.create(MEDIA_TYPE, Encrypt.decodeForHttp(response?.body()!!.byteStream()))
            return response.newBuilder().body(responseBody).build()
        }
        return response!!
    }

}