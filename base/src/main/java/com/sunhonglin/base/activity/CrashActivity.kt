package com.sunhonglin.base.activity

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.sunhonglin.base.BaseApplication
import com.sunhonglin.base.databinding.ActivityCrashBinding
import com.sunhonglin.base.utils.restartApp
import com.sunhonglin.core.util.setDebounceOnClickListener

class CrashActivity : BaseActivity() {
    companion object {
        const val EXTRA_STACK_TRACE = "EXTRA_STACK_TRACE"
    }
    lateinit var binding: ActivityCrashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvError.movementMethod = ScrollingMovementMethod.getInstance()

        binding.tvError.text = intent.getStringExtra(EXTRA_STACK_TRACE)

        binding.tvRestart.setDebounceOnClickListener {
            restartApp(mContext)
        }
    }
}