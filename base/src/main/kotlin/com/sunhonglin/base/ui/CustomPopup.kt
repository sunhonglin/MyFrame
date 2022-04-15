package com.sunhonglin.base.ui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.sunhonglin.base.utils.dp2px
import com.qmuiteam.qmui.skin.QMUISkinManager

@SuppressLint("ClickableViewAccessibility")
class CustomPopup(
    val activity: ComponentActivity,
    val view: View
) : PopupWindow(
    view,
    ViewGroup.LayoutParams.WRAP_CONTENT,
    ViewGroup.LayoutParams.WRAP_CONTENT,
    true
) {

    init {
//        popupWindow.isTouchable = true
//        popupWindow.isOutsideTouchable = true

        QMUISkinManager.defaultInstance(activity).register(this)
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                QMUISkinManager.defaultInstance(activity).unRegister(this@CustomPopup)
                dismiss()
            }
        })

        setOnDismissListener {
            backgroundAlpha(1f)
            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    fun backgroundAlpha(f: Float = 0.5f) {
        val attributesNew = activity.window?.attributes
        attributesNew?.alpha = f
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        activity.window?.attributes = attributesNew
    }

    fun show(v: View, xOffDp: Int, yOffDp: Int) {
        showAsDropDown(
            v, activity.dp2px(xOffDp.toFloat()), activity.dp2px(
                yOffDp.toFloat()
            )
        )
    }
}