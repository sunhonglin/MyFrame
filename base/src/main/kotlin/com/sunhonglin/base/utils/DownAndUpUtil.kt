package com.sunhonglin.base.utils

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

interface OnDownloadListener {
    /**
     * 开始
     */
    fun onLoadStart()

    /**
     * 成功
     * @param filePath 文件路径
     */
    fun onDownloadSuccess(filePath: String)

    /**
     * @param progress 进度
     */
    fun onDownloading(progress: Int)

    /**
     * 失败
     */
    fun onDownloadFailed()
}

/**
 * 下载文件
 *
 * @param filePath 保存的文件路径，包含文件名
 */
fun Context.downloadFile(
    call: Call<ResponseBody>,
    filePath: String,
    listener: OnDownloadListener? = null
) {
    val file = File(filePath)
    listener?.onLoadStart()
    call.enqueue(object : Callback<ResponseBody> {
        override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
            postMainThread {
                listener?.onDownloadFailed()
            }
        }

        override fun onResponse(p0: Call<ResponseBody>, response: Response<ResponseBody>) {
            when (response.code()) {
                200 -> {
                    var fileOutputStream: FileOutputStream? = null
                    var inputStream: InputStream? = null
                    try {
                        val total = response.body()!!.contentLength()
                        var sum: Long = 0
                        inputStream = response.body()!!.byteStream()
                        fileOutputStream = FileOutputStream(file)
                        val buffer = ByteArray(1024 * 1024)
                        var len: Int
                        while (inputStream.read(buffer).also { len = it } != -1) {
                            fileOutputStream.write(buffer, 0, len)
                            listener?.let {
                                sum += len.toLong()
                                val progress = (sum * 100 / total).toInt()
                                // 下载中
                                postMainThread {
                                    it.onDownloading(progress)
                                }
                            }
                        }
                        fileOutputStream.flush()
                        postMainThread {
                            listener?.onDownloadSuccess(filePath)
                        }
                    } catch (e: IOException) {
                        postMainThread {
                            listener?.onDownloadFailed()
                        }
                    } finally {
                        inputStream?.close()
                        fileOutputStream?.close()
                    }
                }
                else -> {
                    postMainThread {
                        listener?.onDownloadFailed()
                    }
                }
            }
        }
    })
}