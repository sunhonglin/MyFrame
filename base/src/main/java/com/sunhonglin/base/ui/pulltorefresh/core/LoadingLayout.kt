package com.sunhonglin.base.ui.pulltorefresh.core

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.core.view.ViewCompat
import com.sunhonglin.base.R
import com.sunhonglin.base.databinding.LayoutPullToRefreshHeaderHorizontalBinding
import com.sunhonglin.base.databinding.LayoutPullToRefreshHeaderVerticalBinding
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase.Mode
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase.Orientation
import com.sunhonglin.base.ui.pulltorefresh.inn.ILoadingLayout
import com.sunhonglin.core.util.gone
import com.sunhonglin.core.util.inVisible
import com.sunhonglin.core.util.visible

abstract class LoadingLayout(
    context: Context,
    mode: Mode,
    scrollDirection: Orientation,
    attrs: TypedArray
) : FrameLayout(context), ILoadingLayout {

    var mInnerLayout: FrameLayout
    var mHeaderImage: ImageView
    var mHeaderProgress: ProgressBar

    var ivRefreshTop: ImageView? = null
    var lRefreshTips: LinearLayout? = null
    var mHeaderText: TextView? = null
    var mSubHeaderText: TextView? = null

    var mUseIntrinsicAnimation = false
    val mMode: Mode = mode
    val mScrollDirection: Orientation = scrollDirection
    var mPullLabel: CharSequence? = null
    var defaultPullLabel: CharSequence? = null
    var mRefreshingLabel: CharSequence? = null
    var mReleaseLabel: CharSequence? = null

    companion object {
        val ANIMATION_INTERPOLATOR: Interpolator = LinearInterpolator()
    }

    init {
        when (scrollDirection) {
            Orientation.HORIZONTAL -> {
                val binding = LayoutPullToRefreshHeaderHorizontalBinding.inflate(
                    LayoutInflater.from(context),
                    this
                )
                mInnerLayout = binding.flInner
                mHeaderImage = binding.pullToRefreshImage
                mHeaderProgress = binding.pullToRefreshProgress
            }
            Orientation.VERTICAL -> {
                val binding = LayoutPullToRefreshHeaderVerticalBinding.inflate(
                    LayoutInflater.from(context), this
                )
                mInnerLayout = binding.flInner
                mHeaderImage = binding.pullToRefreshImage
                mHeaderProgress = binding.pullToRefreshProgress

                mHeaderText = binding.pullToRefreshText
                mSubHeaderText = binding.pullToRefreshSubText
                ivRefreshTop = binding.ivRefreshTop
                lRefreshTips = binding.LRefreshTips
            }
        }

        /**
         * 第一步:设置刷新控件头部的图片
         */
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrRefreshTop) && mode === Mode.PULL_FROM_START) {
            val refreshTop = attrs.getDrawable(R.styleable.PullToRefresh_ptrRefreshTop)
            refreshTop?.let {
                ivRefreshTop?.setImageDrawable(it)
            }
        }
        /**
         * 第二步：控制提示文字显示不显示
         */
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrShowRefreshTips) && mode === Mode.PULL_FROM_START) {
            val isShow = attrs.getBoolean(R.styleable.PullToRefresh_ptrShowRefreshTips, true)
            lRefreshTips?.let {
                when (isShow) {
                    true -> it.visible()
                    false -> it.gone()
                }
            }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrShowLoadMoreTips) && mode === Mode.PULL_FROM_END) {
            val isShow = attrs.getBoolean(R.styleable.PullToRefresh_ptrShowLoadMoreTips, true)
            lRefreshTips?.let {
                when (isShow) {
                    true -> it.visible()
                    false -> it.gone()
                }
            }
        }
        /**
         * 设置Progress样式
         */
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrProgressIndeterminateDrawable)) {
            val ptrProgressIndeterminateDrawable =
                attrs.getDrawable(R.styleable.PullToRefresh_ptrProgressIndeterminateDrawable)
            ptrProgressIndeterminateDrawable?.let {
                mHeaderProgress.indeterminateDrawable = it
            }
        }

        val lp = mInnerLayout.layoutParams as LayoutParams
        when (mode) {
            Mode.PULL_FROM_END -> {
                lp.gravity =
                    if (scrollDirection === Orientation.VERTICAL) Gravity.TOP else Gravity.LEFT

                // Load in labels
                mPullLabel = context.getString(R.string.pull_to_refresh_from_bottom_pull_label)
                mRefreshingLabel =
                    context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label)
                mReleaseLabel =
                    context.getString(R.string.pull_to_refresh_from_bottom_release_label)
                defaultPullLabel =
                    context.getString(R.string.pull_to_refresh_from_bottom_pull_label)
            }
            Mode.PULL_FROM_START -> {
                lp.gravity =
                    if (scrollDirection === Orientation.VERTICAL) Gravity.BOTTOM else Gravity.RIGHT

                // Load in labels
                mPullLabel = context.getString(R.string.pull_to_refresh_pull_label)
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label)
                mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label)
                defaultPullLabel = context.getString(R.string.pull_to_refresh_pull_label)
            }
            else -> {
                lp.gravity =
                    if (scrollDirection === Orientation.VERTICAL) Gravity.BOTTOM else Gravity.RIGHT
                mPullLabel = context.getString(R.string.pull_to_refresh_pull_label)
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label)
                mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label)
                defaultPullLabel = context.getString(R.string.pull_to_refresh_pull_label)
            }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
            val background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground)
            background?.let {
                ViewCompat.setBackground(this, it)
            }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
            val styleID = TypedValue()
            attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID)
            setTextAppearance(styleID.data)
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
            val styleID = TypedValue()
            attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID)
            setSubTextAppearance(styleID.data)
        }

        // Text Color attrs need to be set after TextAppearance attrs
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
            val colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor)
            colors?.let { setTextColor(it) }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
            val colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor)
            colors?.let { setSubTextColor(it) }
        }

        // Try and get defined drawable from Attrs
        var imageDrawable: Drawable? = null
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
            imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable)
        }
        when (mode) {
            Mode.PULL_FROM_END ->
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd)
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom)
                }
            else ->
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart)
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop)
                }
        }

        // If we don't have a user defined drawable, load the default
        if (null == imageDrawable) {
            imageDrawable = context.resources.getDrawable(defaultDrawableResId)
        }

        // Set Drawable, and save width/height
        setLoadingDrawable(imageDrawable)
        reset()
    }

    fun setHeight(height: Int) {
        val lp = layoutParams
        lp.height = height
        requestLayout()
    }

    fun setWidth(width: Int) {
        val lp = layoutParams
        lp.width = width
        requestLayout()
    }

    /**
     * 头部图片不应该算在整个高度中
     * 否则会导致需要下拉到整个内容的顶部才触发刷新
     */
    val contentSize: Int
        get() {
            /**
             * Added By Plucky
             * 头部图片不应该算在整个高度中
             * 否则会导致需要下拉到整个内容的顶部才触发刷新
             */
            val topHeight = ivRefreshTop?.height ?: 0
            return when (mScrollDirection) {
                Orientation.HORIZONTAL -> mInnerLayout.width
                Orientation.VERTICAL -> mInnerLayout.height - topHeight
            }
        }

    fun hideAllViews() {
        mHeaderProgress.inVisible()
        mHeaderImage.inVisible()
        mHeaderText?.let {
            when (it.visibility == View.VISIBLE) {
                true -> it.inVisible()
            }
        }
        mSubHeaderText?.let {
            when (it.visibility == View.VISIBLE) {
                true -> it.inVisible()
            }
        }
    }

    fun onPull(scaleOfLayout: Float) {
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout)
        }
    }

    fun pullToRefresh() {
        mHeaderText?.let {
            it.text = mPullLabel
        }

        // Now call the callback
        pullToRefreshImpl()
    }

    fun refreshing() {
        mHeaderText?.let {
            it.text = mRefreshingLabel
        }
        if (mUseIntrinsicAnimation) {
            if (mHeaderImage.drawable is AnimationDrawable) {
                (mHeaderImage.drawable as AnimationDrawable).start()
            }
        } else {
            // Now call the callback
            refreshingImpl()
        }
        mSubHeaderText?.let {
            it.gone()
        }
    }

    fun releaseToRefresh() {
        mHeaderText?.let {
            it.text = mReleaseLabel
        }

        // Now call the callback
        releaseToRefreshImpl()
    }

    fun reset() {
        mHeaderText?.let {
            it.text = mPullLabel
        }
        mHeaderImage.visible()
        if (mUseIntrinsicAnimation) {
            if (mHeaderImage.drawable is AnimationDrawable) {
                (mHeaderImage.drawable as AnimationDrawable).stop()
            }
        } else {
            // Now call the callback
            resetImpl()
        }
        mSubHeaderText?.let {
            if (it.text.isNullOrBlank()) {
                it.gone()
            } else {
                it.visible()
            }
        }
    }

    override fun setLastUpdatedLabel(label: CharSequence?) {
        setSubHeaderText(label)
    }

    override fun setLoadingDrawable(imageDrawable: Drawable?) {
        // Set Drawable
        mHeaderImage.setImageDrawable(imageDrawable)
        mUseIntrinsicAnimation = imageDrawable is AnimationDrawable

        // Now call the callback
        onLoadingDrawableSet(imageDrawable)
    }

    override fun setPullLabel(pullLabel: CharSequence?) {
        mPullLabel = pullLabel
    }

    override fun resetPullLabel() {
        mPullLabel = defaultPullLabel
    }

    override fun setRefreshingLabel(refreshingLabel: CharSequence?) {
        mRefreshingLabel = refreshingLabel
    }

    override fun setReleaseLabel(releaseLabel: CharSequence?) {
        mReleaseLabel = releaseLabel
    }

    override fun setTextTypeface(tf: Typeface?) {
        mHeaderText?.let {
            it.typeface = tf
        }
    }

    fun showInvisibleViews() {
        mHeaderText?.let {
            when (it.visibility == INVISIBLE) {
                true -> it.visible()
            }
        }
        mSubHeaderText?.let {
            when (it.visibility == INVISIBLE) {
                true -> it.visible()
            }
        }
        if (INVISIBLE == mHeaderProgress.visibility) {
            mHeaderProgress.visibility = VISIBLE
        }
        if (INVISIBLE == mHeaderImage.visibility) {
            mHeaderImage.visibility = VISIBLE
        }
    }

    private fun setSubHeaderText(label: CharSequence?) {
        if (label.isNullOrBlank()) {
            mSubHeaderText?.visibility = GONE
        } else {
            mSubHeaderText?.text = label

            // Only set it to Visible if we're GONE, otherwise VISIBLE will
            // be set soon
            mSubHeaderText?.let {
                when (it.visibility == GONE) {
                    true -> it.visible()
                }
            }
        }
    }

    private fun setSubTextAppearance(value: Int) {
        mSubHeaderText?.setTextAppearance(value)
    }

    private fun setSubTextColor(color: ColorStateList) {
        mSubHeaderText?.setTextColor(color)
    }

    private fun setTextAppearance(value: Int) {
        mHeaderText?.setTextAppearance(value)
        mSubHeaderText?.setTextAppearance(value)
    }

    private fun setTextColor(color: ColorStateList) {
        mHeaderText?.setTextColor(color)
        mSubHeaderText?.setTextColor(color)
    }

    /**
     * Callbacks for derivative Layouts
     */
    abstract val defaultDrawableResId: Int

    abstract fun onLoadingDrawableSet(imageDrawable: Drawable?)
    abstract fun onPullImpl(scaleOfLayout: Float)
    abstract fun pullToRefreshImpl()
    abstract fun refreshingImpl()
    abstract fun releaseToRefreshImpl()
    abstract fun resetImpl()
}