package com.sunhonglin.base.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings

fun Context.installApp(filePath: String) {
    startActivity(getInstallAppIntent(filePath))
}

fun Context.goDeveloperSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
    startActivity(intent)
}