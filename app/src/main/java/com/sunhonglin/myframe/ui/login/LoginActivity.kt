package com.sunhonglin.myframe.ui.login

import android.os.Bundle
import com.sunhonglin.base.activity.BaseActivity
import com.sunhonglin.base.utils.get
import com.sunhonglin.base.utils.setResultAndFinish
import com.sunhonglin.core.data.service.RequestResult
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityLoginBinding
import com.sunhonglin.myframe.di.login.inject
import com.sunhonglin.myframe.ui.home.MainActivity
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin1.setDebounceOnClickListener {
            viewModel.toLogin(userName = "hxbj", password = "123456")
        }

        binding.btnLogin2.setDebounceOnClickListener {
            viewModel.toLogin(userName = "hxbj", password = "123")
        }

        viewModel.login.observe(this) {
            when (it) {
                is RequestResult.Success -> {
                    binding.tvResult.text = "${it.data}"
                }
                is RequestResult.Error -> {
                    binding.tvResult.text = "${it.exception.message}"
                }
            }
        }

        viewModel.toLogin(userName = "hxbj", password = "123456")

        Timber.i("key0 --> ${true}")
        Timber.i("key0 --> ${false}")
        Timber.i("key0 --> ${intent.get<Boolean>("key0")}")
        Timber.i("key1 --> ${intent.get<Float>("key1")}")
        Timber.i("key2 --> ${intent.get<MutableList<String>>("key2")}")

        setResultAndFinish<MainActivity>(mContext,
            "key0" to true,
            "key1" to 1.23F,
            "key2" to listOf("1", "2"),
            resultCode = 2)
    }

    private fun findReplaceType(headers: Map<String, String>) {
//        var call =
//            RequestUtil.builder(LoginService::class.java, BuildConfig.HOST_LOGIN)
//                .findReplaceType(headers)
//
//        call.enqueue(object : Callback<BaseResponse<MutableList<ControllerReplaceType>>> {
//            override fun onFailure(
//                p0: Call<BaseResponse<MutableList<ControllerReplaceType>>>,
//                p1: Throwable
//            ) {
//                dismissProgressDialog()
//            }
//
//            override fun onResponse(
//                p0: Call<BaseResponse<MutableList<ControllerReplaceType>>>,
//                p1: Response<BaseResponse<MutableList<ControllerReplaceType>>>
//            ) {
//                dismissProgressDialog()
//            }
//        })
    }
}
