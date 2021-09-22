package com.sunhonglin.base.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * 获取屏幕参数
 *
 * @return 屏幕参数
 */
fun Context.getDisplayMetrics(): DisplayMetrics {
    return resources.displayMetrics
}

/**
 * 获取像素密度
 *
 * @return 像素密度
 */
fun Context.getDisplayDensity(): Float {
    return getDisplayMetrics().density
}

/**
 * dp转px
 *
 * @param dpValue dp值
 * @return px值
 */
fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * getDisplayDensity() + 0.5f).toInt()
}

/**
 * px转dp
 *
 * @param pxValue px值
 * @return dp值
 */
fun Context.px2dp(pxValue: Float): Int {
    return (pxValue / getDisplayDensity() + 0.5f).toInt()
}

/**
 * sp转px
 *
 * @param spValue sp值
 * @return px值
 */
fun Context.sp2px(spValue: Float): Int {
    return (spValue * getDisplayMetrics().scaledDensity + 0.5f).toInt()
}

/**
 * px转sp
 *
 * @param pxValue px值
 * @return sp值
 */
fun Context.px2sp(pxValue: Float): Int {
    return (pxValue / getDisplayMetrics().scaledDensity + 0.5f).toInt()
}

/**
 * 获取屏幕宽度
 *
 * @return 屏幕宽度
 */
fun Context.getDisplayWidth(): Int {
    return getDisplayMetrics().widthPixels
}

/**
 * 获取屏幕高度
 *
 * @return 屏幕高度
 */
fun Context.getDisplayHeight(): Int {
    return getDisplayMetrics().heightPixels
}