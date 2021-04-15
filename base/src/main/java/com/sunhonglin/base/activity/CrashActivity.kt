package com.sunhonglin.base.activity

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.sunhonglin.base.BuildConfig
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.databinding.ActivityCrashBinding
import com.sunhonglin.base.databinding.LayoutDefaultToolbarBinding
import com.sunhonglin.base.utils.restartApp
import com.sunhonglin.core.util.gone
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.core.util.visible

class CrashActivity : DefaultToolbarActivity() {
    companion object {
        const val EXTRA_STACK_TRACE = "EXTRA_STACK_TRACE"
    }

    lateinit var binding: ActivityCrashBinding

    override fun configureToolbarContent(toolbarBinding: LayoutDefaultToolbarBinding) {
        super.configureToolbarContent(toolbarBinding)
        toolbarBinding.vBack.gone()
        toolbarBinding.imageBack.gone()
    }

    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        setStatusBarMode(StatusBarMode.LIGHT)

        binding = ActivityCrashBinding.inflate(layoutInflater, parentBinding.content, true)

        when {
            BuildConfig.DEBUG -> {
                binding.llDebug.visible()
                binding.tvErrorDebug.movementMethod = ScrollingMovementMethod.getInstance()
                binding.tvErrorDebug.text = intent.getStringExtra(EXTRA_STACK_TRACE)
            }
            else -> {
                binding.llRelease.visible()
            }
        }

        binding.tvRestart.setDebounceOnClickListener {
            restartApp(mContext)
        }
    }
}