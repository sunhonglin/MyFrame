package com.sunhonglin.myframe.ui.home

import android.os.Bundle
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.databinding.LayoutDefaultToolbarBinding
import com.sunhonglin.core.util.skipActivity
import com.sunhonglin.core.util.gone
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityHomeBinding
import com.sunhonglin.myframe.ui.login.LoginActivity
import com.sunhonglin.myframe.ui.test.RecyclerViewActivity

class HomeActivity : DefaultToolbarActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun configureToolbarContent(binding: LayoutDefaultToolbarBinding) {
        super.configureToolbarContent(binding)
        binding.imageBack.gone()
        binding.vBack.setDebounceOnClickListener { }
    }

    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        setStatusBarMode(StatusBarMode.LIGHT)
        binding = ActivityHomeBinding.inflate(layoutInflater, parentBinding.content, true)
        binding.tvLogin.setDebounceOnClickListener {
            skipActivity<LoginActivity>(
                mContext,
                "key0" to true,
                "key1" to 1.23F,
                "key2" to listOf("1", "2"),
                requestCode = 2
            )
        }

        binding.tvTestRcv.setDebounceOnClickListener {
            skipActivity<RecyclerViewActivity>(mContext)
        }
    }
}