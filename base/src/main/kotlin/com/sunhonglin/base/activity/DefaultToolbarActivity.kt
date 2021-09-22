package com.sunhonglin.base.activity

import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.databinding.LayoutDefaultToolbarBinding
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.base.utils.visible

abstract class DefaultToolbarActivity : BaseContentActivity() {

    @CallSuper
    override fun configureToolbar(toolbar: Toolbar) {
        toolbar.visible()
        setSupportActionBar(toolbar)
        LayoutDefaultToolbarBinding.inflate(layoutInflater, toolbar, true)
            .apply(::configureToolbarContent)

    }

    open fun configureToolbarContent(toolbarBinding: LayoutDefaultToolbarBinding) {
        setStatusBarMode(StatusBarMode.LIGHT)
        toolbarBinding.textTitle.text = title
        toolbarBinding.vBack.setDebounceOnClickListener {
            this.onBackPressed()
        }
    }
}