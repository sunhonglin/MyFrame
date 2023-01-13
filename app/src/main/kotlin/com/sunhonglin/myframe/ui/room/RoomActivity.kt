package com.sunhonglin.myframe.ui.room

import android.os.Bundle
import androidx.activity.viewModels
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.utils.setDebounceOnClickListener
import com.sunhonglin.core.util.triadOperatorValue
import com.sunhonglin.myframe.data.db.entity.Student
import com.sunhonglin.myframe.databinding.ActivityRoomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomActivity : DefaultToolbarActivity() {
    private lateinit var binding: ActivityRoomBinding
    private val viewModel by viewModels<StudentViewModel>()
    private val adapter = RoomAdapter()

    var list: MutableList<Student>? = null
    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        binding = ActivityRoomBinding.inflate(layoutInflater, parentBinding.content, true)

        viewModel.studentList.observe(this) {
            list = it
            adapter.setData(it)
        }

        binding.apply {
            rcvStudent.adapter = adapter
            tvInsert1.setDebounceOnClickListener {
                addStudent(1)
            }
            tvInsert10.setDebounceOnClickListener {
                addStudent(10)
            }
            tvInsert100.setDebounceOnClickListener {
                addStudent(100)
            }
            tvDelete1.setDebounceOnClickListener {
                removeStudent(1)
            }
            tvDelete10.setDebounceOnClickListener {
                removeStudent(10)
            }
            tvDelete100.setDebounceOnClickListener {
                removeStudent(100)
            }
            tvUpdate1.setDebounceOnClickListener {
                updateStudent(1)
            }
            tvUpdate10.setDebounceOnClickListener {
                updateStudent(10)
            }
            tvUpdate100.setDebounceOnClickListener {
                updateStudent(100)
            }
        }
    }

    private fun addStudent(count: Int) {
        val list = mutableListOf<Student>()
        for (i in 0 until count) {
            val student = Student(
                name = "张三$i",
                age = i,
                sex = when (i % 3) {
                    0 -> 1
                    1 -> 2
                    else -> -1
                },
                autoUpdateTest = i,
                createdTime = "ct$i",
                lastModifiedTime = "lmt$i"
            )
            list.add(student)
        }
        viewModel.insert(list)
    }

    private fun removeStudent(count: Int) {
        list?.let {
            viewModel.delete(it.subList(0, (count > it.size).triadOperatorValue(it.size, count)))
        }
    }

    private fun updateStudent(count: Int) {
        list?.let {
            val updateList = it.subList(0, (count > it.size).triadOperatorValue(it.size, count))
            updateList.forEachIndexed { index, student ->
                student.name = "${student.name}$index"
            }
            viewModel.update(updateList)
        }
    }
}