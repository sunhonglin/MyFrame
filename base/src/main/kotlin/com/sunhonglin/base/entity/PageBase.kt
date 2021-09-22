package com.sunhonglin.base.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
open class PageBase {
    val current: Int = 0
    val pages: Int = 0
    val searchCount: Boolean = false
    val size: Int = 0
    val total: Int = 0
}