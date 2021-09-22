package com.sunhonglin.base.entity

open class EventMessageBase(
    val type: String,
    val msg: Any? = null
) {
    companion object {
        const val TYPE_NET = "net" //网络状态
    }
}