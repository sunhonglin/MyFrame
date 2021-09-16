package com.sunhonglin.core.data.service

interface ProgressCallback {
    /**
     * 进度
     * 非主线程
     */
    fun onProgress(progress: Int, networkSpeed: Long, done: Boolean)
}