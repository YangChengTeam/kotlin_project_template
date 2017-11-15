package com.yc.kotlin.domin

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.google.gson.annotations.SerializedName
import com.yc.kotlin.App
import com.yc.kotlin.repository.cache.AppDataBase
import com.yc.kotlin.utils.*
import retrofit2.Retrofit
import java.util.zip.ZipFile

/**
 * Created by zhangkai on 2017/11/13.
 */

data class ResultInfo<T>(var code: Int, var msg: String, var data: T, @SerializedName("pub_key") var
publickey: String)

fun kmapOf(vararg params: Pair<String, String?>): Map<String, String?> {
    val defaultParams = Goagal.defaultParams
    val otherParams = mutableMapOf<String, String?>()
    params.forEach { (k, v) ->
        otherParams[k] = v
    }
    return defaultParams.merge(otherParams)
}

val appDatabase: AppDataBase by lazy {
    App.instance.appDatabase
}

val retrofit: Retrofit by lazy {
    App.instance.retrofit
}

object Config {
    const val ISSGIN = "issign"
    const val HTTP_STAUTS_OK = 1
    const val baseUrl = "http://en.wk2.com/api/"
}

object Goagal {
    var key: String? = null
        get() {
            var defaultKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAy/M1AxLZjZOyJToExpn1\n" +
                    "hudAWySRzS+aGwNVdX9QX6vK38O7WUA7h/bYqBu+6tTRC6RnL9BksMrIf5m6D3rt\n" +
                    "faYYmxfe/FI4ZD5ybIhFuRUi95e/J2vQVElsSNqSz7ewXquZpKZAqlzH4hGgOqmO\n" +
                    "THlrwiQwX66bS7x7kDmvxMd5ZRGhTvz62kpKb/orcnWQ1KElNc/bIzTtv3jsrMgH\n" +
                    "FVdFZfev91ew4Kf1YJbqGBGKslBsIoGsgTxI94T6d6XEFxSzdvrRwKhOobXIaOhZ\n" +
                    "o3GBCZIA/1ZOwLK6RyrWdprz+60xifcYIkILdZ7yPazSfHCVHFY6o/fQjK4dxQDW\n" +
                    "Gw0fxN9QX+v3+48nW7QIBx4KNYNIW/eetGhXpOwV4PjNt15fcwJkKsx2W3VQuh93\n" +
                    "jdYB4xMyDUnRwb9np/QR1rmbzSm5ySGkmD7NAj03V+O82Nx4uxsdg2H7EQdVcY7e\n" +
                    "6dEdpLYp2p+VkDd9t/5y1D8KtC35yDwraaxXveTMfLk8SeI/Yz4QaX6dolZEuUWa\n" +
                    "tLaye2uA0w25Ee35irmaNDLhDr804B7U7M4kkbwY7ijvvhnfb1NwFY5lw/2/dZqJ\n" +
                    "x2gH3lXVs6AM4MTDLs4BfCXiq2WO15H8/4Gg/2iEk8QhOWZvWe/vE8/ciB2ABMEM\n" +
                    "vvSb829OOi6npw9i9pJ8CwMCAwEAAQ==";
            var zf: ZipFile? = null
            val ze = zf?.getEntry("META-INF/rsa_public_key.pem")
            val `in` = zf?.getInputStream(ze)
            return readString(`in`) ?: defaultKey
        }


    val systemVersion: String? by lazy {
        if (android.os.Build.MODEL.contains(android.os.Build.BRAND))
            android.os.Build.MODEL + " " + android
                    .os.Build.VERSION.RELEASE
        else
            (Build.BRAND + " " + android
                    .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE);
    }

    val uuid: String? by lazy {
        var uid: String? = Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID)

        if (uid.isNullOrEmpty() || uid == "02:00:00:00:00:00") {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wInfo = wifiManager.connectionInfo
            uid = wInfo.macAddress
        }

        if (uid.isNullOrEmpty()) {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            uid = telephonyManager.deviceId
        }
        uid
    }

    val versionCode: Int? by lazy {
        getPackageInfo(context.packageName)?.versionCode
    }


    val defaultParams: Map<String, String?> by lazy {
        mapOf("device_type" to "2",
                "sv" to systemVersion,
                "imeil" to uuid,
                "app_version" to versionCode?.toString(),
                "ts" to "" + timestamp(),
                "flag" to "0")
    }
}