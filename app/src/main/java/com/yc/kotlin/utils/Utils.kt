package com.yc.kotlin.utils

import android.app.Activity
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Environment
import android.os.Handler
import android.os.Looper
import com.yc.kotlin.App
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Created by zhangkai on 2017/11/11.
 */
val context: Application by lazy {
    App.instance
}

val gUiHandler by lazy {
    Handler(Looper.getMainLooper())
}

//dp 、px 之间的转换
inline fun density() = context.getResources().getDisplayMetrics().density

inline fun Float.densityConvert(func: Float.(Float) -> Int) = func(density())
inline fun Float.dp2px() = densityConvert { (this * it + 0.5f).toInt() }
inline fun Float.px2dp() = densityConvert { (this / it + 0.5f).toInt() }

//sp 、px 之间的转换
inline fun scaledDensity() = context.getResources().getDisplayMetrics().scaledDensity

inline fun Float.scaledDensityConvert(func: Float.(Float) -> Int) = func(scaledDensity())
inline fun Float.sp2px() = scaledDensityConvert { (this * it + 0.5f).toInt() }
inline fun Float.px2sp() = scaledDensityConvert { (this / it + 0.5f).toInt() }

//设置屏幕横竖
inline fun Activity.setLandscape() = this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

inline fun Activity.setPortrait() = this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//获取屏幕宽高
inline fun getScreenWidth() = context.getResources().getDisplayMetrics().widthPixels

inline fun getSeenHeight() = context.getResources().getDisplayMetrics().heightPixels

//判断屏幕横竖
inline fun isLandscape(): Boolean = context.getResources().getConfiguration().orientation === Configuration.ORIENTATION_LANDSCAPE

inline fun isPortrait(): Boolean = context.getResources().getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT

//判断屏幕锁屏
inline fun isScreenLock() = (context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).inKeyguardRestrictedInputMode();

//判断平板
inline fun isTablet() = context.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE;

//函数主线程运行
fun post(r: () -> Unit) = gUiHandler.post(r)

fun postDelayed(delay: Long, r: () -> Unit) = gUiHandler.postDelayed(r, delay)

//函数运行子线程
fun rxrun(r: () -> Unit) = Single.fromCallable(r).subscribeOn(Schedulers.newThread()).subscribe()


// 创建目录
fun makeDir(path: String): String {
    val dir = File(path)
    if (!dir.exists()) dir.mkdir()
    return path
}

// 创建外部存储根目录
fun makeBaseDir() = makeDir(Environment.getExternalStorageDirectory().toString() + "/" + context.packageName)

// 创建基于根目录的文件夹
fun makeDirByBase(name: String) = makeDir(makeBaseDir() + name)

// 从流读取字符串
fun readString(`in`: InputStream?): String? {
    try {
        val br = BufferedReader(InputStreamReader(`in`))
        val result = StringBuffer()
        var line: String = br.readLine()
        while (line != null) {
            result.append(line + "\n")
            line = br.readLine()
        }
        return result.toString()
    } catch(e : Exception){

    }
    return null
}

// 获取应用包信息
fun getPackageInfo(packageName: String): PackageInfo? {
    try {
        val pm = context.getPackageManager()
        return pm.getPackageInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return null
    }
}

// 时间戳
fun timestamp() = System.currentTimeMillis().toString()


// 合并map
fun <K, V> Map<K, V>.merge(other: Map<K, V>): Map<K, V> {
    val result = mutableMapOf<K, V>()
    result.putAll(this)
    other.forEach {
        (k, v)->
        result[k] = v
    }
    return result
}

