package com.sunhonglin.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.Spanned
import androidx.core.text.HtmlCompat
import kotlin.system.exitProcess

fun formatToHtml(value: String): Spanned {
    return HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
    }
    exitProcess(1)
}

fun Context.getAppVersionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}

fun Context.getAndroidId(): String {
    return Settings.System.getString(
        contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

fun Context.postMainThread(unit: () -> Unit) {
    (this as Activity).runOnUiThread {
        unit()
    }
}