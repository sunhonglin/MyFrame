package com.sunhonglin.myframe.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunhonglin.core.di.CoroutinesDispatcherProvider
import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.core.data.service.RequestResult
import com.sunhonglin.myframe.data.login.LoginRepository
import com.sunhonglin.myframe.data.login.model.LoginData
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {
    private val _login = MutableLiveData<RequestResult<BaseResponse<LoginData>>>()
    val login: LiveData<RequestResult<BaseResponse<LoginData>>>
        get() = _login

    fun toLogin(
        userName: String,
        password: String
    ) {

        viewModelScope.launch(dispatcherProvider.io) {
            val result = loginRepository.toLogin(
                userName = userName,
                password = password
            )
            _login.postValue(result)
        }
    }
}