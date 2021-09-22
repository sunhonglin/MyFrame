package com.sunhonglin.myframe.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sunhonglin.core.data.db.AppDatabase
import com.sunhonglin.core.data.db.entity.Student


class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(application)
    private val liveDataStudent: LiveData<MutableList<Student>> = appDatabase.studentDao().getStudentList()

    fun getLiveDataStudent(): LiveData<MutableList<Student>> {
        return liveDataStudent
    }
}