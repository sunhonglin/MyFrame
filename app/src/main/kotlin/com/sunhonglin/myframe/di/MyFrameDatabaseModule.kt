package com.sunhonglin.myframe.di

import android.app.Application
import com.sunhonglin.myframe.data.db.MyFrameDataBase
import com.sunhonglin.myframe.data.db.myFrameDataBase
import com.sunhonglin.myframe.data.db.dao.StudentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MyFrameDatabaseModule {

    @Provides
    @Singleton
    fun providesMyFrameDataBase(
        context: Application
    ): MyFrameDataBase {
        return context.myFrameDataBase()
    }

    @Provides
    @Singleton
    fun providesStudentDao(
        myFrameDataBase: MyFrameDataBase
    ): StudentDao {
        return myFrameDataBase.studentDao()
    }
}