package com.sunhonglin.base

import android.app.Application
import android.content.Intent
import com.sunhonglin.base.activity.CrashActivity
import timber.log.Timber

open class BaseApplication : Application() {
    companion object {
        lateinit var app: BaseApplication
    }

    private var crashActivity: Class<*>? = null

    override fun onCreate() {
        super.onCreate()
        app = this
        initTimber()
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            crashActivity?.let {
                val intent = Intent(this, crashActivity)
                intent.putExtra(CrashActivity.EXTRA_STACK_TRACE, e.stackTraceToString())
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

    fun crashActivity(activity: Class<*>?) {
        crashActivity = activity
    }
}