package com.yc.kotlin.utils

import android.util.Base64
import com.yc.kotlin.domin.Goagal
import java.io.*
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher

/**
 * Created by zhangkai on 2017/11/13.
 */


object Encrypt {
    private val ALGORITHM = "RSA"

    @Throws(NoSuchAlgorithmException::class, Exception::class)
    private fun getPublicKeyFromX509(algorithm: String,
                                     bysKey: String): PublicKey {
        val decodedKey = Base64.decode(bysKey, Base64.NO_WRAP)
        val x509 = X509EncodedKeySpec(decodedKey)
        val keyFactory = KeyFactory.getInstance(algorithm)
        return keyFactory.generatePublic(x509)
    }

    // rsa to byte
    fun rsa2ByteArray(content: String, key: String): ByteArray? {
        try {
            val pubkey = getPublicKeyFromX509(ALGORITHM, key)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, pubkey)
            val plaintext = content.toByteArray(charset("UTF-8"))
            return cipher.doFinal(plaintext)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // rsa to String
    fun rsaToString(content: String, key: String): String? {
        return rsa2ByteArray(content, key)?.let {
            String(it)
        }
    }

    // rsa字符分段
    private fun sectionStr(str: String): List<String> {
        var len = 128
        var strs = mutableListOf<String>()
        if (str.length <= len) {
            strs.add(str)
        } else {
            val section = str.length / len + if (str.length % len > 0) 1 else 0
            for (i in 0..(section - 1)) {
                val startIndex = i * len
                val endIndex = if ((i + 1) * len >= str.length) str.length - 1 else (i + 1) * len
                strs.add(str.substring(startIndex, endIndex))
            }

        }
        return strs.toList()
    }

    // rsa分段加密
    private fun rsa(jsonStr: String): String {
        val key = Goagal.key
        val strs = sectionStr(jsonStr)
        val bytesArray = mutableListOf<ByteArray>()
        var len = 0
        for (str in strs) {
            rsa2ByteArray(str, key)?.let {
                bytesArray.add(it)
                len += it.size
            }
        }

        var buffer = ByteArray(len)
        var start = 0
        for (bytes in bytesArray) {
            System.arraycopy(bytes, 0, buffer, start, bytes.size)
            start += bytes.size
        }
        return String(Base64.encode(buffer, Base64.NO_WRAP))
    }

    // 数据压缩
    fun compress(data: String): ByteArray? {
        try {
            val bytes = data.toByteArray()
            val bais = ByteArrayInputStream(bytes)
            val baos = ByteArrayOutputStream()
            compress(bais, baos)
            val output = baos.toByteArray()
            baos.flush()
            baos.close()
            bais.close()
            return output
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {

        }
        return null
    }

    @Throws(Exception::class)
    private fun compress(inputStream: InputStream, os: OutputStream) {
        val gos = GZIPOutputStream(os)
        val data = ByteArray(1024)
        var count: Int = inputStream.read(data, 0, data.size)
        while (count > 0) {
            gos.write(data, 0, count)
            count = inputStream.read(data, 0, data.size)
        }
        gos.finish()
        gos.close()
    }

    // 数据解压
    fun unzip(inputStream: InputStream?): String? {
        val gin: GZIPInputStream
        try {
            if (inputStream == null) {
                return null
            }
            gin = GZIPInputStream(BufferedInputStream(inputStream))
            val out = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var len: Int = gin.read(buf)
            while (len > 0) {
                out.write(buf, 0, len)
                len = gin.read(buf)
            }
            val result = String(out.toByteArray())
            gin.close()
            out.close()
            return result
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // 自定义解密
    private val k = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '*', '!')
    fun decode(str: String?): String? {
        return str?.let {
            var sb = StringBuffer()
            if (str.startsWith("x") && str.endsWith("y")) {
                var newStr = it.substring(1, it.length - 1)
                val strs = newStr.split("_".toRegex()).dropLastWhile { newStr.isEmpty() }.toTypedArray()

                for (i in strs.indices) {
                    sb.append((Integer.parseInt(strs[i]) - k[i % k.size].toInt()).toChar())
                }
            }
            sb.toString()
        }
    }

    // http request 加密
    fun encodeForHttp(str: String): ByteArray? {
        return compress(Encrypt.rsa(str))
    }

    // http response 解密
    fun decodeForHttp(inputStream: InputStream): String {
        val baseStr = decode(unzip(inputStream));
        return String(Base64.decode(baseStr, Base64.NO_WRAP))
    }
}
