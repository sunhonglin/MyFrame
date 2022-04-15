package com.sunhonglin.base

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.sunhonglin.base.databinding.LayoutLoadingBinding
import com.sunhonglin.base.ui.CustomDialog

interface ProgressDialogManager {
    fun showProgressDialog(tipMessage: String? = null)
    fun dismissProgressDialog()
    fun isProgressDialogShowing(): Boolean
}

class DefaultProgressDialogManager(
    private val activity: FragmentActivity
) : ProgressDialogManager {

    var binding: LayoutLoadingBinding? = null

    private val delegate: CustomDialog by lazy {
        val dialog = CustomDialog(activity).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(false)
        }

        binding = LayoutLoadingBinding.inflate(dialog.layoutInflater)
        binding?.let {
            dialog.setView(it.root, 2, Gravity.CENTER, false)
        }
        dialog
    }

    override fun showProgressDialog(tipMessage: String?) {
        binding?.let {
            it.tvLoadText.text = tipMessage ?: activity.getText(R.string.loading)
        }
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