//package com.sunhonglin.myframe.ui.login
//
//import androidx.lifecycle.*
//import com.sunhonglin.core.data.service.RequestResult
//import com.sunhonglin.myframe.data.login.LoginRepository
//import com.sunhonglin.myframe.data.login.model.LoginData
//import kotlinx.coroutines.launch
//
//class LoginViewModel(
//    private val loginRepository: LoginRepository
//) : ViewModel() {
//    private val _login = MutableLiveData<RequestResult<LoginData>>()
//    val login: LiveData<RequestResult<LoginData>>
//        get() = _login
//
//    fun toLogin(
//        userName: String,
//        password: String
//    ) {
//        viewModelScope.launch() {
//            val result = loginRepository.toLogin(
//                userName = userName,
//                password = password
//            )
//            _login.postValue(result)
//        }
//    }
//}