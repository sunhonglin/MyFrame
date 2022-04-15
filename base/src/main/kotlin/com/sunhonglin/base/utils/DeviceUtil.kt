package com.sunhonglin.base.utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import androidx.annotation.Nullable
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@SuppressLint("PrivateApi")
object DeviceUtil {
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_FLYME_VERSION_NAME = "ro.build.display.id"
    private const val FLYME = "flyme"
    private val MEIZUBOARD = arrayOf("m9", "M9", "mx", "MX")

    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null
    private val BRAND = Build.BRAND.lowercase(Locale.getDefault())
    private var isInfoReaded = false

    private fun checkReadInfo() {
        if (isInfoReaded) {
            return
        }
        isInfoReaded = true
        val properties = Properties()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream =
                    FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
                properties.load(fileInputStream)
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                fileInputStream?.close()
            }
        }
        var clzSystemProperties: Class<*>? = null
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod = clzSystemProperties.getDeclaredMethod(
                "get",
                String::class.java
            )
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME)
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    /**
     * 判断是否是flyme系统
     */
    private val isFlymeValue: OnceReadValue<Void, Boolean> =
        object : OnceReadValue<Void, Boolean>() {
            override fun read(param: Void?): Boolean {
                checkReadInfo()
                return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName!!.contains(FLYME)
            }
        }

    fun isFlyme(): Boolean {
        isFlymeValue.get()?.let {
            return it
        }
        return false
    }

    /**
     * 判断是否是MIUI系统
     */
    fun isMIUI(): Boolean {
        checkReadInfo()
        return !TextUtils.isEmpty(sMiuiVersionName)
    }

    fun isMiuiVersion(v: Int): Boolean {
        checkReadInfo()
        return "v$v" == sMiuiVersionName
    }

    fun isFlymeLowerThan(majorVersion: Int, minorVersion: Int = 0, patchVersion: Int = 0): Boolean {
        checkReadInfo()
        var isLower = false
        if (!sFlymeVersionName.isNullOrBlank()) {
            try {
                val pattern: Pattern = Pattern.compile("(\\d+\\.){2}\\d")
                val matcher: Matcher = pattern.matcher(sFlymeVersionName)
                if (matcher.find()) {
                    val versionString: String = matcher.group()
                    if (versionString.isNotEmpty()) {
                        val version = versionString.split("\\.").toTypedArray()
                        if (version.isNotEmpty()) {
                            if (version[0].toInt() < majorVersion) {
                                isLower = true
                            }
                        }
                        if (version.size >= 2 && minorVersion > 0) {
                            if (version[1].toInt() < majorVersion) {
                                isLower = true
                            }
                        }
                        if (version.size >= 3 && patchVersion > 0) {
                            if (version[2].toInt() < majorVersion) {
                                isLower = true
                            }
                        }
                    }
                }
            } catch (ignore: Throwable) {
            }
        }
        return isMeizu() && isLower
    }


    private val isMeizuValue: OnceReadValue<Void, Boolean> =
        object : OnceReadValue<Void, Boolean>() {
            override fun read(param: Void?): Boolean {
                checkReadInfo()
                return isPhone(MEIZUBOARD) || isFlyme()
            }
        }

    private fun isMeizu(): Boolean {
        isMeizuValue.get()?.let {
            return it
        }
        return false
    }

    private val isEssentialPhoneValue: OnceReadValue<Void, Boolean> =
        object : OnceReadValue<Void, Boolean>() {
            override fun read(param: Void?): Boolean {
                return BRAND.contains("essential")
            }
        }

    fun isEssentialPhone(): Boolean {
        isEssentialPhoneValue.get()?.let {
            return it
        }
        return false
    }

    private fun isPhone(boards: Array<String>): Boolean {
        checkReadInfo()
        val board = Build.BOARD ?: return false
        for (board1 in boards) {
            if (board == board1) {
                return true
            }
        }
        return false
    }

    @Nullable
    private fun getLowerCaseName(p: Properties, get: Method, key: String): String? {
        var name = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: Exception) {
            }
        }
        if (name != null) name = name.lowercase(Locale.getDefault())
        return name
    }
}

abstract class OnceReadValue<P, T> {
    @Volatile
    private var isRead = false
    private var cacheValue: T? = null
    operator fun get(param: P? = null): T? {
        if (isRead) {
            return cacheValue
        }
        synchronized(this) {
            if (!isRead) {
                cacheValue = read(param)
                isRead = true
            }
        }
        return cacheValue
    }

    protected abstract fun read(param: P?): T
}