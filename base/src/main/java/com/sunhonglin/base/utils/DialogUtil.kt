package com.sunhonglin.base.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.*
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog


class DialogUtil {

    interface DialogCallback {
        fun onItemAction(dialog: DialogInterface, which: Int)

        fun onButtonAction(dialog: DialogInterface, index: Int, result: String?)

    }

    companion object {
        const val CANCEL = "取消"
        const val TITLE = "提示"
        const val SURE = "确定"
        const val START = "开始"
        const val CLOSE = "关闭"
        const val GO_SETTING = "去设置"

        /**
         * 消息类型对话框
         */
        fun showConfirmDialog(
            context: Context?,
            title: String,
            message: String,
            listener: DialogCallback?,
            vararg names: String
        ) {
            if (context is Activity) {
                if (context.isDestroyed) return
            }

            val builder = MessageDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setCanceledOnTouchOutside(false)
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
        fun showInputDialog(
            context: Context?,
            title: String,
            defaultText: String?,
            placeholder: String,
            inputType: Int,
            listener: DialogCallback?,
            vararg names: String
        ) {
            if ((context as Activity).isDestroyed) return
            val builder = EditTextDialogBuilder(context)
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

        fun showSelectDialog(context: Context?, listener: DialogCallback?, items: Array<String>) {
            if ((context as Activity).isDestroyed) return
            MenuDialogBuilder(context)
                .setCanceledOnTouchOutside(false)
                .addItems(items) { dialog, which ->
                    listener?.onItemAction(dialog, which)
                    dialog.dismiss()
                }.show()
        }

        /**
         * 提示
         */
        fun showTipDialog(context: Context?, tipMsg: String) {
            if ((context as Activity).isDestroyed) return
            QMUITipDialog.Builder(context).setTipWord(tipMsg).create().apply {
                setOwnerActivity(context)
                show()
            }.apply {
                Handler().postDelayed({
                    try {
                        dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, 800)
            }
        }
    }
}