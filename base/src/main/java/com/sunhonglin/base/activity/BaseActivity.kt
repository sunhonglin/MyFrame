package com.sunhonglin.base.activity

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunhonglin.base.*

abstract class BaseActivity : AppCompatActivity(), UiThreadExecutor, ProgressDialogManager {

    lateinit var mContext: Context
    lateinit var app: Application

    override fun onCreate(savedInstanceState: Bundle?) {
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
}