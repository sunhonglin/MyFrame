package com.sunhonglin.myframe.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunhonglin.core.data.service.CoroutinesDispatcherProvider
import com.sunhonglin.myframe.data.login.LoginRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = loginRepository,
                dispatcherProvider = dispatcherProvider
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}