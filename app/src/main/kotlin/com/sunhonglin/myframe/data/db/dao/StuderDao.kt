package com.sunhonglin.myframe.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sunhonglin.myframe.data.db.entity.Student

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(list: MutableList<Student>)

    @Delete
    suspend fun delete(list: MutableList<Student>)

    @Update
    suspend fun update(list: MutableList<Student>): Int

    @Query("SELECT * FROM student")
    fun getList(): LiveData<MutableList<Student>>

    @Query("SELECT * FROM student WHERE id = :id")
    suspend fun getById(id: Int): Student
}