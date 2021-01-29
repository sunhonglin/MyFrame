package com.sunhonglin.base.utils

import android.text.Html
import android.text.Spanned

fun formatToHtml(value: String): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(
            value, Html.FROM_HTML_MODE_LEGACY
        )
    } else {
        Html.fromHtml(value)
    }
}