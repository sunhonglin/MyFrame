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

///**
// * 多图片预加载
// *
// * @param context
// * @param url
// */
//fun loadPreload(context: Context, url: Array<String?>) {
//    for (index in url) {
//        loadImage(context, index, null, null, null, true)
//    }
//}
//
///**
// * 加载圆形图片
// *
// * @param context
// * @param url
// * @param imageView
// */
//fun loadCircleImg(context: Context, url: String?, imageView: ImageView?) {
//    val options = RequestOptions()
//        .circleCrop()
//    loadImage(context, url, imageView, options, null, false)
//}
//
///**
// * 加载图片 - 带有占位图和错误加载图
// *
// * @param context
// * @param url
// * @param holderId
// * @param errorId
// * @param imageView
// */
//fun loadImgAndDefault(
//    context: Context,
//    url: String?,
//    holderId: Int,
//    errorId: Int,
//    imageView: ImageView?
//) {
//    val options = RequestOptions()
//        .placeholder(holderId)
//        .error(errorId)
//    loadImage(context, url, imageView, options, null, false)
//}
//
///**
// * 加载图片  -  带回调
// *
// * @param context
// * @param url
// * @param simpleTarget
// */
//fun loadImgByTarget(context: Context, url: String?, simpleTarget: SimpleTarget<BitmapDrawable?>?) {
//    loadImage(context, url, null, null, simpleTarget, false)
//}
//
///**
// * 加载图片 - 带大小
// *
// * @param context
// * @param url
// * @param width
// * @param height
// * @param imageView
// */
//fun loadImgAndSize(context: Context, url: String, width: Int, height: Int, imageView: ImageView) {
//    val options = RequestOptions()
//        .override(width, height)
//    loadImage(context, url, imageView, options, null, false)
//}
//
///**
// * 加载图片 - 不加入缓存（防止链接相同图片不更新问题）
// *
// * @param context
// * @param url
// * @param imageView
// */
//fun loadImgSkipMemory(context: Context, url: String, imageView: ImageView) {
//    val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//    loadImage(context, url, imageView, options, null, false)
//}