package com.sunhonglin.myframe.ui.test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.utils.ActivityUtil.activityResultDefaultContracts
import com.sunhonglin.base.utils.ActivityUtil.multiplePermissionContractsPass
import com.sunhonglin.base.utils.ActivityUtil.permissionContracts
import com.sunhonglin.base.utils.ToastUtil.showToast
import com.sunhonglin.base.utils.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityRegisterForActivityResultBinding

class RegisterForActivityResultTestActivity : DefaultToolbarActivity() {
    private lateinit var binding: ActivityRegisterForActivityResultBinding

    private val requestPermission = permissionContracts { granted ->
        if (granted) {
            showToast("Permission Granted.")
        } else {
            showToast("Permission Denied.")
        }
    }

//    private val requestMultiplePermission = multiplePermissionContracts { map ->
//        var toastInfo = ""
//        map.forEach {
//            toastInfo += "${it.key}:${it.value}\n"
//        }
//        when {
//            map.filter { !it.value }.isNotEmpty() -> showToast("Permission Denied.\n$toastInfo")
//            else -> showToast("Permission Granted.")
//        }
//    }

    private val requestMultiplePermission = multiplePermissionContractsPass { granted ->
        if (granted) {
            showToast("Permission Granted.")
        } else {
            showToast("Permission Denied.")
        }
    }

    private val activityResultByCustomerContract =
        registerForActivityResult(RegisterForActivityResultTestResultActivity.Contract()) { result ->
            showToast(result)
        }

    private val activityResultByDefaultContract = activityResultDefaultContracts { result ->
        when (result.resultCode) {
            RESULT_OK -> showToast("${result.data?.getStringExtra("KEY_RESULT")}Data has been updated successfully")
            else -> showToast("${result.data?.getStringExtra("KEY_RESULT")}Failed to update data")
        }
    }

    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        binding = ActivityRegisterForActivityResultBinding.inflate(
            layoutInflater,
            parentBinding.content,
            true
        )

        binding.tvRequestPermission.setDebounceOnClickListener {
            requestPermission.launch(Manifest.permission.READ_CONTACTS)
        }

        binding.tvRequestMultiplePermissions.setDebounceOnClickListener {
            requestMultiplePermission.launch(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CAMERA
                )
            )
        }

        binding.tvCustomerContract.setDebounceOnClickListener {
            activityResultByCustomerContract.launch("FROM RegisterForActivityResultTest ACTIVITY ")
        }

        binding.tvDefaultContract.setDebounceOnClickListener {
            activityResultByDefaultContract.launch(
                Intent(
                    this,
                    RegisterForActivityResultTestResultActivity::class.java
                )
            )
        }


    }
}