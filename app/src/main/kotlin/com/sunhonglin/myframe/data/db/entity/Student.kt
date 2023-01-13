package com.sunhonglin.myframe.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Student(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var name: String,

    var age: Int,

    /**
     * 2022-12-30 room迁移测试
     */
    @ColumnInfo(defaultValue = "0")
    var autoUpdateTest: Int,

    /**
     * 性别 1：男性 2：女性 -1、其他：未知
     */
    var sex: Int = -1,

    var createdTime: String,

    var lastModifiedTime: String
) {
    @Ignore
    var ignoreTest = false
}