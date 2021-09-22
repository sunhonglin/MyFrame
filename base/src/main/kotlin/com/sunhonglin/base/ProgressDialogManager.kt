package com.sunhonglin.base

import androidx.fragment.app.FragmentActivity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

interface ProgressDialogManager {
    fun showProgressDialog()
    fun dismissProgressDialog()
    fun isProgressDialogShowing(): Boolean
}

class DefaultProgressDialogManager(
    private val activity: FragmentActivity
) : ProgressDialogManager {
    private val delegate: QMUITipDialog by lazy {
        val progressDialog = QMUITipDialog.Builder(activity)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(activity.resources.getString(R.string.loading))
            .create()
        progressDialog
    }

    override fun showProgressDialog() {
        if (!delegate.isShowing) {
            delegate.show()
        }
    }

    override fun dismissProgressDialog() {
        delegate.dismiss()
    }

    override fun isProgressDialogShowing(): Boolean {
        return delegate.isShowing
    }
}