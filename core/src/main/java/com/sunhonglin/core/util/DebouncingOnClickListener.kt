package com.sunhonglin.core.util

import android.os.Handler
import android.os.Looper
import android.view.View

fun View.setDebounceOnClickListener(listener: (View) -> Unit) {
    setOnClickListener(object : DebounceOnClickListener() {
        override fun doClick(v: View) {
            listener(v)
        }
    })
}

abstract class DebounceOnClickListener : View.OnClickListener {
    companion object {
        private var enableTime = 50L
        private var enable = true
        private val ENABLE_AGAIN = Runnable { enable = true }
        private val MAIN = Handler(Looper.getMainLooper())
    }

    override fun onClick(v: View) {
        if (enable) {
            enable = false
            MAIN.postDelayed(ENABLE_AGAIN, enableTime)
            doClick(v)
        }
    }

    abstract fun doClick(v : View)
}