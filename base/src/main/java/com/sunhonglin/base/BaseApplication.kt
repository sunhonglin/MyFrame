package com.sunhonglin.base

import android.app.Application
import android.content.Intent
import timber.log.Timber

open class BaseApplication : Application() {
    companion object {
        const val EXTRA_STACK_TRACE = "EXTRA_STACK_TRACE"
        lateinit var app : BaseApplication
    }

    var crashActivity: Class<*>? = null

    override fun onCreate() {
        super.onCreate()
        app = this
        initTimber()
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            crashActivity?.let {
                val intent = Intent(this, crashActivity)
                intent.putExtra(EXTRA_STACK_TRACE, e.stackTraceToString())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}