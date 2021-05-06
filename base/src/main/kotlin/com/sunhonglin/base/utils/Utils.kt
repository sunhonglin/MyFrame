package com.sunhonglin.base.utils

import android.content.Context
import android.content.Intent
import android.text.Html
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