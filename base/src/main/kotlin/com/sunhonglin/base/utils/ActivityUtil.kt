@file:Suppress("UNCHECKED_CAST")

package com.sunhonglin.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.Serializable

fun ComponentActivity.permissionContracts(unit: (granted: Boolean) -> Unit) =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        unit(it)
    }

fun ComponentActivity.multiplePermissionContracts(unit: (map: Map<String, Boolean>) -> Unit) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        unit(it)
    }

fun ComponentActivity.multiplePermissionContractsPass(unit: (allPass: Boolean) -> Unit) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        unit(map.filter { !it.value }.isEmpty())
    }

fun ComponentActivity.activityResultDefaultContracts(unit: (result: ActivityResult) -> Unit) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        unit(it)
    }

inline fun <reified T : Activity> Context.skipActivity(
    vararg extras: Pair<String, Any>? = emptyArray(),
    requestCode: Int? = null
) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(*extras)
    requestCode?.let {
        if (this is Activity) {
            startActivityForResult(intent, it)
            return
        }
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Context.skipActivityAndFinish(
    context: Context,
    vararg extras: Pair<String, Any>? = emptyArray()
) {
    skipActivity<T>(*extras)
    if (context is Activity) with(context) {
        finish()
    }
}

inline fun <reified T : Activity> setResultAndFinish(
    context: Context,
    vararg extras: Pair<String, Any>? = emptyArray(),
    resultCode: Int
) {
    val intent = Intent(context, T::class.java)
    intent.putExtras(*extras)
    if (context is Activity) with(context) {
        setResult(resultCode, intent)
        finish()
    }
}

fun Intent.putExtras(
    vararg extras: Pair<String, Any>?
): Intent {
    if (extras.isEmpty()) return this
    extras.forEach {
        it?.let { (key, value) ->
            when (value) {
                is Boolean -> putExtra(key, value)
                is Byte -> putExtra(key, value)
                is Char -> putExtra(key, value)
                is Short -> putExtra(key, value)
                is Int -> putExtra(key, value)
                is Long -> putExtra(key, value)
                is Float -> putExtra(key, value)
                is Double -> putExtra(key, value)
                is String -> putExtra(key, value)
                is Bundle -> putExtra(key, value)
                is IntArray -> putExtra(key, value)
                is ByteArray -> putExtra(key, value)
                is CharArray -> putExtra(key, value)
                is LongArray -> putExtra(key, value)
                is FloatArray -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
                is ShortArray -> putExtra(key, value)
                is DoubleArray -> putExtra(key, value)
                is BooleanArray -> putExtra(key, value)
                is CharSequence -> putExtra(key, value)
                is Array<*> -> {
                    when {
                        value.isArrayOf<String>() ->
                            putExtra(key, value)
                        value.isArrayOf<Parcelable>() ->
                            putExtra(key, value)
                        value.isArrayOf<CharSequence>() ->
                            putExtra(key, value)
                        else -> putExtra(key, value)
                    }
                }
                is Serializable -> putExtra(key, value)
            }
        }
    }
    return this
}

fun <T> Intent.get(
    key: String
): T? {
    try {
        this.extras?.let {
            return (it.get(key) as T)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}