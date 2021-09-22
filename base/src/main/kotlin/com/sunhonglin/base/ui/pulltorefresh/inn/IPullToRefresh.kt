package com.sunhonglin.base.ui.pulltorefresh.inn

import android.view.View
import android.view.animation.Interpolator
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase.State

interface IPullToRefresh<T : View> {
    /**
     * Demos the Pull-to-Refresh functionality to the user so that they are
     * aware it is there. This could be useful when the user first opens your
     * app, etc. The animation will only happen if the Refresh View (ListView,
     * ScrollView, etc) is in a state where a Pull-to-Refresh could occur by a
     * user's touch gesture (i.e. scrolled to the top/bottom).
     *
     * @return true - if the Demo has been started, false if not.
     */
    fun demo(): Boolean

    /**
     * Get the mode that this view is currently in. This is only really useful
     * when using `Mode.BOTH`.
     *
     * @return Mode that the view is currently in
     */
    fun getCurrentMode(): PullToRefreshBase.Mode?

    /**
     * Returns whether the Touch Events are filtered or not. If true is
     * returned, then the View will only use touch events where the difference
     * in the Y-axis is greater than the difference in the X-axis. This means
     * that the View will not interfere when it is used in a horizontal
     * scrolling View (such as a ViewPager).
     *
     * @return boolean - true if the View is filtering Touch Events
     */
    fun getFilterTouchEvents(): Boolean

    /**
     * Returns a proxy object which allows you to call methods on all of the
     * LoadingLayouts (the Views which show when Pulling/Refreshing).
     *
     * You should not keep the result of this method any longer than you need
     * it.
     *
     * @return Object which will proxy any calls you make on it, to all of the
     * LoadingLayouts.
     */
//    fun getLoadingLayoutProxy(): ILoadingLayout

    /**
     * Returns a proxy object which allows you to call methods on the
     * LoadingLayouts (the Views which show when Pulling/Refreshing). The actual
     * LoadingLayout(s) which will be affected, are chosen by the parameters you
     * give.
     *
     * You should not keep the result of this method any longer than you need
     * it.
     *
     * @param includeStart - Whether to include the Start/Header Views
     * @param includeEnd - Whether to include the End/Footer Views
     * @return Object which will proxy any calls you make on it, to the
     * LoadingLayouts included.
     */
    fun getLoadingLayoutProxy(includeStart: Boolean = true, includeEnd: Boolean = true): ILoadingLayout

    /**
     * Get the mode that this view has been set to. If this returns
     * `Mode.BOTH`, you can use `getCurrentMode()` to
     * check which mode the view is currently in
     *
     * @return Mode that the view has been set to
     */
    fun getMode(): PullToRefreshBase.Mode?

    /**
     * Get the Wrapped Refreshable View. Anything returned here has already been
     * added to the content view.
     *
     * @return The View which is currently wrapped
     */
    fun getRefreshableView(): T

    /**
     * Get whether the 'Refreshing' View should be automatically shown when
     * refreshing. Returns true by default.
     *
     * @return - true if the Refreshing View will be show
     */
    fun getShowViewWhileRefreshing(): Boolean

    /**
     * @return - The state that the View is currently in.
     */
    fun getState(): State?

    /**
     * Whether Pull-to-Refresh is enabled
     *
     * @return enabled
     */
    fun isPullToRefreshEnabled(): Boolean

    /**
     * Gets whether Overscroll support is enabled. This is different to
     * Android's standard Overscroll support (the edge-glow) which is available
     * from GINGERBREAD onwards
     *
     * @return true - if both PullToRefresh-OverScroll and Android's inbuilt
     * OverScroll are enabled
     */
    fun isPullToRefreshOverScrollEnabled(): Boolean

    /**
     * Returns whether the Widget is currently in the Refreshing mState
     *
     * @return true if the Widget is currently refreshing
     */
    fun isRefreshing(): Boolean

    /**
     * Returns whether the widget has enabled scrolling on the Refreshable View
     * while refreshing.
     *
     * @return true if the widget has enabled scrolling while refreshing
     */
    fun isScrollingWhileRefreshingEnabled(): Boolean

    /**
     * Mark the current Refresh as complete. Will Reset the UI and hide the
     * Refreshing View
     */
    fun onRefreshComplete()

    /**
     * Set the Touch Events to be filtered or not. If set to true, then the View
     * will only use touch events where the difference in the Y-axis is greater
     * than the difference in the X-axis. This means that the View will not
     * interfere when it is used in a horizontal scrolling View (such as a
     * ViewPager), but will restrict which types of finger scrolls will trigger
     * the View.
     *
     * @param filterEvents - true if you want to filter Touch Events. Default is
     * true.
     */
    fun setFilterTouchEvents(filterEvents: Boolean)

    /**
     * Set the mode of Pull-to-Refresh that this view will use.
     *
     * @param mode - Mode to set the View to
     */
    fun setMode(mode: PullToRefreshBase.Mode?)

    /**
     * Set OnPullEventListener for the Widget
     *
     * @param listener - Listener to be used when the Widget has a pull event to
     * propagate.
     */
    fun setOnPullEventListener(listener: PullToRefreshBase.OnPullEventListener<T>?)

    /**
     * Set OnRefreshListener for the Widget
     *
     * @param listener - Listener to be used when the Widget is set to Refresh
     */
    fun setOnRefreshListener(listener: PullToRefreshBase.OnRefreshListener<T>?)

    /**
     * Set OnRefreshListener for the Widget
     *
     * @param listener - Listener to be used when the Widget is set to Refresh
     */
    fun setOnRefreshListener(listener: PullToRefreshBase.OnRefreshListener2<T>?)

    /**
     * Sets whether Overscroll support is enabled. This is different to
     * Android's standard Overscroll support (the edge-glow). This setting only
     * takes effect when running on device with Android v2.3 or greater.
     *
     * @param enabled - true if you want Overscroll enabled
     */
    fun setPullToRefreshOverScrollEnabled(enabled: Boolean)

    /**
     * Sets the Widget to be in the refresh state. The UI will be updated to
     * show the 'Refreshing' view, and be scrolled to show such.
     */
    fun setRefreshing()

    /**
     * Sets the Widget to be in the refresh state. The UI will be updated to
     * show the 'Refreshing' view.
     *
     * @param doScroll - true if you want to force a scroll to the Refreshing
     * view.
     */
    fun setRefreshing(doScroll: Boolean)

    /**
     * Sets the Animation Interpolator that is used for animated scrolling.
     * Defaults to a DecelerateInterpolator
     *
     * @param interpolator - Interpolator to use
     */
    fun setScrollAnimationInterpolator(interpolator: Interpolator)

    /**
     * By default the Widget disables scrolling on the Refreshable View while
     * refreshing. This method can change this behaviour.
     *
     * @param scrollingWhileRefreshingEnabled - true if you want to enable
     * scrolling while refreshing
     */
    fun setScrollingWhileRefreshingEnabled(scrollingWhileRefreshingEnabled: Boolean)

    /**
     * A mutator to enable/disable whether the 'Refreshing' View should be
     * automatically shown when refreshing.
     *
     * @param showView
     */
    fun setShowViewWhileRefreshing(showView: Boolean)
}