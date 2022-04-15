package com.sunhonglin.base.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.io.File

fun Context.getInstallAppIntent(filePath: String): Intent? {
    //apk文件的本地路径
    val apkFile = File(filePath)
    if (!apkFile.exists()) {
        return null
    }
    val intent = Intent(Intent.ACTION_VIEW)
    val contentUri: Uri = FileUtil.getUriForFile(this, apkFile)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
    intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
    return intent
}

