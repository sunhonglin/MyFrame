package com.sunhonglin.base.utils

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun hiddenAndClearFocus(v: View?, ev: MotionEvent?) {
    if (ev?.action == MotionEvent.ACTION_DOWN) {
        if (isShouldHideInput(v, ev)) {
            v?.clearFocus()
            hideSoftInput(v)
        }
    }
}

/**
 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
 *
 * @param v 当前焦点view，currentFocus
 * @param event
 * @return
 */
fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
    if (v != null && v is EditText) {
        val l = intArrayOf(0, 0)
        v.getLocationInWindow(l)
        val left = l[0]
        val top = l[1]
        val bottom = top + v.getHeight()
        val right = (left
                + v.getWidth())
        return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
    }
    return false
}

/**
 * 隐藏键盘
 * @param v 当前焦点view，currentFocus
 */
fun hideSoftInput(v: View?) {
    if (v!!.windowToken != null) {
        val im: InputMethodManager =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(
            v.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}