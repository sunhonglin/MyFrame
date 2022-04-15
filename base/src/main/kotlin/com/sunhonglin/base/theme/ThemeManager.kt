package com.sunhonglin.base.theme

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.sunhonglin.base.R
import com.sunhonglin.base.activity.BaseActivity
import com.sunhonglin.base.utils.DataStoreUtil
import com.qmuiteam.qmui.skin.QMUISkinLayoutInflaterFactory
import com.qmuiteam.qmui.skin.QMUISkinManager


object ThemeManager {
    const val SKIN_COLD = 1
    const val SKIN_HEAT = 2

    private const val THEME = "theme"

    fun saveStyle(style: Int) = DataStoreUtil.putSyncData(THEME, style)

    fun getStyle() = DataStoreUtil.readIntData(THEME, SKIN_COLD)

    fun removeStyle() = DataStoreUtil.removeIntData(THEME)

    fun init(activity: BaseActivity): QMUISkinManager {
        LayoutInflaterCompat.setFactory2(
            activity.layoutInflater,
            QMUISkinLayoutInflaterFactory(activity, LayoutInflater.from(activity))
        )

        val mSkinManager = QMUISkinManager.defaultInstance(activity)

        mSkinManager?.apply {
            addSkin(SKIN_COLD, R.style.AppThemeNoActionBarCold)
            addSkin(SKIN_HEAT, R.style.AppThemeNoActionBarHeat)
        }

        QMUISkinManager.setRuleHandler(
            AppSkinRuleTintListHandler.NAME,
            AppSkinRuleTintListHandler()
        )

        return mSkinManager
    }
}

fun ComponentActivity.changeTheme(theme: Int) {
    QMUISkinManager.defaultInstance(this).changeSkin(theme)
}

fun Fragment.addSkinChangeListener(onSkinChangeListener: QMUISkinManager.OnSkinChangeListener) {
    this.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onResume() {
            QMUISkinManager.defaultInstance(activity).addSkinChangeListener(onSkinChangeListener)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            QMUISkinManager.defaultInstance(activity).removeSkinChangeListener(onSkinChangeListener)
        }
    })
}

