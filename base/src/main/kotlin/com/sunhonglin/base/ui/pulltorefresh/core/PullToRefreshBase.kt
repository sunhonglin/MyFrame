package com.sunhonglin.base.ui.pulltorefresh.core

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.sunhonglin.base.R
import com.sunhonglin.base.ui.pulltorefresh.inn.ILoadingLayout
import com.sunhonglin.base.ui.pulltorefresh.inn.IPullToRefresh
import com.sunhonglin.base.utils.showToast
import com.sunhonglin.base.utils.inVisible
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

abstract class PullToRefreshBase<T : View> : LinearLayout, IPullToRefresh<T> {

    companion object {
        const val USE_HW_LAYERS = false
        const val FRICTION = 2.0f
        const val pullToRefreshScrollDuration = 200
        const val pullToRefreshScrollDurationLonger = 325
        const val DEMO_SCROLL_INTERVAL = 225
        const val STATE_STATE = "ptr_state"
        const val STATE_MODE = "ptr_mode"
        const val STATE_CURRENT_MODE = "ptr_current_mode"
        const val STATE_SCROLLING_REFRESHING_ENABLED = "ptr_disable_scrolling"
        const val STATE_SHOW_REFRESHING_VIEW = "ptr_show_refreshing_view"
        const val STATE_SUPER = "ptr_super"
    }

    private var mTouchSlop = 0
    private var mLastMotionX = 0f
    private var mLastMotionY = 0f
    private var mInitialMotionX = 0f
    private var mInitialMotionY = 0f
    private var mIsBeingDragged = false
    private var mState = State.RESET
    private var mMode = Mode.default
    private lateinit var mCurrentMode: Mode
    lateinit var mRefreshableView: T
    private lateinit var refreshableViewWrapper: FrameLayout
    private var mShowViewWhileRefreshing = true
    private var mScrollingWhileRefreshingEnabled = false
    private var mFilterTouchEvents = true
    private var mOverScrollEnabled = true
    private var mLayoutVisibilityChangesEnabled = true
    private var mScrollAnimationInterpolator: Interpolator? = null
    private var mLoadingAnimationStyle = AnimationStyle.default
    private lateinit var headerLayout: LoadingLayout
    private lateinit var footerLayout: LoadingLayout
    private var mOnRefreshListener: OnRefreshListener<T>? = null
    private var mOnRefreshListener2: OnRefreshListener2<T>? = null
    private var mOnPullEventListener: OnPullEventListener<T>? = null
    private var mCurrentSmoothScrollRunnable: SmoothScrollRunnable? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, mode: Mode) : super(context) {
        mMode = mode
        init(context, null)
    }

    constructor(context: Context, mode: Mode, animStyle: AnimationStyle) : super(context) {
        mMode = mode
        mLoadingAnimationStyle = animStyle
        init(context, null)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        Timber.d("addView: ${child.javaClass.simpleName}")
        val refreshableView = getRefreshableView()
        if (refreshableView is ViewGroup) {
            refreshableView.addView(child, index, params)
        } else {
            throw UnsupportedOperationException("Refreshable View is not a ViewGroup so can't addView")
        }
    }

    override fun demo(): Boolean {
        if (mMode.showHeaderLoadingLayout() && isReadyForPullStart) {
            smoothScrollToAndBack(-headerSize * 2)
            return true
        } else if (mMode.showFooterLoadingLayout() && isReadyForPullEnd) {
            smoothScrollToAndBack(footerSize * 2)
            return true
        }
        return false
    }

    override fun getCurrentMode(): Mode? {
        return mCurrentMode
    }

    override fun getFilterTouchEvents(): Boolean {
        return mFilterTouchEvents
    }

//    override fun getLoadingLayoutProxy(): ILoadingLayout {
//        return getLoadingLayoutProxy(includeStart = true, includeEnd = true)
//    }

    override fun getLoadingLayoutProxy(includeStart: Boolean, includeEnd: Boolean): ILoadingLayout {
        return createLoadingLayoutProxy(includeStart, includeEnd)
    }

    override fun getMode(): Mode {
        return mMode
    }

    override fun getRefreshableView(): T {
        return mRefreshableView
    }

    override fun getShowViewWhileRefreshing(): Boolean {
        return mShowViewWhileRefreshing
    }

    override fun getState(): State {
        return mState
    }

    /**
     * Added By Plucky ...not good 解决如果无下一页 不再请求接口的问题
     * @param mode Mode
     */
    fun resetPull(mode: Mode) {
        setMode(mode)
        getLoadingLayoutProxy().resetPullLabel()
    }

    /**
     * 显示没有更多了
     */
    fun showNoMore() {
        setMode(Mode.PULL_FROM_START)
        onRefreshComplete()
        context.showToast(resId = R.string.tip_no_more)
    }

    override fun isPullToRefreshEnabled(): Boolean {
        return mMode.permitsPullToRefresh()
    }

    override fun isPullToRefreshOverScrollEnabled(): Boolean {
        return (mOverScrollEnabled && OverScrollHelper.isAndroidOverScrollEnabled(mRefreshableView))
    }

    override fun isRefreshing(): Boolean {
        return mState == State.REFRESHING || mState == State.MANUAL_REFRESHING
    }

    override fun isScrollingWhileRefreshingEnabled(): Boolean {
        return mScrollingWhileRefreshingEnabled
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (!isPullToRefreshEnabled()) {
            return false
        }
        val action = event.action
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false
            return false
        }
        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true
        }
        when (action) {
            MotionEvent.ACTION_MOVE -> {

                // If we're refreshing, and the flag is set. Eat all MOVE events
                if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
                    return true
                }
                if (isReadyForPull) {
                    val y = event.y
                    val x = event.x
                    val diff: Float
                    val oppositeDiff: Float
                    when (pullToRefreshScrollDirection) {
                        Orientation.HORIZONTAL -> {
                            diff = x - mLastMotionX
                            oppositeDiff = y - mLastMotionY
                        }
                        else -> {
                            diff = y - mLastMotionY
                            oppositeDiff = x - mLastMotionX
                        }
                    }
                    val absDiff: Float = abs(diff)
                    if (absDiff > mTouchSlop && (!mFilterTouchEvents || absDiff > abs(
                            oppositeDiff
                        ))
                    ) {
                        if (mMode.showHeaderLoadingLayout() && diff >= 1f && isReadyForPullStart) {
                            mLastMotionY = y
                            mLastMotionX = x
                            mIsBeingDragged = true
                            if (mMode == Mode.BOTH) {
                                mCurrentMode = Mode.PULL_FROM_START
                            }
                        } else if (mMode.showFooterLoadingLayout() && diff <= -1f && isReadyForPullEnd) {
                            mLastMotionY = y
                            mLastMotionX = x
                            mIsBeingDragged = true
                            if (mMode == Mode.BOTH) {
                                mCurrentMode = Mode.PULL_FROM_END
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_DOWN -> {
                if (isReadyForPull) {
                    mInitialMotionY = event.y
                    mLastMotionY = mInitialMotionY
                    mInitialMotionX = event.x
                    mLastMotionX = mInitialMotionX
                    mIsBeingDragged = false
                }
            }
        }
        return mIsBeingDragged
    }

    override fun onRefreshComplete() {
        if (isRefreshing()) {
            setState(State.RESET)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isPullToRefreshEnabled()) {
            return false
        }

        // If we're refreshing, and the flag is set. Eat the event
        if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
            return true
        }
        if (event.action == MotionEvent.ACTION_DOWN && event.edgeFlags != 0) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (mIsBeingDragged) {
                    mLastMotionY = event.y
                    mLastMotionX = event.x
                    pullEvent()
                    return true
                }
            }
            MotionEvent.ACTION_DOWN -> {
                if (isReadyForPull) {
                    mInitialMotionY = event.y
                    mLastMotionY = mInitialMotionY
                    mInitialMotionX = event.x
                    mLastMotionX = mInitialMotionX
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false
                    if (mState == State.RELEASE_TO_REFRESH
                        && (null != mOnRefreshListener || null != mOnRefreshListener2)
                    ) {
                        setState(State.REFRESHING, true)
                        return true
                    }

                    // If we're already refreshing, just scroll back to the top
                    if (isRefreshing()) {
                        smoothScrollTo(0)
                        return true
                    }

                    // If we haven't returned by here, then we're not in a state
                    // to pull, so just reset
                    setState(State.RESET)
                    return true
                }
            }
        }
        return false
    }

    override fun setScrollingWhileRefreshingEnabled(scrollingWhileRefreshingEnabled: Boolean) {
        mScrollingWhileRefreshingEnabled = scrollingWhileRefreshingEnabled
    }

    override fun setFilterTouchEvents(filterEvents: Boolean) {
        mFilterTouchEvents = filterEvents
    }

    override fun setLongClickable(longClickable: Boolean) {
        getRefreshableView().isLongClickable = longClickable
    }

    override fun setMode(mode: Mode?) {
        mode?.let {
            Timber.d("Setting mode to: $it")
            mMode = it
            updateUIForMode()
        }
    }

    override fun setOnPullEventListener(listener: OnPullEventListener<T>?) {
        mOnPullEventListener = listener
    }

    override fun setOnRefreshListener(listener: OnRefreshListener<T>?) {
        mOnRefreshListener = listener
        mOnRefreshListener2 = null
    }

    override fun setOnRefreshListener(listener: OnRefreshListener2<T>?) {
        mOnRefreshListener2 = listener
        mOnRefreshListener = null
    }

    override fun setPullToRefreshOverScrollEnabled(enabled: Boolean) {
        mOverScrollEnabled = enabled
    }

    override fun setRefreshing() {
        setRefreshing(true)
    }

    override fun setRefreshing(doScroll: Boolean) {
        if (!isRefreshing()) {
            setState(State.MANUAL_REFRESHING, doScroll)
        }
    }

    override fun setScrollAnimationInterpolator(interpolator: Interpolator) {
        mScrollAnimationInterpolator = interpolator
    }

    override fun setShowViewWhileRefreshing(showView: Boolean) {
        mShowViewWhileRefreshing = showView
    }

    fun setState(state: State, vararg params: Boolean) {
        mState = state
//        Timber.d("State: ${mState.name}")
        when (mState) {
            State.RESET -> onReset()
            State.PULL_TO_REFRESH -> onPullToRefresh()
            State.RELEASE_TO_REFRESH -> onReleaseToRefresh()
            State.REFRESHING, State.MANUAL_REFRESHING -> onRefreshing(
                params[0]
            )
            State.OVER_SCROLLING -> {
            }
        }

        // Call OnPullEventListener
        mOnPullEventListener?.onPullEvent(this, mState, mCurrentMode)
    }

    /**
     * Used internally for adding view. Need because we override addView to
     * pass-through to the Refreshable View
     */
    private fun addViewInternal(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
    }

    /**
     * Used internally for adding view. Need because we override addView to
     * pass-through to the Refreshable View
     */
    private fun addViewInternal(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, -1, params)
    }

    private fun createLoadingLayout(
        context: Context,
        mode: Mode,
        attrs: TypedArray
    ): LoadingLayout {
        val layout = mLoadingAnimationStyle.createLoadingLayout(
            context, mode,
            pullToRefreshScrollDirection, attrs
        )
        layout.inVisible()
        return layout
    }

    /**
     * Used internally for [.getLoadingLayoutProxy].
     * Allows derivative classes to include any extra LoadingLayouts.
     */
    fun createLoadingLayoutProxy(
        includeStart: Boolean,
        includeEnd: Boolean
    ): LoadingLayoutProxy {
        val proxy = LoadingLayoutProxy()
        if (includeStart && mMode.showHeaderLoadingLayout()) {
            proxy.addLayout(headerLayout)
        }
        if (includeEnd && mMode.showFooterLoadingLayout()) {
            proxy.addLayout(footerLayout)
        }
        return proxy
    }

    fun disableLoadingLayoutVisibilityChanges() {
        mLayoutVisibilityChangesEnabled = false
    }

    private val footerSize: Int
        get() = footerLayout.contentSize

    private val headerSize: Int
        get() = headerLayout.contentSize

    /**
     * Called when the UI has been to be updated to be in the
     * [State.PULL_TO_REFRESH] state.
     */
    private fun onPullToRefresh() {
        when (mCurrentMode) {
            Mode.PULL_FROM_END -> footerLayout.pullToRefresh()
            Mode.PULL_FROM_START -> headerLayout.pullToRefresh()
            else -> {
            }
        }
    }

    /**
     * Called when the UI has been to be updated to be in the
     * [State.REFRESHING] or [State.MANUAL_REFRESHING] state.
     *
     * @param doScroll - Whether the UI should scroll for this event.
     */
    private fun onRefreshing(doScroll: Boolean) {
        if (mMode.showHeaderLoadingLayout()) {
            headerLayout.refreshing()
        }
        if (mMode.showFooterLoadingLayout()) {
            footerLayout.refreshing()
        }
        if (doScroll) {
            if (mShowViewWhileRefreshing) {

                // Call Refresh Listener when the Scroll has finished
                val listener: OnSmoothScrollFinishedListener =
                    object : OnSmoothScrollFinishedListener {
                        override fun onSmoothScrollFinished() {
                            callRefreshListener()
                        }
                    }
                when (mCurrentMode) {
                    Mode.MANUAL_REFRESH_ONLY, Mode.PULL_FROM_END ->
                        smoothScrollTo(
                            footerSize, listener
                        )
                    else -> smoothScrollTo(-headerSize, listener)
                }
            } else {
                smoothScrollTo(0)
            }
        } else {
            // We're not scrolling, so just call Refresh Listener now
            callRefreshListener()
        }
    }

    /**
     * Called when the UI has been to be updated to be in the
     * [State.RELEASE_TO_REFRESH] state.
     */
    private fun onReleaseToRefresh() {
        when (mCurrentMode) {
            Mode.PULL_FROM_END -> footerLayout.releaseToRefresh()
            Mode.PULL_FROM_START -> headerLayout.releaseToRefresh()
            else -> {
            }
        }
    }

    /**
     * Called when the UI has been to be updated to be in the
     * [State.RESET] state.
     */
    private fun onReset() {
        mIsBeingDragged = false
        mLayoutVisibilityChangesEnabled = true

        // Always reset both layouts, just in case...
        headerLayout.reset()
        footerLayout.reset()
        smoothScrollTo(0)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        /**
         * 解决same id crash 的问题
         */
        try {
            if (state is Bundle) {
                setMode(Mode.mapIntToValue(state.getInt(STATE_MODE, 0)))
                mCurrentMode = Mode.mapIntToValue(state.getInt(STATE_CURRENT_MODE, 0))
                mScrollingWhileRefreshingEnabled = state.getBoolean(
                    STATE_SCROLLING_REFRESHING_ENABLED, false
                )
                mShowViewWhileRefreshing = state.getBoolean(STATE_SHOW_REFRESHING_VIEW, true)

                // Let super Restore Itself
                super.onRestoreInstanceState(state.getParcelable(STATE_SUPER))
                val viewState = State.mapIntToValue(state.getInt(STATE_STATE, 0))
                if (viewState == State.REFRESHING || viewState == State.MANUAL_REFRESHING) {
                    setState(viewState, true)
                }

                // Now let derivative classes restore their state
                onPtrRestoreInstanceState(state)
                return
            }
            super.onRestoreInstanceState(state)
        } catch (e: Exception) {
            Timber.d("Android's fucking crash of the same id")
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()

        // Let derivative classes get a chance to save state first, that way we
        // can make sure they don't overwrite any of our values
        onPtrSaveInstanceState(bundle)
        bundle.putInt(STATE_STATE, mState.intValue)
        bundle.putInt(STATE_MODE, mMode.intValue)
        bundle.putInt(STATE_CURRENT_MODE, mCurrentMode.intValue)
        bundle.putBoolean(STATE_SCROLLING_REFRESHING_ENABLED, mScrollingWhileRefreshingEnabled)
        bundle.putBoolean(STATE_SHOW_REFRESHING_VIEW, mShowViewWhileRefreshing)
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState())
        return bundle
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Timber.d(String.format("onSizeChanged. W: %d, H: %d", w, h))
        super.onSizeChanged(w, h, oldw, oldh)

        // We need to update the header/footer when our size changes
        refreshLoadingViewsSize()

        // Update the Refreshable View layout
        refreshRefreshableViewSize(w, h)
        /**
         * As we're currently in a Layout Pass, we need to schedule another one
         * to layout any changes we've made here
         */
        post { requestLayout() }
    }

    /**
     * Re-measure the Loading Views height, and adjust internal padding as
     * necessary
     */
    private fun refreshLoadingViewsSize() {
        val maximumPullScroll = (maximumPullScroll * 1.2f).toInt()
        var pLeft = paddingLeft
        var pTop = paddingTop
        var pRight = paddingRight
        var pBottom = paddingBottom
        when (pullToRefreshScrollDirection) {
            Orientation.HORIZONTAL -> {
                if (mMode.showHeaderLoadingLayout()) {
                    headerLayout.width = maximumPullScroll
                    pLeft = -maximumPullScroll
                } else {
                    pLeft = 0
                }
                if (mMode.showFooterLoadingLayout()) {
                    footerLayout.width = maximumPullScroll
                    pRight = -maximumPullScroll
                } else {
                    pRight = 0
                }
            }
            Orientation.VERTICAL -> {
                if (mMode.showHeaderLoadingLayout()) {
                    headerLayout.height = maximumPullScroll
                    pTop = -maximumPullScroll
                } else {
                    pTop = 0
                }
                if (mMode.showFooterLoadingLayout()) {
                    footerLayout.height = maximumPullScroll
                    pBottom = -maximumPullScroll
                } else {
                    pBottom = 0
                }
            }
        }
        Timber.d(
            String.format(
                "Setting Padding. L: %d, T: %d, R: %d, B: %d",
                pLeft,
                pTop,
                pRight,
                pBottom
            )
        )
        setPadding(pLeft, pTop, pRight, pBottom)
    }

    private fun refreshRefreshableViewSize(width: Int, height: Int) {
        // We need to set the Height of the Refreshable View to the same as
        // this layout
        val lp = refreshableViewWrapper.layoutParams as LayoutParams
        when (pullToRefreshScrollDirection) {
            Orientation.HORIZONTAL -> if (lp.width != width) {
                lp.width = width
                refreshableViewWrapper.requestLayout()
            }
            Orientation.VERTICAL -> if (lp.height != height) {
                lp.height = height
                refreshableViewWrapper.requestLayout()
            }
        }
    }

    /**
     * Helper method which just calls scrollTo() in the correct scrolling
     * direction.
     *
     * @param value - New Scroll value
     */
    fun setHeaderScroll(value: Int) {
        var mValue = value
//        Timber.d("setHeaderScroll: $mValue")

        // Clamp value to with pull scroll range
        val maximumPullScroll = maximumPullScroll
        mValue = min(maximumPullScroll, max(-maximumPullScroll, mValue))
        if (mLayoutVisibilityChangesEnabled) {
            when {
                mValue < 0 -> {
                    headerLayout.visibility = VISIBLE
                }
                mValue > 0 -> {
                    footerLayout.visibility = VISIBLE
                }
                else -> {
                    headerLayout.visibility = INVISIBLE
                    footerLayout.visibility = INVISIBLE
                }
            }
        }
        if (USE_HW_LAYERS) {
            /**
             * Use a Hardware Layer on the Refreshable View if we've scrolled at
             * all. We don't use them on the Header/Footer Views as they change
             * often, which would negate any HW layer performance boost.
             */
            refreshableViewWrapper.setLayerType(
                if (mValue != 0) LAYER_TYPE_HARDWARE else LAYER_TYPE_NONE,
                null
            )
        }
        when (pullToRefreshScrollDirection) {
            Orientation.VERTICAL -> scrollTo(0, mValue)
            Orientation.HORIZONTAL -> scrollTo(mValue, 0)
        }
    }

    /**
     * Smooth Scroll to position using the default duration of
     * {@value #SMOOTH_SCROLL_DURATION_MS} ms.
     *
     * @param scrollValue - Position to scroll to
     */
    private fun smoothScrollTo(scrollValue: Int) {
        smoothScrollTo(scrollValue, pullToRefreshScrollDuration.toLong())
    }

    /**
     * Smooth Scroll to position using the default duration of
     * {@value #SMOOTH_SCROLL_DURATION_MS} ms.
     *
     * @param scrollValue - Position to scroll to
     * @param listener    - Listener for scroll
     */
    private fun smoothScrollTo(scrollValue: Int, listener: OnSmoothScrollFinishedListener?) {
        smoothScrollTo(scrollValue, pullToRefreshScrollDuration.toLong(), 0, listener)
    }

    /**
     * Smooth Scroll to position using the longer default duration of
     * {@value #SMOOTH_SCROLL_LONG_DURATION_MS} ms.
     *
     * @param scrollValue - Position to scroll to
     */
    fun smoothScrollToLonger(scrollValue: Int) {
        smoothScrollTo(scrollValue, pullToRefreshScrollDurationLonger.toLong())
    }

    /**
     * Updates the View State when the mode has been set. This does not do any
     * checking that the mode is different to current state so always updates.
     */
    private fun updateUIForMode() {
        // We need to use the correct LayoutParam values, based on scroll
        // direction
        val lp = loadingLayoutLayoutParams

        // Remove Header, and then add Header Loading View again if needed
        if (this === headerLayout.parent) {
            removeView(headerLayout)
        }
        if (mMode.showHeaderLoadingLayout()) {
            addViewInternal(headerLayout, 0, lp)
        }

        // Remove Footer, and then add Footer Loading View again if needed
        if (this === footerLayout.parent) {
            removeView(footerLayout)
        }
        if (mMode.showFooterLoadingLayout()) {
            addViewInternal(footerLayout, lp)
        }

        // Hide Loading Views
        refreshLoadingViewsSize()

        // If we're not using Mode.BOTH, set mCurrentMode to mMode, otherwise
        // set it to pull down
        mCurrentMode = if (mMode != Mode.BOTH) mMode else Mode.PULL_FROM_START
    }

    private fun addRefreshableView(context: Context, refreshableView: T?) {
        refreshableViewWrapper = FrameLayout(context)
        refreshableViewWrapper.addView(
            refreshableView, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addViewInternal(
            refreshableViewWrapper, LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun callRefreshListener() {
        mOnRefreshListener?.let {
            it.onRefresh(this)
            return
        }

        mOnRefreshListener2?.let {
            when (mCurrentMode) {
                Mode.PULL_FROM_START -> it.onPullDownToRefresh(this)
                Mode.PULL_FROM_END -> it.onPullUpToRefresh(this)
                else -> return
            }
        }
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        when (pullToRefreshScrollDirection) {
            Orientation.HORIZONTAL -> setOrientation(HORIZONTAL)
            else -> setOrientation(VERTICAL)
        }
        gravity = Gravity.CENTER
        val config = ViewConfiguration.get(context)
        mTouchSlop = config.scaledTouchSlop

        // Styleables from XML
        val a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshBase)
        if (a.hasValue(R.styleable.PullToRefreshBase_ptrMode)) {
            mMode = Mode.mapIntToValue(a.getInteger(R.styleable.PullToRefreshBase_ptrMode, 0))
        }
        if (a.hasValue(R.styleable.PullToRefreshBase_ptrAnimationStyle)) {
            mLoadingAnimationStyle = AnimationStyle.mapIntToValue(
                a.getInteger(
                    R.styleable.PullToRefreshBase_ptrAnimationStyle, 0
                )
            )
        }
        mRefreshableView = createRefreshableView(context, attrs)
        addRefreshableView(context, mRefreshableView)
        headerLayout = createLoadingLayout(context, Mode.PULL_FROM_START, a)
        footerLayout = createLoadingLayout(context, Mode.PULL_FROM_END, a)
        /**
         * Styleables from XML
         */
        if (a.hasValue(R.styleable.PullToRefreshBase_ptrRefreshableViewBackground)) {
            val background =
                a.getDrawable(R.styleable.PullToRefreshBase_ptrRefreshableViewBackground)
            if (null != background) {
                mRefreshableView.background = background
            }
        } else if (a.hasValue(R.styleable.PullToRefreshBase_ptrAdapterViewBackground)) {
            val background = a.getDrawable(R.styleable.PullToRefreshBase_ptrAdapterViewBackground)
            if (null != background) {
                mRefreshableView.background = background
            }
        }
        if (a.hasValue(R.styleable.PullToRefreshBase_ptrOverScroll)) {
            mOverScrollEnabled = a.getBoolean(R.styleable.PullToRefreshBase_ptrOverScroll, true)
        }
        if (a.hasValue(R.styleable.PullToRefreshBase_ptrScrollingWhileRefreshingEnabled)) {
            mScrollingWhileRefreshingEnabled = a.getBoolean(
                R.styleable.PullToRefreshBase_ptrScrollingWhileRefreshingEnabled, false
            )
        }
        handleStyledAttributes(a)
        a.recycle()
        updateUIForMode()
    }

    private val isReadyForPull: Boolean
        get() {
            return when (mMode) {
                Mode.PULL_FROM_START -> isReadyForPullStart
                Mode.PULL_FROM_END -> isReadyForPullEnd
                Mode.BOTH -> isReadyForPullEnd || isReadyForPullStart
                else -> false
            }
        }

    /**
     * Actions a Pull Event
     *
     * @return true if the Event has been handled, false if there has been no
     * change
     */
    private fun pullEvent() {
        val newScrollValue: Int
        val itemDimension: Int
        val initialMotionValue: Float
        val lastMotionValue: Float
        when (pullToRefreshScrollDirection) {
            Orientation.HORIZONTAL -> {
                initialMotionValue = mInitialMotionX
                lastMotionValue = mLastMotionX
            }
            else -> {
                initialMotionValue = mInitialMotionY
                lastMotionValue = mLastMotionY
            }
        }
        when (mCurrentMode) {
            Mode.PULL_FROM_END -> {
                newScrollValue =
                    (max(initialMotionValue - lastMotionValue, 0f) / FRICTION).roundToInt()
                itemDimension = footerSize
            }
            Mode.PULL_FROM_START -> {
                newScrollValue =
                    (min(initialMotionValue - lastMotionValue, 0f) / FRICTION).roundToInt()
                itemDimension = headerSize
            }
            else -> {
                newScrollValue =
                    (min(initialMotionValue - lastMotionValue, 0f) / FRICTION).roundToInt()
                itemDimension = headerSize
            }
        }
        setHeaderScroll(newScrollValue)
        if (newScrollValue != 0 && !isRefreshing()) {
            val scale = abs(newScrollValue) / itemDimension.toFloat()
            when (mCurrentMode) {
                Mode.PULL_FROM_END -> footerLayout.onPull(scale)
                Mode.PULL_FROM_START -> headerLayout.onPull(scale)
                else -> headerLayout.onPull(scale)
            }
            if (mState != State.PULL_TO_REFRESH && itemDimension >= abs(newScrollValue)) {
                setState(State.PULL_TO_REFRESH)
            } else if (mState == State.PULL_TO_REFRESH && itemDimension < abs(newScrollValue)) {
                setState(State.RELEASE_TO_REFRESH)
            }
        }
    }

    private val loadingLayoutLayoutParams: LayoutParams
        get() {
            return when (pullToRefreshScrollDirection) {
                Orientation.HORIZONTAL -> LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
                )
                else -> LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
        }
    private val maximumPullScroll: Int
        get() {
            return when (pullToRefreshScrollDirection) {
                Orientation.HORIZONTAL -> (width / FRICTION).roundToInt()
                else -> (height / FRICTION).roundToInt()
            }
        }

    /**
     * Smooth Scroll to position using the specific duration
     *
     * @param scrollValue - Position to scroll to
     * @param duration    - Duration of animation in milliseconds
     */
    private fun smoothScrollTo(scrollValue: Int, duration: Long) {
        smoothScrollTo(scrollValue, duration, 0, null)
    }

    private fun smoothScrollTo(
        newScrollValue: Int, duration: Long, delayMillis: Long,
        listener: OnSmoothScrollFinishedListener?
    ) {
        mCurrentSmoothScrollRunnable?.stop()
        val oldScrollValue = when (pullToRefreshScrollDirection) {
            Orientation.HORIZONTAL -> scrollX
            else -> scrollY
        }
        if (oldScrollValue != newScrollValue) {
            if (null == mScrollAnimationInterpolator) {
                // Default interpolator is a Decelerate Interpolator
                mScrollAnimationInterpolator = DecelerateInterpolator()
            }
            mCurrentSmoothScrollRunnable =
                SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener)
            if (delayMillis > 0) {
                postDelayed(mCurrentSmoothScrollRunnable, delayMillis)
            } else {
                post(mCurrentSmoothScrollRunnable)
            }
        }
    }

    private fun smoothScrollToAndBack(y: Int) {
        smoothScrollTo(
            y,
            pullToRefreshScrollDuration.toLong(),
            0,
            object : OnSmoothScrollFinishedListener {
                override fun onSmoothScrollFinished() {
                    smoothScrollTo(
                        0,
                        pullToRefreshScrollDuration.toLong(),
                        DEMO_SCROLL_INTERVAL.toLong(),
                        null
                    )
                }
            })
    }

    enum class AnimationStyle {
        /**
         * This is the default for Android-PullToRefresh. Allows you to use any
         * drawable, which is automatically rotated and used as a Progress Bar.
         */
        ROTATE,

        /**
         * This is the old default, and what is commonly used on iOS. Uses an
         * arrow image which flips depending on where the user has scrolled.
         */
        FLIP;

        fun createLoadingLayout(
            context: Context,
            mode: Mode,
            scrollDirection: Orientation,
            attrs: TypedArray
        ): LoadingLayout {
            return when (this) {
                FLIP -> FlipLoadingLayout(
                    context,
                    mode, scrollDirection, attrs
                )
                else -> RotateLoadingLayout(
                    context, mode, scrollDirection,
                    attrs
                )
            }
        }

        companion object {
            val default: AnimationStyle
                get() = ROTATE

            /**
             * Maps an int to a specific mode. This is needed when saving state, or
             * inflating the view from XML where the mode is given through a attr
             * int.
             *
             * @param modeInt - int to map a Mode to
             * @return Mode that modeInt maps to, or ROTATE by default.
             */
            fun mapIntToValue(modeInt: Int): AnimationStyle {
                return when (modeInt) {
                    0x1 -> FLIP
                    else -> ROTATE
                }
            }
        }
    }

    // The modeInt values need to match those from attrs.xml
    enum class Mode(val intValue: Int) {
        /**
         * Disable all Pull-to-Refresh gesture and Refreshing handling
         * 禁用刷新加载
         */
        DISABLED(0x0),

        /**
         * Only allow the user to Pull from the start of the Refreshable View to
         * refresh. The start is either the Top or Left, depending on the
         * scrolling direction.
         * 仅仅支持刷新
         */
        PULL_FROM_START(0x1),

        /**
         * Only allow the user to Pull from the end of the Refreshable View to
         * refresh. The start is either the Bottom or Right, depending on the
         * scrolling direction.
         * 仅仅支持加载更多
         */
        PULL_FROM_END(0x2),

        /**
         * Allow the user to both Pull from the start, from the end to refresh.
         * 刷新加载都支持
         */
        BOTH(0x3),

        /**
         * Disables Pull-to-Refresh gesture handling, but allows manually
         * setting the Refresh state via
         * [setRefreshing()][PullToRefreshBase.setRefreshing].
         * 只允许手动触发
         */
        MANUAL_REFRESH_ONLY(0x4);

        /**
         * @return true if the mode permits Pull-to-Refresh
         * 如果当前模式允许刷新则返回true
         */
        fun permitsPullToRefresh(): Boolean {
            return !(this == DISABLED || this == MANUAL_REFRESH_ONLY)
        }

        /**
         * @return true if this mode wants the Loading Layout Header to be shown
         * 如果该模式下能加载显示header部分，则返回true
         */
        fun showHeaderLoadingLayout(): Boolean {
            return this == PULL_FROM_START || this == BOTH
        }

        /**
         * @return true if this mode wants the Loading Layout Footer to be shown
         * 如果该模式下能加载显示footer部分，则返回true
         */
        fun showFooterLoadingLayout(): Boolean {
            return this == PULL_FROM_END || this == BOTH || this == MANUAL_REFRESH_ONLY
        }

        companion object {
            /**
             * Maps an int to a specific mode. This is needed when saving state, or
             * inflating the view from XML where the mode is given through a attr
             * int.
             *
             * @param modeInt - int to map a Mode to
             * @return Mode that modeInt maps to, or PULL_FROM_START by default.
             */
            fun mapIntToValue(modeInt: Int): Mode {
                for (value: Mode in values()) {
                    if (modeInt == value.intValue) {
                        return value
                    }
                }

                // If not, return default
                return default
            }

            //默认状态只支持刷新
            val default: Mode
                get() = PULL_FROM_START
        }
    }

    /**
     * Simple Listener that allows you to be notified when the user has scrolled
     * to the end of the AdapterView.
     *
     * @author Chris Banes
     */
    interface OnLastItemVisibleListener {
        /**
         * Called when the user has scrolled to the end of the list
         */
        fun onLastItemVisible()
    }

    /**
     * Listener that allows you to be notified when the user has started or
     * finished a touch event. Useful when you want to append extra UI events
     * (such as sounds).
     *
     * @author Chris Banes
     */
    interface OnPullEventListener<V : View> {
        /**
         * Called when the internal state has been changed, usually by the user
         * pulling.
         *
         * @param refreshView - View which has had it's state change.
         * @param state       - The new state of View.
         * @param direction   - One of [Mode.PULL_FROM_START] or
         * [Mode.PULL_FROM_END] depending on which direction
         * the user is pulling. Only useful when <var>state</var> is
         * [State.PULL_TO_REFRESH] or
         * [State.RELEASE_TO_REFRESH].
         */
        fun onPullEvent(refreshView: PullToRefreshBase<V>, state: State?, direction: Mode?)
    }

    /**
     * Simple Listener to listen for any callbacks to Refresh.
     *
     * @author Chris Banes
     */
    interface OnRefreshListener<V : View> {
        /**
         * onRefresh will be called for both a Pull from start, and Pull from
         * end
         */
        fun onRefresh(refreshView: PullToRefreshBase<V>?)
    }

    /**
     * An advanced version of the Listener to listen for callbacks to Refresh.
     * This listener is different as it allows you to differentiate between Pull
     * Ups, and Pull Downs.
     *
     * @author Chris Banes
     */
    interface OnRefreshListener2<V : View> {
        /**
         * onPullDownToRefresh will be called only when the user has Pulled from
         * the start, and released.
         */
        fun onPullDownToRefresh(refreshView: PullToRefreshBase<V>)

        /**
         * onPullUpToRefresh will be called only when the user has Pulled from
         * the end, and released.
         */
        fun onPullUpToRefresh(refreshView: PullToRefreshBase<V>)
    }

    enum class Orientation {
        VERTICAL, HORIZONTAL
    }

    enum class State(val intValue: Int) {
        /**
         * When the UI is in a state which means that user is not interacting
         * with the Pull-to-Refresh function.
         * 重置初始化状态
         */
        RESET(0x0),

        /**
         * When the UI is being pulled by the user, but has not been pulled far
         * enough so that it refreshes when released.
         * 拉动距离不足指定阈值，进行释放
         */
        PULL_TO_REFRESH(0x1),

        /**
         * When the UI is being pulled by the user, and **has**
         * been pulled far enough so that it will refresh when released.
         * 拉动距离大于等于指定阈值，进行释放
         */
        RELEASE_TO_REFRESH(0x2),

        /**
         * When the UI is currently refreshing, caused by a pull gesture.
         * 由于用户手势操作，引起当前UI刷新
         */
        REFRESHING(0x8),

        /**
         * When the UI is currently refreshing, caused by a call to
         * [setRefreshing()][PullToRefreshBase.setRefreshing].
         * 由于代码调用setRefreshing引起刷新UI
         */
        MANUAL_REFRESHING(0x9),

        /**
         * When the UI is currently overScrolling, caused by a fling on the
         * Refreshable View.
         * 由于结束滑动，可以刷新视图
         */
        OVER_SCROLLING(0x10);

        companion object {
            /**
             * Maps an int to a specific state. This is needed when saving state.
             *
             * @param stateInt - int to map a State to
             * @return State that stateInt maps to
             */
            fun mapIntToValue(stateInt: Int): State {
                for (value: State in values()) {
                    if (stateInt == value.intValue) {
                        return value
                    }
                }

                // If not, return default
                return RESET
            }
        }
    }

    internal inner class SmoothScrollRunnable(
        private val mScrollFromY: Int,
        private val mScrollToY: Int,
        duration: Long,
        listener: OnSmoothScrollFinishedListener?
    ) : Runnable {
        private val mInterpolator = mScrollAnimationInterpolator
        private val mDuration: Long = duration
        private val mListener = listener
        private var mContinueRunning = true
        private var mStartTime: Long = -1
        private var mCurrentY = -1
        override fun run() {
            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1L) {
                mStartTime = System.currentTimeMillis()
            } else {
                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                var normalizedTime = 1000 * (System.currentTimeMillis() - mStartTime) / mDuration
                normalizedTime = max(min(normalizedTime, 1000), 0)
                val deltaY = ((mScrollFromY - mScrollToY)
                        * mInterpolator!!.getInterpolation(normalizedTime / 1000f)).roundToInt()
                mCurrentY = mScrollFromY - deltaY
                setHeaderScroll(mCurrentY)
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                ViewCompat.postOnAnimation(this@PullToRefreshBase, this)
            } else {
                mListener?.onSmoothScrollFinished()
            }
        }

        fun stop() {
            mContinueRunning = false
            removeCallbacks(this)
        }

    }

    /**
     * Allows Derivative classes to handle the XML Attrs without creating a
     * TypedArray
     *
     * @param a - TypedArray of PullToRefresh Attributes
     */
    open fun handleStyledAttributes(a: TypedArray?) {}

    /**
     * Called by [.onRestoreInstanceState] so that derivative
     * classes can handle their saved instance state.
     *
     * @param savedInstanceState - Bundle which contains saved instance state.
     */
    open fun onPtrRestoreInstanceState(savedInstanceState: Bundle?) {}

    /**
     * Called by [.onSaveInstanceState] so that derivative classes can
     * save their instance state.
     *
     * @param saveState - Bundle to be updated with saved state.
     */
    open fun onPtrSaveInstanceState(saveState: Bundle?) {}

    interface OnSmoothScrollFinishedListener {
        fun onSmoothScrollFinished()
    }

    /**
     * This is implemented by derived classes to return the created View. If you
     * need to use a custom View (such as a custom ListView), override this
     * method and return an instance of your custom class.
     *
     * Be sure to set the ID of the view in this method, especially if you're
     * using a ListActivity or ListFragment.
     *
     * @param context Context to create view with
     * @param attrs   AttributeSet from wrapped class. Means that anything you
     * include in the XML layout declaration will be routed to the
     * created View
     * @return New instance of the Refreshable View
     */
    abstract fun createRefreshableView(context: Context, attrs: AttributeSet?): T

    /**
     * Implemented by derived class to return whether the View is in a state
     * where the user can Pull to Refresh by scrolling from the end.
     *
     * @return true if the View is currently in the correct state (for example,
     * bottom of a ListView)
     */
    abstract val isReadyForPullEnd: Boolean

    /**
     * Implemented by derived class to return whether the View is in a state
     * where the user can Pull to Refresh by scrolling from the start.
     *
     * @return true if the View is currently the correct state (for example, top
     * of a ListView)
     */
    abstract val isReadyForPullStart: Boolean

    /**
     * @return Either [Orientation.VERTICAL] or
     * [Orientation.HORIZONTAL] depending on the scroll direction.
     */
    abstract val pullToRefreshScrollDirection: Orientation
}