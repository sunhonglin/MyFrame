package com.sunhonglin.core.util

import android.os.Handler
import android.os.Looper
import android.view.View

private const val enableTimeDefault = 500L
fun View.setDebounceOnClickListener(enableT: Long = enableTimeDefault, listener: (View) -> Unit) {
    setOnClickListener(object : DebounceOnClickListener(enableT) {
        override fun doClick(v: View) {
            listener(v)
        }
    })
}


abstract class DebounceOnClickListener(var enableT: Long) : View.OnClickListener {
    private var enable = true
    private val enableRunnable = Runnable { enable = true }
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onClick(v: View) {
        if (enable) {
            enable = false
            doClick(v)
            mainHandler.postDelayed(enableRunnable, enableT)
        }
    }

    abstract fun doClick(v: View)
}