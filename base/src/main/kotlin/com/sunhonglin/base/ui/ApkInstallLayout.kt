//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.sunhonglin.base.ui

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.sunhonglin.base.R
import com.sunhonglin.base.databinding.LayoutAppUpdateBinding
import com.sunhonglin.base.utils.*
import com.sunhonglin.core.data.api.AppUpdateService
import com.sunhonglin.core.data.appUpdate.model.AppUpdateInfo
import com.sunhonglin.core.data.service.RequestUtil
import com.qmuiteam.qmui.widget.dialog.QMUIBaseDialog
import com.sunhonglin.base.utils.gone
import com.sunhonglin.base.utils.visible
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import java.util.*


class ApkInstallLayout(private val mContext: Context, apk: AppUpdateInfo) :
    View.OnClickListener {
    private val apkUpdateInfo: AppUpdateInfo = apk
    private var isForceUpdate: Boolean = true
    private var isCancel = true
    private var localSavePath: String = ""
    private var autoInstall = false
    private var exitAppListener: ExitAppListener? = null

    private var dialog: QMUIBaseDialog? = null

    var binding: LayoutAppUpdateBinding =
        LayoutAppUpdateBinding.inflate((mContext as Activity).layoutInflater)

    init {
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            mContext.getDisplayWidth(),
            mContext.getDisplayHeight()
        )
        params.gravity = Gravity.CENTER
        binding.root.layoutParams = params

        binding.tvContent.movementMethod = ScrollingMovementMethod.getInstance()

        binding.tvTitle.text =
            mContext.getString(R.string.app_update_fmt_title_update, apkUpdateInfo.version)
        binding.tvContent.text = apkUpdateInfo.description
        binding.tvOk.isEnabled = apkUpdateInfo.downloadUrl.isURL()

        binding.tvOk.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)

        isForceUpdate = apkUpdateInfo.forceUpdate == 0
        if (isForceUpdate) {
            binding.tvCancel.text = mContext.getString(R.string.exit)
        }
        dialog = QMUIBaseDialog(mContext, R.style.AppUpdateDialog)

        dialog?.setOnDismissListener {
            if (isForceUpdate && isCancel && exitAppListener != null) {
                exitAppListener!!.onExitApp()
            }
        }
        dialog?.setCancelable(!isForceUpdate)
        dialog?.setContentView(binding.root)
        if (apkUpdateInfo.downloadUrl.isNotEmpty()) {
            val str = apkUpdateInfo.downloadUrl.split("/")
            localSavePath =
                mContext.getExternalFilesDir("apk").toString() + File.separator + str[str.size - 1]
        }
    }

    fun setLocalSavePath(localSavePath: String): ApkInstallLayout {
        this.localSavePath = localSavePath
        return this
    }

    fun setAutoInstall(autoInstall: Boolean): ApkInstallLayout {
        this.autoInstall = autoInstall
        return this
    }

    fun show() {
        binding.tvOk.setText(R.string.update)
        dialog?.show()
    }

    private fun dismiss() {
        call?.cancel()
        dialog?.dismiss()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> {
                isCancel = true
                dismiss()
            }
            R.id.tv_ok -> {
                when {
                    mContext.getString(R.string.install) == binding.tvOk.text.toString() -> install()
                    else -> {
                        isCancel = false
                        download()
                    }
                }
            }
        }
    }

    private var call: Call<ResponseBody>? = null

    private fun download() {
        when {
            apkUpdateInfo.downloadUrl.isURL() -> {
                val file = File(localSavePath)
                if (file.exists()) {
                    file.delete()
                }
                binding.numberProgressBar.visible()
                call = RequestUtil.builder<AppUpdateService>(isStream = true)
                    .toDownLoadApk(apkUpdateInfo.downloadUrl)
                call?.let {
                    mContext.downloadFile(it, file.absolutePath,
                        object : OnDownloadListener {
                            override fun onLoadStart() {
                                binding.numberProgressBar.visible()
                                binding.tvOk.isEnabled = false
                            }

                            override fun onDownloadSuccess(filePath: String) {
                                binding.numberProgressBar.gone()
                                binding.tvOk.isEnabled = true
                                binding.tvOk.setText(R.string.install)
                                if (autoInstall) {
                                    install()
                                }
                            }

                            override fun onDownloading(progress: Int) {
                                binding.numberProgressBar.progress = progress
                                binding.tvOk.text =
                                    mContext.getString(R.string.app_update_fmt_progress, progress)
                            }

                            override fun onDownloadFailed() {
                                binding.numberProgressBar.gone()
                                mContext.showToastInfo(
                                    resId = R.string.app_update_error_download
                                )
                                binding.tvOk.setText(R.string.update)
                                binding.tvOk.isEnabled = true
                            }
                        })
                }
            }
        }
    }

    private fun install() {
        val file = File(localSavePath)
        if (file.exists()) {
            val result: String = EncryptUtil.encryptMD5File2String(file.absolutePath)
            if (apkUpdateInfo.md5.isNotEmpty() && !TextUtils.equals(
                    result.uppercase(Locale.getDefault()),
                    apkUpdateInfo.md5.uppercase(Locale.getDefault())
                )
            ) {
                mContext.showToastInfo(resId = R.string.app_update_error_install)
            } else {
                AppUtil.installApp(mContext, file.absolutePath)
            }
        }
    }

    fun setExitAppListener(exitAppListener: ExitAppListener?) {
        this.exitAppListener = exitAppListener
    }

    interface ExitAppListener {
        fun onExitApp()
    }
}