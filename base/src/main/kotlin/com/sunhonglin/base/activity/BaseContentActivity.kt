package com.sunhonglin.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import com.sunhonglin.base.databinding.ActivityBaseContentBinding

abstract class BaseContentActivity : BaseActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBaseContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureToolbar(binding.toolbar)
        inflateContent(binding, savedInstanceState)
    }

    abstract fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    )

    open fun configureToolbar(toolbar: Toolbar) {

    }
}