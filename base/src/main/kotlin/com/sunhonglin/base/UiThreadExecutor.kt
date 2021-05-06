package com.sunhonglin.base

interface UiThreadExecutor {
    fun runOnUiThread(action: Runnable)
}