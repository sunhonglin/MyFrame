package com.sunhonglin.base.activity

import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import com.sunhonglin.base.databinding.LayoutDefaultToolbarBinding
import com.sunhonglin.core.util.setDebounceOnClickListener

abstract class DefaultToolbarActivity : BaseContentActivity() {

    @CallSuper
    override fun configureToolbar(toolbar: Toolbar) {
        toolbar.visibility = View.VISIBLE
        setSupportActionBar(toolbar)
        LayoutDefaultToolbarBinding.inflate(layoutInflater, toolbar, true)
            .apply(::configureToolbarContent)
    }

    open fun configureToolbarContent(toolbarBinding: LayoutDefaultToolbarBinding) {
        toolbarBinding.textTitle.text = title
        toolbarBinding.vBack.setDebounceOnClickListener {
            this.onBackPressed()
        }
    }
}