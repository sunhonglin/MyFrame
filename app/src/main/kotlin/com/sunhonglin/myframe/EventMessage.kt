package com.sunhonglin.myframe

import com.sunhonglin.base.entity.EventMessageBase

class EventMessage(
    type: String,
    msg: Any? = null
) : EventMessageBase(type, msg) {
    companion object {
        const val LOGIN_IN = "loginIn"
        const val LOGIN_GO = "goLogin"
        const val PASSWORD_CHANGED = "passwordChanged"
    }
}