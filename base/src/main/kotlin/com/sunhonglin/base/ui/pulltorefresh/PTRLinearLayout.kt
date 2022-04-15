package com.sunhonglin.base.ui.pulltorefresh

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase

class PTRLinearLayout: PullToRefreshBase<LinearLayoutCompat> {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, mode: Mode) : super(context, mode)
    constructor(context: Context, mode: Mode, animStyle: AnimationStyle) : super(
        context,
        mode,
        animStyle
    )

    override fun createRefreshableView(context: Context, attrs: AttributeSet?): LinearLayoutCompat {
        return LinearLayoutCompat(context, attrs)
    }

    override val isReadyForPullEnd: Boolean
        get() = false
    override val isReadyForPullStart: Boolean
        get() = true
    override val pullToRefreshScrollDirection: Orientation
        get() = Orientation.VERTICAL
}