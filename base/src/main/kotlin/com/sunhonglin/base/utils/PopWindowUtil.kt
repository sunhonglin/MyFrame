package com.sunhonglin.base.utils

import android.view.View
import androidx.activity.ComponentActivity
import com.sunhonglin.base.ui.CustomPopup

fun ComponentActivity.showPopWindow(
    v: View,
    contentView: View,
    xOffDp: Int = 0,
    yOffDp: Int = 0,
    backgroundAlpha: Float = 0.5f
): CustomPopup {
    val popUp = CustomPopup(this, contentView)
    popUp.backgroundAlpha(backgroundAlpha)
    popUp.show(v, xOffDp, yOffDp)
    return popUp
}