package com.sunhonglin.myframe

import android.content.Context
import com.sunhonglin.base.BaseApplication
import com.sunhonglin.base.activity.CrashActivity
import com.sunhonglin.core.di.CoreComponent
import com.sunhonglin.core.di.DaggerCoreComponent
import com.tencent.bugly.crashreport.CrashReport

class MyFrameApp : BaseApplication() {

    companion object {
        @JvmStatic
        fun coreComponent(context: Context): CoreComponent =
            (context.applicationContext as MyFrameApp).coreComponent
    }

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        crashActivity(CrashActivity::class.java)
        initBugLy()
    }

    private fun initBugLy() {
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(applicationContext, "c3f5faabd3", false)
        }
    }
}

fun coreComponent() = MyFrameApp.coreComponent(BaseApplication.app)