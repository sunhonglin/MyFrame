package com.sunhonglin.base.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import java.lang.reflect.Field
import java.lang.reflect.Method

@SuppressLint("PrivateApi")
internal object StatusBarHelper {
    private enum class StatusBarType {
        Default, Miui, Flyme, Android6
    }

    private var mStatusBarType: StatusBarType = StatusBarType.Default

    /**
     * 设置状态栏黑色字体图标，
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     */
    fun setStatusBarLightMode(activity: Activity): Boolean {
        if (mStatusBarType != StatusBarType.Default) {
            return setStatusBarLightMode(activity, mStatusBarType)
        }
        if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(
                activity.window,
                true
            )
        ) {
            mStatusBarType = StatusBarType.Miui
            return true
        } else if (FlymeSetStatusBarLightMode(activity.window, true)) {
            mStatusBarType = StatusBarType.Flyme
            return true
        }
            
        Android6SetStatusBarLightMode(activity.window, true)
        mStatusBarType = StatusBarType.Android6
        return true
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     * @param type     StatusBar 类型，对应不同的系统
     */
    private fun setStatusBarLightMode(activity: Activity, type: StatusBarType): Boolean {
        return when (type) {
            StatusBarType.Miui -> MIUISetStatusBarLightMode(activity.window, true)
            StatusBarType.Flyme -> FlymeSetStatusBarLightMode(activity.window, true)
            StatusBarType.Android6 -> Android6SetStatusBarLightMode(activity.window, true)
            else -> false
        }
    }

    /**
     * 设置状态栏白色字体图标
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     */
    fun setStatusBarDarkMode(activity: Activity?): Boolean {
        if (activity == null) return false
        if (mStatusBarType === StatusBarType.Default) {
            // 默认状态，不需要处理
            return true
        }
        return when (mStatusBarType) {
            StatusBarType.Miui -> MIUISetStatusBarLightMode(activity.window, false)
            StatusBarType.Flyme -> FlymeSetStatusBarLightMode(activity.window, false)
            StatusBarType.Android6 -> Android6SetStatusBarLightMode(activity.window, false)
            else -> true
        }
    }

    fun translucent(activity: Activity) {
        translucent(activity.window)
    }

    private fun translucent(window: Window) {
        translucent(window, 0x40000000)
    }

    private fun supportTranslucent(): Boolean {
        // Essential Phone 在 Android 8 之前沉浸式做得不全，系统不从状态栏顶部开始布局却会下发 WindowInsets
        return !(DeviceUtil.isEssentialPhone() && Build.VERSION.SDK_INT < 26)
    }

    /**
     * 沉浸式状态栏。
     * 支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android。
     *
     * @param activity 需要被设置沉浸式状态栏的 Activity。
     */
    fun translucent(activity: Activity, @ColorInt colorOn5x: Int) {
        val window = activity.window
        translucent(window, colorOn5x)
    }

    @TargetApi(19)
    fun translucent(window: Window, @ColorInt colorOn5x: Int) {
        if (!supportTranslucent()) {
            // 版本小于4.4，绝对不考虑沉浸式
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handleDisplayCutoutMode(window)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 小米 Android 6.0 ，开发版 7.7.13 及以后版本设置黑色字体又需要 clear FLAG_TRANSLUCENT_STATUS, 因此还原为官方模式
            if (DeviceUtil.isFlymeLowerThan(8)) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
                return
            }
        }
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility =
            systemUiVisibility or (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.decorView.systemUiVisibility = systemUiVisibility
        // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    @TargetApi(28)
    private fun handleDisplayCutoutMode(window: Window) {
        val decorView = window.decorView
        if (ViewCompat.isAttachedToWindow(decorView)) {
            realHandleDisplayCutoutMode(window, decorView)
        } else {
            decorView.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    realHandleDisplayCutoutMode(window, v)
                }

                override fun onViewDetachedFromWindow(v: View) {}
            })
        }
    }

    @TargetApi(28)
    private fun realHandleDisplayCutoutMode(window: Window, decorView: View) {
        if (decorView.rootWindowInsets != null &&
            decorView.rootWindowInsets.displayCutout != null
        ) {
            val params = window.attributes
            params.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }

    /**
     * 设置状态栏字体图标为深色，需要 MIUIV6 以上
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回 true
     */

    private fun MIUISetStatusBarLightMode(window: Window?, light: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field: Field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField: Method = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (light) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
            } catch (ignored: Exception) {
            }
        }
        return result
    }

    /**
     * 设置状态栏字体图标为深色，Android 6
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @TargetApi(23)
    private fun Android6SetStatusBarLightMode(window: Window, light: Boolean): Boolean {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            if (insetsController != null) {
                insetsController.isAppearanceLightStatusBars = light
            }
        } else {
            // 经过测试，小米 Android 11 用  WindowInsetsControllerCompat 不起作用， 我还能说什么呢。。。
            val decorView: View = window.decorView
            var systemUi: Int = decorView.systemUiVisibility
            systemUi = if (light) {
                systemUi or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUi and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = systemUi
        }
        if (DeviceUtil.isMiuiVersion(9)) {
            // MIUI 9 低于 6.0 版本依旧只能回退到以前的方案
            // https://github.com/Tencent/QMUI_Android/issues/160
            MIUISetStatusBarLightMode(window, light)
        }
        return true
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为 Flyme 用户
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private fun FlymeSetStatusBarLightMode(window: Window?, light: Boolean): Boolean {
        var result = false
        if (window != null) {
            Android6SetStatusBarLightMode(window, light)

            // flyme 在 6.2.0.0A 支持了 Android 官方的实现方案，旧的方案失效
            // 高版本调用这个出现不可预期的 Bug,官方文档也没有给出完整的高低版本兼容方案
            if (DeviceUtil.isFlymeLowerThan(7)) {
                try {
                    val lp = window.attributes
                    val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                    val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                    darkFlag.isAccessible = true
                    meizuFlags.isAccessible = true
                    val bit = darkFlag.getInt(null)
                    var value = meizuFlags.getInt(lp)
                    value = if (light) {
                        value or bit
                    } else {
                        value and bit.inv()
                    }
                    meizuFlags.setInt(lp, value)
                    window.attributes = lp
                    result = true
                } catch (ignored: java.lang.Exception) {
                }
            } else if (DeviceUtil.isFlyme()) {
                result = true
            }
        }
        return result
    }

    /**
     * 更改状态栏图标、文字颜色的方案是否是MIUI自家的， MIUI9 && Android 6 之后用回Android原生实现
     * 见小米开发文档说明：https://dev.mi.com/console/doc/detail?pId=1159
     */
    private fun isMIUICustomStatusBarLightModeImpl(): Boolean {
        return DeviceUtil.isMiuiVersion(5) || DeviceUtil.isMiuiVersion(6) ||
                DeviceUtil.isMiuiVersion(7) || DeviceUtil.isMiuiVersion(8)
    }
}