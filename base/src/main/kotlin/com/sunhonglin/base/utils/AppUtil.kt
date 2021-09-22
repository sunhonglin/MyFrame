package com.sunhonglin.base.utils

import android.content.Context

class AppUtil {
    companion object {
        fun installApp(context: Context, filePath: String) {
            context.startActivity(IntentUtil.getInstallAppIntent(context, filePath))
        }
    }
}