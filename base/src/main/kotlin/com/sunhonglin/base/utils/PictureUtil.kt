package com.sunhonglin.base.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.provider.MediaStore
import java.io.File

class PictureUtil {

    companion object {
        private fun getOpenCameraIntent(): Intent {
            return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        }

        fun getImagePickerIntent(): Intent {
            val intent = Intent(Intent.ACTION_PICK, null)
            return intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }

        fun takePicture(context: Context, filePath: String, requestCode: Int) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    val values = ContentValues(1)
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                    values.put(MediaStore.Images.Media.DATA, filePath)

                    val mCameraTempUri: Uri? = context.contentResolver
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    val intent = getOpenCameraIntent()
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri)
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
                    (context as Activity).startActivityForResult(intent, requestCode)
                } catch (var6: Exception) {
                    var6.printStackTrace()
                }
            } else {
                val openCameraIntent = getOpenCameraIntent()
                val mUri = Uri.fromFile(File(filePath))
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
                (context as Activity).startActivityForResult(openCameraIntent, requestCode)
            }
        }
    }
}