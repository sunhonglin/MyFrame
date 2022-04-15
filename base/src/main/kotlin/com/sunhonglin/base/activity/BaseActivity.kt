package com.sunhonglin.base.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunhonglin.base.*
import com.sunhonglin.base.theme.ThemeManager
import com.sunhonglin.base.interfaces.BindEventBus
import com.sunhonglin.base.interfaces.BindSkinManager
import com.sunhonglin.base.utils.EventBusUtil
import com.qmuiteam.qmui.skin.QMUISkinManager

abstract class BaseActivity : AppCompatActivity(), UiThreadExecutor, ProgressDialogManager {
    lateinit var mContext: Context
    private var mSkinManager: QMUISkinManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        when {
            this.javaClass.isAnnotationPresent(BindEventBus::class.java) ->
                EventBusUtil.register(this)
        }
        when {
            this.javaClass.isAnnotationPresent(BindSkinManager::class.java) ->
                mSkinManager = ThemeManager.init(this)
        }

        super.onCreate(savedInstanceState)
        configureTheme()
        mContext = this
    }

    private fun configureTheme() {
        when (ThemeManager.getStyle()) {
            ThemeManager.SKIN_HEAT -> setTheme(R.style.AppThemeNoActionBarHeat)
            else -> setTheme(R.style.AppThemeNoActionBarCold)
        }
    }

    fun setStatusBarMode(mode: StatusBarMode) = mode.configure(this)

    private val progressDialogManager: ProgressDialogManager by lazy {
        DefaultProgressDialogManager(this)
    }

    override fun showProgressDialog() = progressDialogManager.showProgressDialog()

    override fun dismissProgressDialog() = progressDialogManager.dismissProgressDialog()

    override fun isProgressDialogShowing(): Boolean =
        progressDialogManager.isProgressDialogShowing()

    override fun onStart() {
        super.onStart()
        mSkinManager?.register(this)
    }

    override fun onDestroy() {
        when {
            this.javaClass.isAnnotationPresent(BindEventBus::class.java) -> EventBusUtil.unregister(
                this
            )
            this.javaClass.isAnnotationPresent(BindSkinManager::class.java) -> mSkinManager?.unRegister(
                this
            )
        }
        dismissProgressDialog()
        super.onDestroy()
    }
}