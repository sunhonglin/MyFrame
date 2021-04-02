@file:JvmName("Injector")

package com.sunhonglin.myframe.di.login

import com.sunhonglin.myframe.coreComponent
import com.sunhonglin.myframe.ui.login.LoginActivity


/**
 * Injector for [LoginActivity].
 */
fun inject(activity: LoginActivity) {
    DaggerLoginComponent.builder()
        .coreComponent(coreComponent())
        .loginActivity(activity)
        .build()
        .inject(activity)
}