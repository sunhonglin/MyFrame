package com.sunhonglin.base.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

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

fun Application.initNetListener(unit: (netStatus: NetStatus) -> Unit) {
    val builder = NetworkRequest.Builder().build()
    val systemService = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    systemService.registerNetworkCallback(builder, object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            unit(getNetWorkState())
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            unit(getNetWorkState())
        }
    })
}
