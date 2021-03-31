package com.sunhonglin.myframe.ui.login

import android.os.Bundle
import com.sunhonglin.base.activity.BaseActivity
import com.sunhonglin.base.utils.DialogUtil
import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.core.data.service.RequestUtil
import com.sunhonglin.core.data.service.isSuccessful
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.BuildConfig
import com.sunhonglin.myframe.data.api.LoginService
import com.sunhonglin.myframe.data.login.model.ControllerReplaceType
import com.sunhonglin.myframe.data.login.model.LoginData
import com.sunhonglin.myframe.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin1.setDebounceOnClickListener {
            login("hxbj", "123456")
        }

        binding.btnLogin2.setDebounceOnClickListener {
            login("hx", "123")
        }

        login("hxbj", "123456")
    }

    private fun login(userName: String, password: String) {
        showProgressDialog()
        var map =
            mapOf(
                "userName" to userName,
                "password" to password
            )
        var call =
            RequestUtil.builder(LoginService::class.java, BuildConfig.HOST_LOGIN).toLogin(map)

        call.enqueue(object : Callback<BaseResponse<LoginData>> {
            override fun onFailure(p0: Call<BaseResponse<LoginData>>, p1: Throwable) {
                dismissProgressDialog()
                binding.tvResult.text = p1.message.toString()
            }

            override fun onResponse(
                p0: Call<BaseResponse<LoginData>>,
                p1: Response<BaseResponse<LoginData>>
            ) {
                p1.body()?.let { body ->
                    binding.tvResult.text = body.toString()

                    if (body.isSuccessful()) {
                        body.result.let { result ->
                            findReplaceType(mapOf(
                                "Authorization" to result.token,
                                "loginId" to result.userName
                            ))
                        }
                    } else {
                        dismissProgressDialog()
                        DialogUtil.showTipDialog(mContext, body.message)
                    }
                }
            }
        })
    }

    private fun findReplaceType(headers : Map<String, String>) {
        var call =
            RequestUtil.builder(LoginService::class.java, BuildConfig.HOST_LOGIN).findReplaceType(headers)

        call.enqueue(object : Callback<BaseResponse<MutableList<ControllerReplaceType>>> {
            override fun onFailure(
                p0: Call<BaseResponse<MutableList<ControllerReplaceType>>>,
                p1: Throwable
            ) {
                dismissProgressDialog()
            }

            override fun onResponse(
                p0: Call<BaseResponse<MutableList<ControllerReplaceType>>>,
                p1: Response<BaseResponse<MutableList<ControllerReplaceType>>>
            ) {
                dismissProgressDialog()
            }
        })
    }
}
