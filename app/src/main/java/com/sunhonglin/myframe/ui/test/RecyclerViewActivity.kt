package com.sunhonglin.myframe.ui.test

import android.os.Bundle
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.adapter.BaseRcvAdapter
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.R
import com.sunhonglin.myframe.databinding.ActivityRecyclerviewTestBinding
import com.sunhonglin.myframe.databinding.LayoutItemTestBinding

class RecyclerViewActivity : DefaultToolbarActivity() {
    lateinit var binding: ActivityRecyclerviewTestBinding
    lateinit var testAdapter: RcvAdapter

    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        setStatusBarMode(StatusBarMode.LIGHT)

        binding =
            ActivityRecyclerviewTestBinding.inflate(layoutInflater, parentBinding.content, true)

        testAdapter = RcvAdapter()

        binding.rcvTest.apply {
            adapter = testAdapter
            testAdapter.setData(getData())
        }

        binding.btnNext.setDebounceOnClickListener {
            testAdapter.addData(getData())
        }
    }

    private fun getData(): MutableList<String> {
        var list = mutableListOf<String>()
        for (i in 0..100) {
            list.add("test $i")
        }
        return list
    }
}



