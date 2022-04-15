package com.sunhonglin.base.ui

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sunhonglin.base.R
import com.sunhonglin.base.databinding.LayoutCustomDialogBinding
import com.sunhonglin.base.utils.getDisplayWidth
import com.sunhonglin.base.utils.hideSoftInput

class CustomDialog(
    val activity: ComponentActivity
) : AppCompatDialog(activity, R.style.CustomDialog) {

    var binding: LayoutCustomDialogBinding
    private var animIn: Int = R.anim.anim_alpha_in_150
    private var animOut: Int = R.anim.anim_alpha_out_150

    init {
        // height 不能使用DisplayHeight，否则会导致8.0.0的设备遮挡底部弹出框
        // Honor7C Android 8.0.0已适配
        window?.setLayout(activity.getDisplayWidth(), ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setWindowAnimations(-1) //去除原有动画
        binding = LayoutCustomDialogBinding.inflate(layoutInflater)
        super.setContentView(binding.root)

        binding.root.setOnClickListener { dismiss() }
        binding.llContent.setOnClickListener(null)

        setCancelable(true)
        setCanceledOnTouchOutside(true)

        activity.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> superDismiss()
                else -> {}
            }
        })
    }

    /**
     * 三种形式
     * 0 从底部
     * 1 从右边
     * 2 alpha，默认
     */
    fun setView(
        v: View,
        style: Int = 2,
        gravity: Int = Gravity.CENTER,
        backgroundDimEnabled: Boolean = true
    ) {
        when (style) {
            0 -> {
                animIn = R.anim.anim_translate_bottom_in_150
                animOut = R.anim.anim_translate_bottom_out_150
            }
            1 -> {
                animIn = R.anim.anim_translate_right_in_150
                animOut = R.anim.anim_translate_right_out_150
            }
            2 -> {
                animIn = R.anim.anim_alpha_in_150
                animOut = R.anim.anim_alpha_out_150
            }
        }

        if (!backgroundDimEnabled) window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        window?.attributes?.apply {
//            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
////            dimAmount = when (isDim) {
////                true -> 0.5f
////                else -> 0f
////            }
//        }

        (binding.llContent.layoutParams as FrameLayout.LayoutParams).gravity = gravity

        binding.llContent.removeAllViews()
        binding.llContent.addView(v)
    }

    override fun show() {
        super.show()
        hideSoftInput(binding.root)
        binding.root.startAnimation(AnimationUtils.loadAnimation(context, animIn))
    }

    override fun dismiss() {
        val anim = AnimationUtils.loadAnimation(context, animOut)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                superDismiss()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        binding.root.startAnimation(anim)
    }

    private fun superDismiss() {
        super.dismiss()
    }
}