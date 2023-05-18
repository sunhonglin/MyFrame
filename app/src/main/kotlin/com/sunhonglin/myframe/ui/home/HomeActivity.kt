package com.sunhonglin.myframe.ui.home

import android.os.Bundle
import android.webkit.WebSettings
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.activity.WebActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.databinding.LayoutDefaultToolbarBinding
import com.sunhonglin.base.utils.TimeUtil
import com.sunhonglin.base.utils.ViewUtil.gone
import com.sunhonglin.base.utils.ActivityUtil.skipActivity
import com.sunhonglin.base.utils.setDebounceOnClickListener
import com.sunhonglin.myframe.R
import com.sunhonglin.myframe.databinding.ActivityHomeBinding
import com.sunhonglin.myframe.ui.login.LoginActivity
import com.sunhonglin.myframe.ui.room.RoomActivity
import com.sunhonglin.myframe.ui.test.RecyclerViewActivity
import com.sunhonglin.myframe.ui.test.RegisterForActivityResultTestActivity

class HomeActivity : DefaultToolbarActivity() {
    lateinit var binding: ActivityHomeBinding

    private val onBackPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (TimeUtil.doubleClickExit(1500)) {
                finish()
            } else {
                Toast.makeText(mContext, com.sunhonglin.base.R.string.tip_exit_app, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun configureToolbarContent(toolbarBinding: LayoutDefaultToolbarBinding) {
        super.configureToolbarContent(toolbarBinding)
        toolbarBinding.imageBack.gone()
    }

    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        setStatusBarMode(StatusBarMode.LIGHT)

        onBackPressedDispatcher.addCallback(onBackPress)

        binding = ActivityHomeBinding.inflate(layoutInflater, parentBinding.content, true)
        binding.tvLogin.setDebounceOnClickListener {
            skipActivity<LoginActivity>(
                "key0" to true,
                "key1" to 1.23F,
                "key2" to listOf("1", "2"),
                requestCode = 2
            )
        }

        binding.tvTestRcv.setDebounceOnClickListener {
            skipActivity<RecyclerViewActivity>()
        }

        binding.tvWeb.setDebounceOnClickListener {
            skipActivity<WebActivity>(
                WebActivity.PATH to "https://www.bilibili.com/",
                WebActivity.CACHE to WebSettings.LOAD_NO_CACHE
            )
        }

        binding.tvComponentTest.setDebounceOnClickListener {
            skipActivity<RegisterForActivityResultTestActivity>()
        }

        binding.tvRoom.setDebounceOnClickListener {
            skipActivity<RoomActivity>()
        }
    }
}