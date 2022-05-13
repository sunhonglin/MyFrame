package com.sunhonglin.myframe.ui.login

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.utils.get
import com.sunhonglin.base.utils.loadImage
import com.sunhonglin.core.util.encodeToString
import com.sunhonglin.core.util.isRequestResultSuccess
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : DefaultToolbarActivity() {
    lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        setStatusBarMode(StatusBarMode.LIGHT)

        binding = ActivityLoginBinding.inflate(layoutInflater, parentBinding.content, true)

        binding.btnLogin1.setDebounceOnClickListener {
            showProgressDialog()
            viewModel.toLogin(userName = "hxbj", password = "123456")
        }

        binding.btnLogin2.setDebounceOnClickListener {
            showProgressDialog()
            viewModel.toLogin(userName = "hxbj", password = "123")
        }

        viewModel.login.observe(this) {
            isRequestResultSuccess(it) { result ->
                result?.let { loginData ->
                    binding.tvResult.text = loginData.encodeToString()
                }
            }
        }

        showProgressDialog()
        viewModel.toLogin(userName = "hxbj", password = "123456")

        Timber.i("key0 --> ${true}")
        Timber.i("key0 --> ${false}")
        Timber.i("key0 --> ${intent.get<Boolean>("key0")}")
        Timber.i("key1 --> ${intent.get<Float>("key1")}")
        Timber.i("key2 --> ${intent.get<MutableList<String>>("key2")}")


        val requestOptions = RequestOptions.circleCropTransform()
        binding.ivGlide.loadImage(
            "https://t7.baidu.com/it/u=612028266,626816349&fm=193&f=GIF",
            false,
            requestOptions
        ) {
            object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Timber.i("----------------> onResourceReady")
                    binding.ivGlide.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Timber.i("----------------> onLoadCleared")
                }
            }
        }
    }


//    private fun findReplaceType(headers: Map<String, String>) {
////        var call =
////            RequestUtil.builder(LoginService::class.java, BuildConfig.HOST_LOGIN)
////                .findReplaceType(headers)
////
////        call.enqueue(object : Callback<BaseResponse<MutableList<ControllerReplaceType>>> {
////            override fun onFailure(
////                p0: Call<BaseResponse<MutableList<ControllerReplaceType>>>,
////                p1: Throwable
////            ) {
////                dismissProgressDialog()
////            }
////
////            override fun onResponse(
////                p0: Call<BaseResponse<MutableList<ControllerReplaceType>>>,
////                p1: Response<BaseResponse<MutableList<ControllerReplaceType>>>
////            ) {
////                dismissProgressDialog()
////            }
////        })
//    }
}


