package com.sunhonglin.base

import android.app.Activity
import com.sunhonglin.base.utils.StatusBarHelper

enum class StatusBarMode {

    LIGHT {
        override fun configure(activity: Activity) {
            StatusBarHelper.setStatusBarLightMode(activity)
        }
    },

    DARK {
        override fun configure(activity: Activity) {
            StatusBarHelper.setStatusBarDarkMode(activity)
        }
    },

    TRANSLUCENT {
        override fun configure(activity: Activity) {
            StatusBarHelper.translucent(activity)
        }
    };

    abstract fun configure(activity: Activity)
}
