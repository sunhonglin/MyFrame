package com.sunhonglin.base.ui.pulltorefresh.inn

import android.graphics.Typeface
import android.graphics.drawable.Drawable

interface ILoadingLayout {

    /**
     * 设置子标题显示内容
     * 一般为 最后一次更新时间
     * @param label 显示内容
     */
    fun setLastUpdatedLabel(label: CharSequence?)

    /**
     * Set the drawable used in the loading layout. This is the same as calling
     * `setLoadingDrawable(drawable, Mode.BOTH)`
     *
     * @param drawable - Drawable to display
     */
    fun setLoadingDrawable(drawable: Drawable?)

    /**
     * Set Text to show when the Widget is being Pulled
     * `setPullLabel(releaseLabel, Mode.BOTH)`
     *
     * @param pullLabel - CharSequence to display
     */
    fun setPullLabel(pullLabel: CharSequence?)


    /**
     * Added By Plucky
     * 重置PullLabel
     */
    fun resetPullLabel()

    /**
     * Set Text to show when the Widget is refreshing
     * `setRefreshingLabel(releaseLabel, Mode.BOTH)`
     *
     * @param refreshingLabel - CharSequence to display
     */
    fun setRefreshingLabel(refreshingLabel: CharSequence?)

    /**
     * Set Text to show when the Widget is being pulled, and will refresh when
     * released. This is the same as calling
     * `setReleaseLabel(releaseLabel, Mode.BOTH)`
     *
     * @param releaseLabel - CharSequence to display
     */
    fun setReleaseLabel(releaseLabel: CharSequence?)

    /**
     * Set's the Sets the typeface and style in which the text should be
     * displayed. Please see
     * [ TextView#setTypeface(Typeface)][android.widget.TextView.setTypeface].
     */
    fun setTextTypeface(tf: Typeface?)
}