package com.sunhonglin.base.utils

import android.content.Context
import com.mylhyl.acp.Acp
import com.mylhyl.acp.AcpListener
import com.mylhyl.acp.AcpOptions

interface PermissionListener {
    fun onGranted()
    fun onDenied(permissions: List<String?>?)
}

/**
 * 动态请求权限
 * @param listener 回调
 * @param list 需要申请的权限
 */
fun Context.requestPermissions(
    listener: PermissionListener? = null,
    vararg list: String
) {
    Acp.getInstance(this)
        .request(AcpOptions.Builder().setPermissions(*list).build(), object : AcpListener {
            override fun onGranted() {
                listener?.onGranted()
            }

            override fun onDenied(permissions: List<String?>?) {
                listener?.onDenied(permissions)
            }
        })
}