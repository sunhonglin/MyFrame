package com.sunhonglin.core.data.service

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

class ProgressRequestBody(
    private var requestBody: RequestBody? = null,
    var callback: ProgressCallback? = null
) : RequestBody() {

    private var previousTime: Long = 0
    private lateinit var countingSink: CountingSink

    private var isSecond = false

    override fun contentType(): MediaType? {
        return this.requestBody?.contentType()
    }

    override fun contentLength(): Long {
        requestBody?.let {
            return it.contentLength()
        }
        return -1L
    }

    override fun writeTo(sink: BufferedSink) {
        previousTime = System.currentTimeMillis()
        countingSink = CountingSink(sink)
        val bufferedSink: BufferedSink = countingSink.buffer()
        this.requestBody?.writeTo(bufferedSink)
        bufferedSink.flush()
        isSecond = true
    }

    inner class CountingSink(
        delegate: Sink
    ) : ForwardingSink(delegate) {
        private var bytesWritten = 0L
        var contentLength = 0L

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            if (isSecond) {
                if (contentLength == 0L) {
                    contentLength = this@ProgressRequestBody.contentLength()
                }
                bytesWritten += byteCount
                if (this@ProgressRequestBody.callback != null) {
                    var totalTime: Long =
                        (System.currentTimeMillis() - this@ProgressRequestBody.previousTime) / 1000L
                    if (totalTime == 0L) {
                        ++totalTime
                    }
                    val networkSpeed = bytesWritten / totalTime
                    val progress = (bytesWritten * 100L / contentLength).toInt()
                    val done = bytesWritten == contentLength
                    this@ProgressRequestBody.callback?.onProgress(progress, networkSpeed, done)
                }
            }
        }
    }
}