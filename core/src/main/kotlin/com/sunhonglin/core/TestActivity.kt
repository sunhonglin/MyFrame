package com.sunhonglin.core

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sunhonglin.core.databinding.ActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

    private val viewModel: TestViewModel by viewModels()

    lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.testLiveData.observe(this) {
            it?.let { res ->
                binding.tvInfo.text = res.toString()
            }
        }

        viewModel.toTest()
    }
}