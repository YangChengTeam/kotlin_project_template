package com.yc.kotlin.helper

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.yc.kotlin.utils.Encrypt
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.StringReader
import java.lang.reflect.Type
import java.nio.charset.Charset


/**
 * Created by zhangkai on 2017/11/13.
 */
class JSONConvertFactory : Converter.Factory {
    private var gson: Gson

    companion object {
        fun create(): JSONConvertFactory {
            return create(Gson())
        }

        fun create(gson: Gson): JSONConvertFactory {
            return JSONConvertFactory(gson)
        }
    }

    constructor(gson: Gson?) {
        if (gson == null) {
            throw NullPointerException("gson == null")
        } else {
            this.gson = gson
        }
    }

    override  fun  responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?):
            Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return JSONResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type?,
                                      parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type!!))
        return JSONRequestBodyConverter(gson, adapter)
    }
}

internal class JSONRequestBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<T,
        RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, Encrypt.encodeForHttp(buffer.readByteString().utf8()))
    }

    companion object {
        private val MEDIA_TYPE = MediaType.parse("text/html; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }
}
internal class JSONResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) :
        Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val targetReader = StringReader(Encrypt.decodeForHttp(value.byteStream()))
        val jsonReader = gson.newJsonReader(targetReader)
        try {
            return adapter.read(jsonReader)
        } finally {
            value.close()
        }
    }
}