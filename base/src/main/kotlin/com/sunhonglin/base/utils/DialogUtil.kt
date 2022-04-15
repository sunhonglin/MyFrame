package com.sunhonglin.base.utils

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.Gravity
import androidx.activity.ComponentActivity
import com.sunhonglin.base.adapter.BaseRcvAdapter
import com.sunhonglin.base.databinding.LayoutBottomSheetBinding
import com.sunhonglin.base.databinding.LayoutDialogInputBinding
import com.sunhonglin.base.databinding.LayoutTipDialogBinding
import com.sunhonglin.base.ui.CustomDialog
import com.sunhonglin.core.util.setDebounceOnClickListener

const val CANCEL = "取消"
const val TITLE = "提示"
const val SURE = "确定"
const val START = "开始"
const val CLOSE = "关闭"
const val QUIT = "退出"
const val RETRY = "再试一次"
const val GO_SETTING = "去设置"
const val YES = "是"
const val NO = "否"
const val REFRESH = "刷新"

/**
 * 消息类型对话框
 */
fun ComponentActivity.showConfirmDialog(
    message: String?,
    title: String = TITLE,
    cancelText: String = CANCEL,
    sureText: String = SURE,
    onlyShowOk: Boolean = false,
    cancelable: Boolean = true,
    unit: ((index: Int) -> Unit)? = null
) {
    val binding = LayoutTipDialogBinding.inflate(layoutInflater)
    binding.tvTitle.text = title
    binding.tvContent.text = message
    binding.tvCancel.text = cancelText
    binding.tvOk.text = sureText

    val dialog = CustomDialog(this)
    dialog.setCancelable(cancelable)
    dialog.setView(binding.root)
    if (onlyShowOk) binding.tvCancel.gone()
    binding.tvCancel.setDebounceOnClickListener {
        dialog.dismiss()
        unit?.let { it1 -> it1(0) }
    }
    binding.tvOk.setDebounceOnClickListener {
        dialog.dismiss()
        unit?.let { it1 -> it1(1) }
    }
    dialog.show()
}

/**
 * 带输入框的对话框
 * 不可取消，光标定位到最后，调用者控制隐藏
 */
@Suppress("DEPRECATION")
fun ComponentActivity.showInputDialog(
    defaultText: String = "",
    placeholder: String = "",
    maxLength: Int = 16,
    inputType: Int = InputType.TYPE_CLASS_TEXT,
    title: String = TITLE,
    cancelable: Boolean = true,
    outSideCancelable: Boolean = true,
    unit: (text: String) -> Unit
) {
    val binding = LayoutDialogInputBinding.inflate(layoutInflater)
    binding.tvTitle.text = title
    binding.etContent.apply {
        hint = placeholder
        filters = arrayOf<InputFilter>(LengthFilter(maxLength))
        setText(defaultText)
        setInputType(inputType)
    }

    val dialog = CustomDialog(this)
    dialog.setCancelable(cancelable)
    dialog.setView(binding.root)
    if (outSideCancelable) {
        binding.root.setDebounceOnClickListener { dialog.dismiss() }
    } else {
        binding.root.setDebounceOnClickListener {}
    }
    binding.tvCancel.setDebounceOnClickListener {
        dialog.dismiss()
    }
    binding.tvOk.setDebounceOnClickListener {
        dialog.dismiss()
        unit(binding.etContent.text.toString())
    }
    dialog.show()
}

/**
 * 菜单类型的对话框
 * 默认底部
 */
fun <T> ComponentActivity.showSelectorDialog(
    adapter: BaseRcvAdapter<T>,
    title: String? = TITLE,
    onlyShowCancel: Boolean = false,
    cancelable: Boolean = true,
    outSideCancelable: Boolean = true,
    gravity: Int = Gravity.BOTTOM
): CustomDialog {
    val dialog = CustomDialog(this).apply {
        setCancelable(cancelable)
        setOutSideCancelable(outSideCancelable)
    }

    val binding = LayoutBottomSheetBinding.inflate(layoutInflater).apply {
        tvTitle.text = title
        rcv.adapter = adapter
        if (onlyShowCancel) {
            tvOk.gone()
            vLine.gone()
        }
        if (outSideCancelable) {
            root.setDebounceOnClickListener { dialog.dismiss() }
        } else {
            root.setDebounceOnClickListener {}
        }
        tvCancel.setDebounceOnClickListener {
            dialog.dismiss()
        }
        tvOk.setDebounceOnClickListener {
            dialog.dismiss()
        }
    }

    return dialog.apply {
        setView(binding.root, 0, gravity)
        show()
    }
}