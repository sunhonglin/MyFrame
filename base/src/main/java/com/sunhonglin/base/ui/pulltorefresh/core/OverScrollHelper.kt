package com.sunhonglin.base.ui.pulltorefresh.core

import android.view.View
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase.*
import timber.log.Timber
import kotlin.math.abs

class OverScrollHelper {

    companion object {
        const val DEFAULT_OVER_SCROLL_SCALE = 1f

        fun isAndroidOverScrollEnabled(view: View): Boolean {
            return view.overScrollMode != View.OVER_SCROLL_NEVER
        }
    }

    /**
     * Helper method for OverScrolling that encapsulates all of the necessary
     * function.
     *
     * This should only be used on AdapterView's such as ListView as it just
     * calls through to overScrollBy() with the scrollRange = 0. AdapterView's
     * do not have a scroll range (i.e. getScrollY() doesn't work).
     *
     * @param view - PullToRefreshView that is calling this.
     * @param deltaX - Change in X in pixels, passed through from from
     * overScrollBy call
     * @param scrollX - Current X scroll value in pixels before applying deltaY,
     * passed through from from overScrollBy call
     * @param deltaY - Change in Y in pixels, passed through from from
     * overScrollBy call
     * @param scrollY - Current Y scroll value in pixels before applying deltaY,
     * passed through from from overScrollBy call
     * @param isTouchEvent - true if this scroll operation is the result of a
     * touch event, passed through from from overScrollBy call
     */
    fun overScrollBy(
        view: PullToRefreshBase<*>, deltaX: Int, scrollX: Int,
        deltaY: Int, scrollY: Int, isTouchEvent: Boolean
    ) {
        overScrollBy(view, deltaX, scrollX, deltaY, scrollY, 0, isTouchEvent)
    }

    /**
     * Helper method for OverScrolling that encapsulates all of the necessary
     * function. This version of the call is used for Views that need to specify
     * a Scroll Range but scroll back to it's edge correctly.
     *
     * @param view - PullToRefreshView that is calling this.
     * @param deltaX - Change in X in pixels, passed through from from
     * overScrollBy call
     * @param scrollX - Current X scroll value in pixels before applying deltaY,
     * passed through from from overScrollBy call
     * @param deltaY - Change in Y in pixels, passed through from from
     * overScrollBy call
     * @param scrollY - Current Y scroll value in pixels before applying deltaY,
     * passed through from from overScrollBy call
     * @param scrollRange - Scroll Range of the View, specifically needed for
     * ScrollView
     * @param isTouchEvent - true if this scroll operation is the result of a
     * touch event, passed through from from overScrollBy call
     */
    fun overScrollBy(
        view: PullToRefreshBase<*>, deltaX: Int, scrollX: Int,
        deltaY: Int, scrollY: Int, scrollRange: Int, isTouchEvent: Boolean
    ) {
        overScrollBy(
            view,
            deltaX,
            scrollX,
            deltaY,
            scrollY,
            scrollRange,
            0,
            DEFAULT_OVER_SCROLL_SCALE,
            isTouchEvent
        )
    }

    /**
     * Helper method for OverScrolling that encapsulates all of the necessary
     * function. This is the advanced version of the call.
     *
     * @param view - PullToRefreshView that is calling this.
     * @param deltaX - Change in X in pixels, passed through from from
     * overScrollBy call
     * @param scrollX - Current X scroll value in pixels before applying deltaY,
     * passed through from from overScrollBy call
     * @param deltaY - Change in Y in pixels, passed through from from
     * overScrollBy call
     * @param scrollY - Current Y scroll value in pixels before applying deltaY,
     * passed through from from overScrollBy call
     * @param scrollRange - Scroll Range of the View, specifically needed for
     * ScrollView
     * @param fuzzyThreshold - Threshold for which the values how fuzzy we
     * should treat the other values. Needed for WebView as it
     * doesn't always scroll back to it's edge. 0 = no fuzziness.
     * @param scaleFactor - Scale Factor for overScroll amount
     * @param isTouchEvent - true if this scroll operation is the result of a
     * touch event, passed through from from overScrollBy call
     */
    fun overScrollBy(
        view: PullToRefreshBase<*>, deltaX: Int, scrollX: Int,
        deltaY: Int, scrollY: Int, scrollRange: Int, fuzzyThreshold: Int,
        scaleFactor: Float, isTouchEvent: Boolean
    ) {
        val deltaValue: Int
        val currentScrollValue: Int
        val scrollValue: Int
        when (view.pullToRefreshScrollDirection) {
            Orientation.HORIZONTAL -> {
                deltaValue = deltaX
                scrollValue = scrollX
                currentScrollValue = view.scrollX
            }
            else -> {
                deltaValue = deltaY
                scrollValue = scrollY
                currentScrollValue = view.scrollY
            }
        }

        // Check that OverScroll is enabled and that we're not currently
        // refreshing.
        if (view.isPullToRefreshOverScrollEnabled() && !view.isRefreshing()) {
            val mode: Mode = view.getMode()

            // Check that Pull-to-Refresh is enabled, and the event isn't from
            // touch
            if (mode.permitsPullToRefresh() && !isTouchEvent && deltaValue != 0) {
                val newScrollValue = deltaValue + scrollValue

                Timber.d("""OverScroll. DeltaX: $deltaX, ScrollX: $scrollX, DeltaY: $deltaY, ScrollY: $scrollY, NewY: $newScrollValue, ScrollRange: $scrollRange, CurrentScroll: $currentScrollValue""")
                if (newScrollValue < 0 - fuzzyThreshold) {
                    // Check the mode supports the overScroll direction, and
                    // then move scroll
                    if (mode.showHeaderLoadingLayout()) {
                        // If we're currently at zero, we're about to start
                        // overScrolling, so change the state
                        if (currentScrollValue == 0) {
                            view.setState(State.OVER_SCROLLING)
                        }
                        view.setHeaderScroll((scaleFactor * (currentScrollValue + newScrollValue)).toInt())
                    }
                } else if (newScrollValue > scrollRange + fuzzyThreshold) {
                    // Check the mode supports the overScroll direction, and
                    // then move scroll
                    if (mode.showFooterLoadingLayout()) {
                        // If we're currently at zero, we're about to start
                        // overScrolling, so change the state
                        if (currentScrollValue == 0) {
                            view.setState(State.OVER_SCROLLING)
                        }
                        view.setHeaderScroll((scaleFactor * (currentScrollValue + newScrollValue - scrollRange)).toInt())
                    }
                } else if (abs(newScrollValue) <= fuzzyThreshold
                    || abs(newScrollValue - scrollRange) <= fuzzyThreshold
                ) {
                    // Means we've stopped overScrolling, so scroll back to 0
                    view.setState(State.RESET)
                }
            } else if (isTouchEvent && State.OVER_SCROLLING === view.getState()) {
                // This condition means that we were overScrolling from a fling,
                // but the user has touched the View and is now overScrolling
                // from touch instead. We need to just reset.
                view.setState(State.RESET)
            }
        }
    }
}