package com.sunhonglin.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.StringRes
import com.sunhonglin.base.databinding.LayoutToastBinding
import com.sunhonglin.base.utils.ToastUtil.Companion.DURATION_DEFAULT
import com.sunhonglin.base.utils.ToastUtil.Companion.toast

@SuppressLint("ShowToast", "WrongConstant")
fun Context.showToast(
    text: String? = null,
    @StringRes resId: Int? = null,
    duration: Int = DURATION_DEFAULT
) {
    toast?.cancel()
    toast = Toast.makeText(this, text, duration)
    toast?.setGravity(Gravity.CENTER, 0, 0)
    val binding = LayoutToastBinding.inflate(LayoutInflater.from(this))
    var message = text
    resId?.let {
        message = getString(resId)
    }

    binding.tvInfo.text = message
    toast?.view = binding.root
    toast?.show()
}


class ToastUtil {
    companion object {
        const val DURATION_DEFAULT = Toast.LENGTH_SHORT
        var toast: Toast? = null
    }
}