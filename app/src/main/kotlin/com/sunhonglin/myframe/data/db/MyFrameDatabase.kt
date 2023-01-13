package com.sunhonglin.myframe.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sunhonglin.myframe.data.db.dao.StudentDao
import com.sunhonglin.myframe.data.db.entity.Student

@Database(
    version = 2,
    entities = [Student::class],
    exportSchema = true,// 导出schemas
    autoMigrations = [// 自动迁移
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class MyFrameDataBase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}

private const val DATABASE_NAME = "myframe-db"
private var instance: MyFrameDataBase? = null

fun Context.myFrameDataBase(): MyFrameDataBase {
    return instance ?: synchronized(this) {
        Room.databaseBuilder(
            applicationContext,
            MyFrameDataBase::class.java,
            DATABASE_NAME
        ).build().also {
            instance = it
        }
    }
}