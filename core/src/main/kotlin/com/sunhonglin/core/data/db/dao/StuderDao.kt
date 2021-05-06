package com.sunhonglin.core.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sunhonglin.core.data.db.entity.Student

@Dao
interface StudentDao {

    @Insert
    fun insertStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Update
    fun updateStudent(student: Student)

    @Query("SELECT * FROM student")
    fun getStudentList(): LiveData<MutableList<Student>>

    @Query("SELECT * FROM student WHERE id = :id")
    fun getStudentById(id: Int): Student
}