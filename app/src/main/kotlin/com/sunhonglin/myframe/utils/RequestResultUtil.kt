package com.sunhonglin.myframe.utils

import android.content.Context
import com.sunhonglin.base.activity.BaseActivity
import com.sunhonglin.base.utils.showToastInfo
import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.core.data.service.RequestResult
import com.sunhonglin.core.data.service.isSuccessful

fun <T> Context.isRequestResultSuccess(
    result: RequestResult<BaseResponse<T>>?,
    unit: (T?) -> Unit
) {
    when (this) {
        is BaseActivity -> {
            if (isProgressDialogShowing()) dismissProgressDialog()
        }
    }

    when (result) {
        is RequestResult.Success -> {
            if (result.data.isSuccessful()) {
                unit(result.data.data)
            } else {
                showToastInfo(result.data.message)
            }

        }
        is RequestResult.Error -> {
            showToastInfo(result.exception.message)
        }
    }
}