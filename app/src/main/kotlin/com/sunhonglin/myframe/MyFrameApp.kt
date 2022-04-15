package com.sunhonglin.myframe

import com.sunhonglin.base.BaseApplication
import com.sunhonglin.base.activity.CrashActivity
import com.sunhonglin.myframe.data.login.model.LoginData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyFrameApp : BaseApplication() {
    lateinit var loginData: LoginData

    companion object {
        lateinit var app: MyFrameApp
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        crashActivity = CrashActivity::class.java
        initBugLy()
    }

    private fun initBugLy() {
//        if (!BuildConfig.DEBUG) {
////            CrashReport.initCrashReport(applicationContext, "525a45b330", false)
//        }
    }
}