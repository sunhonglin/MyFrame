package com.sunhonglin.base.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.sunhonglin.base.R
import com.qmuiteam.qmui.skin.QMUISkinHelper
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialogView
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView

class QMUITipSingletonDialog(context: Context) : QMUITipDialog(context) {

    class Builder(private var mContext: Context) : QMUITipDialog.Builder(mContext) {

        private var mTipWord: CharSequence = ""
        private val mSkinManager: QMUISkinManager? = null

        /**
         * 设置显示的文案
         */
        override fun setTipWord(tipWord: CharSequence): Builder {
            mTipWord = tipWord
            tipView?.text = tipWord
            return this
        }

        @IconType
        private var mCurrentIconType = ICON_TYPE_NOTHING

        /**
         * 设置 icon 显示的内容
         *
         * @see iconType
         */
        override fun setIconType(@IconType iconType: Int): Builder {
            mCurrentIconType = iconType
            return this
        }

        private var dialog = QMUITipSingletonDialog(mContext)
        private var tipView: QMUISpanTouchFixTextView? = null

        override fun create(cancelable: Boolean): QMUITipSingletonDialog {
            return create(cancelable, R.style.QMUI_TipDialog)
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 [android.app.Dialog.show] 方法
         *
         * @param cancelable 按系统返回键是否可以取消
         * @return 创建的 Dialog
         */
        override fun create(cancelable: Boolean, style: Int): QMUITipSingletonDialog {
            dialog = QMUITipSingletonDialog(mContext)
            dialog.setCancelable(cancelable)
            dialog.setSkinManager(mSkinManager)
            val dialogContext = dialog.context
            val dialogView = QMUITipDialogView(dialogContext)

            val builder = QMUISkinValueBuilder.acquire()
            if (mCurrentIconType == ICON_TYPE_LOADING) {
                val loadingView = QMUILoadingView(dialogContext)
                loadingView.setColor(
                    QMUIResHelper.getAttrColor(
                        dialogContext, R.attr.qmui_skin_support_tip_dialog_loading_color
                    )
                )
                loadingView.setSize(
                    QMUIResHelper.getAttrDimen(
                        dialogContext, R.attr.qmui_tip_dialog_loading_size
                    )
                )
                builder.tintColor(R.attr.qmui_skin_support_tip_dialog_loading_color)
                QMUISkinHelper.setSkinValue(loadingView, builder)
                dialogView.addView(loadingView, onCreateIconOrLoadingLayoutParams(dialogContext))
            } else if (mCurrentIconType == ICON_TYPE_SUCCESS || mCurrentIconType == ICON_TYPE_FAIL || mCurrentIconType == ICON_TYPE_INFO
            ) {
                val imageView: ImageView = AppCompatImageView(dialogContext)
                builder.clear()
                val drawable: Drawable?
                when (mCurrentIconType) {
                    ICON_TYPE_SUCCESS -> {
                        drawable = QMUIResHelper.getAttrDrawable(
                            dialogContext, R.attr.qmui_skin_support_tip_dialog_icon_success_src
                        )
                        builder.src(R.attr.qmui_skin_support_tip_dialog_icon_success_src)
                    }
                    ICON_TYPE_FAIL -> {
                        drawable = QMUIResHelper.getAttrDrawable(
                            dialogContext, R.attr.qmui_skin_support_tip_dialog_icon_error_src
                        )
                        builder.src(R.attr.qmui_skin_support_tip_dialog_icon_error_src)
                    }
                    else -> {
                        drawable = QMUIResHelper.getAttrDrawable(
                            dialogContext, R.attr.qmui_skin_support_tip_dialog_icon_info_src
                        )
                        builder.src(R.attr.qmui_skin_support_tip_dialog_icon_info_src)
                    }
                }
                imageView.setImageDrawable(drawable)
                QMUISkinHelper.setSkinValue(imageView, builder)
                dialogView.addView(imageView, onCreateIconOrLoadingLayoutParams(dialogContext))
            }

            if (mTipWord.isNotEmpty()) {
                tipView = QMUISpanTouchFixTextView(dialogContext)
                tipView?.let {
                    it.ellipsize = TextUtils.TruncateAt.END
                    it.gravity = Gravity.CENTER
                    it.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        QMUIResHelper.getAttrDimen(dialogContext, R.attr.qmui_tip_dialog_text_size)
                            .toFloat()
                    )
                    it.setTextColor(
                        QMUIResHelper.getAttrColor(
                            dialogContext, R.attr.qmui_skin_support_tip_dialog_text_color
                        )
                    )
                    it.text = mTipWord
                    builder.clear()
                    builder.textColor(R.attr.qmui_skin_support_tip_dialog_text_color)
                    QMUISkinHelper.setSkinValue(it, builder)
                    dialogView.addView(
                        tipView,
                        onCreateTextLayoutParams(dialogContext, mCurrentIconType)
                    )
                }
            }
            builder.release()
            dialog.setContentView(dialogView)
            return dialog
        }
    }
}