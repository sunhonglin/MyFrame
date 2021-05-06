package com.sunhonglin.base.ui.pulltorefresh.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView.ScaleType
import com.sunhonglin.base.R
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase.Mode
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase.Orientation
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class RotateLoadingLayout(
    context: Context,
    mode: Mode,
    scrollDirection: Orientation,
    attrs: TypedArray
) : LoadingLayout(context, mode, scrollDirection, attrs) {

    companion object {
        const val ROTATION_ANIMATION_DURATION = 1200
    }

    private val mRotateAnimation: Animation
    private var mHeaderImageMatrix: Matrix? = null
    var mRotationPivotX = 0f
    var mRotationPivotY = 0f
    private var mRotateDrawableWhilePulling: Boolean =
        attrs.getBoolean(R.styleable.PullToRefreshBase_ptrRotateDrawableWhilePulling, true)

    init {
        mHeaderImage.scaleType = ScaleType.MATRIX
        mHeaderImageMatrix = Matrix()
        mHeaderImage.imageMatrix = mHeaderImageMatrix
        mRotateAnimation = RotateAnimation(
            0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        mRotateAnimation.interpolator = ANIMATION_INTERPOLATOR
        mRotateAnimation.duration = ROTATION_ANIMATION_DURATION.toLong()
        mRotateAnimation.repeatCount = Animation.INFINITE
        mRotateAnimation.repeatMode = Animation.RESTART
    }


    override fun onLoadingDrawableSet(imageDrawable: Drawable?) {
        if (null != imageDrawable) {
            mRotationPivotX = (imageDrawable.intrinsicWidth / 2f).roundToInt().toFloat()
            mRotationPivotY = (imageDrawable.intrinsicHeight / 2f).roundToInt().toFloat()
        }
    }

    override fun onPullImpl(scaleOfLayout: Float) {
        val angle = if (mRotateDrawableWhilePulling) {
            scaleOfLayout * 90f
        } else {
            max(0f, min(180f, scaleOfLayout * 360f - 180f))
        }
        mHeaderImageMatrix?.setRotate(angle, mRotationPivotX, mRotationPivotY)
        mHeaderImage.imageMatrix = mHeaderImageMatrix
    }

    override fun refreshingImpl() {
        mHeaderImage.startAnimation(mRotateAnimation)
    }

    override fun resetImpl() {
        mHeaderImage.clearAnimation()
        resetImageRotation()
    }

    private fun resetImageRotation() {
        mHeaderImageMatrix?.let {
            it.reset()
            mHeaderImage.imageMatrix = it
        }
    }

    override fun pullToRefreshImpl() {
        // NO-OP
    }

    override fun releaseToRefreshImpl() {
        // NO-OP
    }

    override val defaultDrawableResId: Int
        get() = R.drawable.ic_pull_to_refresh_default_ptr_rotate
}