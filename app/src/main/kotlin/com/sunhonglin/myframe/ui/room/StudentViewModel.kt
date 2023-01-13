package com.sunhonglin.myframe.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunhonglin.core.di.CoroutinesDispatcherProvider
import com.sunhonglin.myframe.data.db.entity.Student
import com.sunhonglin.myframe.data.student.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    val studentList = studentRepository.getList()

    fun insert(list: MutableList<Student>) {
        viewModelScope.launch(dispatcherProvider.io) {
            studentRepository.insert(list)
        }
    }

    fun delete(list: MutableList<Student>) {
        viewModelScope.launch(dispatcherProvider.io) {
            studentRepository.delete(list)
        }
    }

    fun update(list: MutableList<Student>) {
        viewModelScope.launch(dispatcherProvider.io) {
            studentRepository.update(list)
        }
    }
}