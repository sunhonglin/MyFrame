package com.sunhonglin.core.data.appUpdate.model

import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "table_app_update_info")
data class AppUpdateInfo(
    var version: String,
    var forceUpdate: Int = 0,
    var description: String,
    var downloadUrl: String,
    var md5: String
)
