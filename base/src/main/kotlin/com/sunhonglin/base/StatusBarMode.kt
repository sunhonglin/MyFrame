package com.sunhonglin.base

import android.app.Activity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

enum class StatusBarMode {

    LIGHT {
        override fun configure(activity: Activity) {
            QMUIStatusBarHelper.setStatusBarLightMode(activity)
        }
    },

    DARK {
        override fun configure(activity: Activity) {
            QMUIStatusBarHelper.setStatusBarDarkMode(activity)
        }
    },

    TRANSLUCENT {
        override fun configure(activity: Activity) {
            QMUIStatusBarHelper.translucent(activity)
        }
    };

    abstract fun configure(activity: Activity)
}
