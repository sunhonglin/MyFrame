package com.sunhonglin.base.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class FileUtil {
    companion object {

        fun getUriForFile(mContext: Context, file: File): Uri {
            return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                true -> FileProvider.getUriForFile(
                    mContext,
                    mContext.packageName + ".fileProvider",
                    file
                )
                else -> Uri.fromFile(file)
            }
        }
    }
}