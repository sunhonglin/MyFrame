package com.sunhonglin.base.ui.pulltorefresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase
import com.sunhonglin.core.util.inVisible
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.core.util.visible

class PTRRecyclerView : PullToRefreshBase<RecyclerView> {
    var ddy = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, mode: Mode) : super(context, mode)
    constructor(context: Context, mode: Mode, animStyle: AnimationStyle) : super(
        context,
        mode,
        animStyle
    )

    override val pullToRefreshScrollDirection: Orientation
        get() = Orientation.VERTICAL

    override fun createRefreshableView(
        context: Context,
        attrs: AttributeSet?
    ): RecyclerView {
        return RecyclerView(context, attrs)
    }

    /**
     * 设置返回顶部view
     * @param backUpView 显示的view
     */
    fun backUpView(backUpView: View?) {
        val recyclerView = getRefreshableView()
        backUpView?.let {
            it.inVisible()
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    ddy += dy
                    if (ddy > 1200) {
                        it.visible()
                    } else {
                        it.inVisible()
                    }
                }
            })
            it.setDebounceOnClickListener { view ->
                ddy = 0
                view.inVisible()
                recyclerView.scrollToPosition(0)
            }
        }
    }

    override val isReadyForPullEnd: Boolean
        get() = isLastItemVisible

    override val isReadyForPullStart: Boolean
        get() = isFirstItemVisible

    private val isFirstItemVisible: Boolean
        get() {
            val layoutManager = getRefreshableView().layoutManager as LinearLayoutManager
            if (layoutManager.itemCount == 0) {
                return true
            } else {
                if (layoutManager.findFirstCompletelyVisibleItemPosition() <= 1) {
                    val firstVisibleChild = getRefreshableView().getChildAt(0)
                    if (firstVisibleChild != null) {
                        return firstVisibleChild.top >= getRefreshableView().top
                    }
                }
            }
            return false
        }

    private val isLastItemVisible: Boolean
        get() {
            val layoutManager = getRefreshableView().layoutManager as LinearLayoutManager
            if (layoutManager.itemCount == 0) {
                return true
            } else {
                val lastItemPosition = layoutManager.itemCount - 1
                val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastVisiblePosition >= lastItemPosition) {
                    val childIndex =
                        lastVisiblePosition - layoutManager.findFirstCompletelyVisibleItemPosition()
                    val lastVisibleChild = getRefreshableView().getChildAt(childIndex)
                    if (lastVisibleChild != null) {
                        return lastVisibleChild.bottom <= getRefreshableView().bottom
                    }
                }
            }
            return false
        }

    /**
     * 加载数据完成之后 往上再偏移100px
     */
    fun onAppendData() {
        getRefreshableView().scrollBy(0, 100)
    }
}