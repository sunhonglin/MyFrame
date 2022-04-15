package com.sunhonglin.base

import android.app.Application
import android.content.Intent
import androidx.annotation.CallSuper
import com.sunhonglin.base.activity.CrashActivity
import com.sunhonglin.base.utils.DataStoreUtil
import timber.log.Timber
import kotlin.system.exitProcess


open class BaseApplication : Application() {
    var crashActivity: Class<*>? = null

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        initTimber()
        DataStoreUtil.init(this)
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            crashActivity?.let {
                val intent = Intent(this, it)
                intent.putExtra(CrashActivity.EXTRA_STACK_TRACE, e.stackTraceToString())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                exitProcess(0)
            }
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}