package com.sunhonglin.base.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunhonglin.base.DefaultProgressDialogManager
import com.sunhonglin.base.ProgressDialogManager
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.UiThreadExecutor
import com.sunhonglin.base.interfaces.BindEventBus
import com.sunhonglin.base.utils.EventBusUtil

abstract class BaseActivity : AppCompatActivity(), UiThreadExecutor, ProgressDialogManager {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        when {
            this.javaClass.isAnnotationPresent(BindEventBus::class.java) ->
                EventBusUtil.register(this)
        }

        super.onCreate(savedInstanceState)
        mContext = this
    }

    fun setStatusBarMode(mode: StatusBarMode) = mode.configure(this)

    private val progressDialogManager: ProgressDialogManager by lazy {
        DefaultProgressDialogManager(this)
    }

    override fun showProgressDialog(tipMessage: String?) =
        progressDialogManager.showProgressDialog()

    override fun dismissProgressDialog() = progressDialogManager.dismissProgressDialog()

    override fun isProgressDialogShowing(): Boolean =
        progressDialogManager.isProgressDialogShowing()

    override fun onDestroy() {
        when {
            this.javaClass.isAnnotationPresent(BindEventBus::class.java) -> EventBusUtil.unregister(
                this
            )
        }
        dismissProgressDialog()
        super.onDestroy()
    }
}