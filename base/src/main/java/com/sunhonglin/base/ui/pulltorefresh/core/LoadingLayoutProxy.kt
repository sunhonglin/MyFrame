package com.sunhonglin.base.ui.pulltorefresh.core

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import com.sunhonglin.base.ui.pulltorefresh.inn.ILoadingLayout

class LoadingLayoutProxy : ILoadingLayout {

    private var mLoadingLayouts = hashSetOf<LoadingLayout>()

    /**
     * This allows you to add extra LoadingLayout instances to this proxy. This
     * is only necessary if you keep your own instances, and want to have them
     * included in any
     * [ createLoadingLayoutProxy(...)][PullToRefreshBase.createLoadingLayoutProxy] calls.
     *
     * @param layout - LoadingLayout to have included.
     */
    fun addLayout(layout: LoadingLayout?) {
        if (null != layout) {
            mLoadingLayouts.add(layout)
        }
    }

    override fun setLastUpdatedLabel(label: CharSequence?) {
        for (layout in mLoadingLayouts) {
            layout.setLastUpdatedLabel(label)
        }
    }

    override fun setLoadingDrawable(drawable: Drawable?) {
        for (layout in mLoadingLayouts) {
            layout.setLoadingDrawable(drawable)
        }
    }

    override fun setRefreshingLabel(refreshingLabel: CharSequence?) {
        for (layout in mLoadingLayouts) {
            layout.setRefreshingLabel(refreshingLabel)
        }
    }

    override fun setPullLabel(label: CharSequence?) {
        for (layout in mLoadingLayouts) {
            layout.setPullLabel(label)
        }
    }

    override fun resetPullLabel() {
        for (layout in mLoadingLayouts) {
            layout.resetPullLabel()
        }
    }

    override fun setReleaseLabel(label: CharSequence?) {
        for (layout in mLoadingLayouts) {
            layout.setReleaseLabel(label)
        }
    }

    override fun setTextTypeface(tf: Typeface?) {
        for (layout in mLoadingLayouts) {
            layout.setTextTypeface(tf)
        }
    }
}