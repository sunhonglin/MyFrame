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
import com.sunhonglin.base.utils.gone
import com.sunhonglin.base.utils.inVisible
import com.sunhonglin.base.utils.visible
import kotlin.math.max

@SuppressLint("ViewConstructor")
class FlipLoadingLayout(
    context: Context,
    mode: Mode,
    scrollDirection: Orientation,
    attrs: TypedArray
) : LoadingLayout(context, mode, scrollDirection, attrs) {

    private val mRotateAnimation: Animation
    private val mResetRotateAnimation: Animation

    init {
        val rotateAngle = if (mode === Mode.PULL_FROM_START) -180 else 180
        mRotateAnimation = RotateAnimation(
            0f,
            rotateAngle.toFloat(), Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR)
        mRotateAnimation.setDuration(FLIP_ANIMATION_DURATION.toLong())
        mRotateAnimation.setFillAfter(true)
        mResetRotateAnimation = RotateAnimation(
            rotateAngle.toFloat(), 0f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR)
        mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION.toLong())
        mResetRotateAnimation.setFillAfter(true)
    }

    override fun onLoadingDrawableSet(imageDrawable: Drawable?) {
        imageDrawable?.let {
            val dHeight = it.intrinsicHeight
            val dWidth = it.intrinsicWidth

            /**
             * We need to set the width/height of the ImageView so that it is
             * square with each side the size of the largest drawable dimension.
             * This is so that it doesn't clip when rotated.
             */
            val lp = mHeaderImage.layoutParams
            lp.height = max(dHeight, dWidth)
            lp.width = lp.height
            mHeaderImage.requestLayout()
            /**
             * We now rotate the Drawable so that is at the correct rotation,
             * and is centered.
             */
            mHeaderImage.scaleType = ScaleType.MATRIX
            val matrix = Matrix()
            matrix.postTranslate((lp.width - dWidth) / 2f, (lp.height - dHeight) / 2f)
            matrix.postRotate(drawableRotationAngle, lp.width / 2f, lp.height / 2f)
            mHeaderImage.imageMatrix = matrix
        }
    }

    override fun onPullImpl(scaleOfLayout: Float) {
        // NO-OP
    }

    override fun pullToRefreshImpl() {
        // Only start reset Animation, we've previously show the rotate anim
        if (mRotateAnimation === mHeaderImage.animation) {
            mHeaderImage.startAnimation(mResetRotateAnimation)
        }
    }

    override fun refreshingImpl() {
        mHeaderImage.clearAnimation()
        mHeaderImage.inVisible()
        mHeaderProgress.visible()
    }

    override fun releaseToRefreshImpl() {
        mHeaderImage.startAnimation(mRotateAnimation)
    }

    override fun resetImpl() {
        mHeaderImage.clearAnimation()
        mHeaderProgress.gone()
        mHeaderImage.visible()
    }

    override val defaultDrawableResId: Int
        get() = R.drawable.ic_down

    private val drawableRotationAngle: Float
        get() {
            var angle = 0f
            when (mMode) {
                Mode.PULL_FROM_END -> angle = if (mScrollDirection === Orientation.HORIZONTAL) {
                    90f
                } else {
                    180f
                }
                Mode.PULL_FROM_START -> if (mScrollDirection === Orientation.HORIZONTAL) {
                    angle = 270f
                }
                else -> {
                }
            }
            return angle
        }

    companion object {
        const val FLIP_ANIMATION_DURATION = 150
    }
}