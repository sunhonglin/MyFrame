package com.sunhonglin.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunhonglin.core.data.api.AppUpdateService
import com.sunhonglin.core.data.appUpdate.AppUpdateRepository
import com.sunhonglin.core.data.appUpdate.model.AppUpdateInfo
import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.core.data.service.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val appUpdateRepository: AppUpdateRepository
) : ViewModel() {

    private val _testLiveData = MutableLiveData<BaseResponse<AppUpdateInfo>>()
    val testLiveData: LiveData<BaseResponse<AppUpdateInfo>>
        get() = _testLiveData

    fun toTest() {
        viewModelScope.launch {
            val result =
                appUpdateRepository.toAppUpdate("com.langcoo.deviceUpdate", "1.0.0", "official")
            when (result) {
                is RequestResult.Success -> _testLiveData.postValue(result.data)
            }


        }
    }
}

