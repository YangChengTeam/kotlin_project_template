package com.yc.kotlin.di.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yc.kotlin.App
import com.yc.kotlin.helper.EncryptInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by zhangkai on 2017/11/10.
 */
@Module
class NetModule
(internal var mBaseUrl: String) {
    @Provides
    @Singleton
    internal fun provideOkHttpCache(application: App): Cache {
        val cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
        return Cache(application.getCacheDir(), cacheSize)
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(cache: Cache, loggingInterceptor: HttpLoggingInterceptor, encryptInterceptor: EncryptInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.addInterceptor(loggingInterceptor).addInterceptor(encryptInterceptor).cache(cache).build()

    }

    @Provides
    @Singleton
    internal fun provideLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    internal fun provideKeyErrorInterceptor(): EncryptInterceptor {
        return EncryptInterceptor()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build()
    }
}