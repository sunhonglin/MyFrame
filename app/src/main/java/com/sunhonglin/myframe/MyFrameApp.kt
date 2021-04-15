package com.sunhonglin.myframe

import android.content.Context
import com.sunhonglin.base.BaseApplication
import com.sunhonglin.base.activity.CrashActivity
import com.sunhonglin.core.di.CoreComponent
import com.sunhonglin.core.di.DaggerCoreComponent

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
    }
}

fun coreComponent() = MyFrameApp.coreComponent(BaseApplication.app)