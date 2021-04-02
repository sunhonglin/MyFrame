package com.sunhonglin.myframe.ui.login

import android.os.Bundle
import com.sunhonglin.base.activity.BaseActivity
import com.sunhonglin.base.utils.DialogUtil
import com.sunhonglin.core.data.service.RequestResult
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityLoginBinding
import com.sunhonglin.myframe.di.login.inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
            binding.tvResult.text = it.toString()
        }

        viewModel.toLogin(userName = "hxbj", password = "123456")
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
