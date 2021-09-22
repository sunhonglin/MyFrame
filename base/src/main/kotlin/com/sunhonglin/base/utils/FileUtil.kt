package com.sunhonglin.base.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
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

        fun getFilePathByUri(context: Context, uri: Uri): String? {
            return if ("content".equals(uri.scheme, ignoreCase = true)) {
                getRealPathFromUri(
                    context,
                    uri
                )
            } else {
                if ("file".equals(uri.scheme, ignoreCase = true)) uri.path else null
            }
        }

        @SuppressLint("NewApi")
        private fun getRealPathFromUri(context: Context, uri: Uri): String? {
            var filePath: String? = null
            if (DocumentsContract.isDocumentUri(context, uri)) {
                val documentId = DocumentsContract.getDocumentId(uri)
                var type: String
                if (isMediaDocument(uri)) {
                    val id = documentId.split(":").toTypedArray()[1]
                    type = "_id=?"
                    val selectionArgs = arrayOf(id)
                    val contentUri = when (type) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
                    filePath = getDataColumn(
                        context,
                        contentUri,
                        type,
                        selectionArgs
                    )
                } else if (isDownloadsDocument(uri)) {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(documentId)
                    )
                    filePath = getDataColumn(
                        context,
                        contentUri
                    )
                } else if (isExternalStorageDocument(uri)) {
                    type = DocumentsContract.getDocumentId(uri)
                    val split = type.split(":").toTypedArray()
                    type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        filePath =
                            context.getExternalFilesDir(null).toString() + "/" + split[1]
                    }
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                filePath = getDataColumn(
                    context,
                    uri
                )
            } else if ("file" == uri.scheme) {
                filePath = uri.path
            }
            return filePath
        }

        private fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String? = null,
            selectionArgs: Array<String> = arrayOf()
        ): String? {
            uri ?: return null
            var path: String? = null
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                    path = cursor.getString(columnIndex)
                }
            } catch (var8: Exception) {
                cursor?.close()
            }
            return path
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }
    }
}