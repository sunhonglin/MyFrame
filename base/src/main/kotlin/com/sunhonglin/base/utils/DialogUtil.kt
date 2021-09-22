package com.sunhonglin.base.utils

import android.content.Context
import android.content.DialogInterface
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.*

const val CANCEL = "取消"
const val TITLE = "提示"
const val SURE = "确定"
const val START = "开始"
const val CLOSE = "关闭"
const val QUIT = "退出"
const val RETRY = "再试一次"
const val GO_SETTING = "去设置"

interface DialogCallback {
    fun onItemAction(dialog: DialogInterface, which: Int)
    fun onButtonAction(dialog: DialogInterface, index: Int, result: String?)
}

/**
 * 消息类型对话框
 */
fun Context.showConfirmDialog(
    title: String,
    message: String,
    listener: DialogCallback?,
    vararg names: String,
    cancelable: Boolean = true
) {
    val builder = MessageDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setCanceledOnTouchOutside(false)
        .setCancelable(cancelable)
    for (element in names) {
        builder.addAction(element) { dialog, index ->
            listener?.onButtonAction(dialog, index, null)
            dialog.dismiss()
        }
    }
    builder.show()
}

/**
 * 带输入框的对话框
 * 不可取消，光标定位到最后，调用者控制隐藏
 */
@Suppress("DEPRECATION")
fun Context.showInputDialog(
    title: String,
    defaultText: String?,
    placeholder: String,
    inputType: Int,
    listener: DialogCallback?,
    vararg names: String
) {
    val builder = EditTextDialogBuilder(this)
    builder.setTitle(title)
        .setPlaceholder(placeholder)
        .setInputType(inputType)
        .setDefaultText(defaultText)
        .setCanceledOnTouchOutside(false)
        .create()
    names.forEach {
        builder.addAction(it) { dialog, index ->
            hideSoftInput(builder.editText)
            listener?.onButtonAction(dialog, index, builder.editText.text.toString())
        }
    }
    builder.show()
    defaultText?.let {
        builder.editText.setSelection(it.length)
    }
}

/**
 * 菜单类型的对话框
 */

fun Context.showSelectDialog(listener: DialogCallback?, items: Array<String>) {
    MenuDialogBuilder(this)
        .setCanceledOnTouchOutside(false)
        .addItems(items) { dialog, which ->
            listener?.onItemAction(dialog, which)
            dialog.dismiss()
        }.show()
}