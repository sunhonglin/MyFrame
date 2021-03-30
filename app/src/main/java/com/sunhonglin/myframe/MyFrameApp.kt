package com.sunhonglin.myframe

import com.sunhonglin.base.BaseApplication
import com.sunhonglin.base.activity.CrashActivity
import timber.log.Timber

class MyFrameApp : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        crashActivity = CrashActivity::class.java
    }


}