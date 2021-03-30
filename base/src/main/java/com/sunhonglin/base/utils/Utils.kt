package com.sunhonglin.base.utils

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.Spanned
import kotlin.system.exitProcess

fun formatToHtml(value: String): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(
            value, Html.FROM_HTML_MODE_LEGACY
        )
    } else {
        Html.fromHtml(value)
    }
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
    }
    exitProcess(1)
}