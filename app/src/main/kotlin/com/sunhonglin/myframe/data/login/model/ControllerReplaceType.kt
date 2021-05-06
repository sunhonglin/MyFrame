package com.sunhonglin.myframe.data.login.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class ControllerReplaceType(
    var id: String,
    var type: String
)