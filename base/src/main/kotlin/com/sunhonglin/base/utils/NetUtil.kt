package com.sunhonglin.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

enum class NetStatus {
    NONE, MOBILE, WIFI
}

fun Context.getNetWorkState(): NetStatus {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
        return when {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetStatus.WIFI
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetStatus.MOBILE
            else -> NetStatus.NONE
        }
    }
    return NetStatus.NONE

}
