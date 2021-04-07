package com.sunhonglin.base.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun ImageView.loadImage(
    url: String,
    preload: Boolean = false,
    requestOptions: RequestOptions? = null,
    customTarget: (() -> Target<Bitmap>)? = null
) {
    val requestBuilder = Glide.with(context).asBitmap().load(url)

    requestOptions?.let {
        requestBuilder.apply(it)
    }

    if (preload) {
        requestBuilder.preload()
    }

    if (customTarget != null) {
        requestBuilder.into(customTarget())
    } else {
        requestBuilder.into(this)
    }
}