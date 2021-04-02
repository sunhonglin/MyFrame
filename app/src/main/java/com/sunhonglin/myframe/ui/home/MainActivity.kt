package com.sunhonglin.myframe.ui.home

import android.os.Bundle
import com.sunhonglin.base.activity.BaseActivity
import com.sunhonglin.base.utils.skipActivity
import com.sunhonglin.core.data.db.AppDatabase
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityMainBinding
import com.sunhonglin.myframe.ui.login.LoginActivity

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var appDatabase: AppDatabase
    lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvLogin.setDebounceOnClickListener {
            skipActivity<LoginActivity>(
                mContext,
                "key0" to true,
                "key1" to 1.23F,
                "key2" to listOf("1", "2"),
                requestCode = 2
            )
        }
//
//        appDatabase = AppDatabase.getInstance(this)
//        var studentViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(StudentViewModel::class.java)
//        studentViewModel.getLiveDataStudent().observe(this, Observer<MutableList<Student>> {
//            mainAdapter.setData(it)
//        })
//
//        mainAdapter = MainAdapter()
//        binding.rcvStudents.adapter = mainAdapter
//
//        var index = 0
//        binding.btnInsertStudent.setOnClickListener {
//            InsertStudentTask(appDatabase, "姓名$index", index).execute()
//            index ++
//        }
    }

//
//    class InsertStudentTask(var appDatabase: AppDatabase, var name: String, var age: Int) :
//        AsyncTask<Void?, Void?, Void?>() {
//        override fun doInBackground(vararg arg0: Void?): Void? {
//            appDatabase.studentDao().insertStudent(Student(name = name, age = age))
//            return null
//        }
//    }
}