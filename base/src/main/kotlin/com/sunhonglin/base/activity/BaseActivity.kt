package com.sunhonglin.base.activity

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.sunhonglin.base.*
import com.sunhonglin.base.interfaces.BindEventBus
import com.sunhonglin.base.interfaces.BindSkinManager
import com.sunhonglin.base.utils.EventBusUtil
import com.qmuiteam.qmui.skin.QMUISkinLayoutInflaterFactory
import com.qmuiteam.qmui.skin.QMUISkinManager

abstract class BaseActivity : AppCompatActivity(), UiThreadExecutor, ProgressDialogManager {
    companion object {
        const val SKIN_COLD = 1
        const val SKIN_HEAT = 2
    }

    lateinit var mContext: Context
    lateinit var app: Application
    private var mSkinManager: QMUISkinManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        when {
            this.javaClass.isAnnotationPresent(BindEventBus::class.java) -> EventBusUtil.register(
                this
            )
            this.javaClass.isAnnotationPresent(BindSkinManager::class.java) -> {
                LayoutInflaterCompat.setFactory2(
                    layoutInflater,
                    QMUISkinLayoutInflaterFactory(this, LayoutInflater.from(this))
                )
                // 注入 QMUISkinManager
                mSkinManager = QMUISkinManager.defaultInstance(this)
                mSkinManager?.apply {
                    addSkin(SKIN_COLD, R.style.AppThemeNoActionBarCold)
                    addSkin(SKIN_HEAT, R.style.AppThemeNoActionBarHeat)
                }
            }
        }

        super.onCreate(savedInstanceState)
        mContext = this
        app = BaseApplication.app
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