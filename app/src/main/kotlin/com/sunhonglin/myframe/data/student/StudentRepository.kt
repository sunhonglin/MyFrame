package com.sunhonglin.myframe.data.student

import androidx.lifecycle.LiveData
import com.sunhonglin.myframe.data.db.dao.StudentDao
import com.sunhonglin.myframe.data.db.entity.Student
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val studentDao: StudentDao
) {

    suspend fun insert(list: MutableList<Student>) {
        studentDao.insert(list)
    }

    suspend fun delete(list: MutableList<Student>) {
        studentDao.delete(list)
    }

    suspend fun update(list: MutableList<Student>) = studentDao.update(list)

    fun getList(): LiveData<MutableList<Student>> = studentDao.getList()

    suspend fun getById(id: Int): Student {
        return studentDao.getById(id)
    }
}