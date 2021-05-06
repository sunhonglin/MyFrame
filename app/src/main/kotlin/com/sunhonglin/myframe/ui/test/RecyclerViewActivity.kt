package com.sunhonglin.myframe.ui.test

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase
import com.sunhonglin.base.ui.pulltorefresh.core.PullToRefreshBase.OnRefreshListener2
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityRecyclerviewTestBinding

class RecyclerViewActivity : DefaultToolbarActivity(), OnRefreshListener2<RecyclerView> {
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

        binding.rcvTest.getRefreshableView().layoutManager = LinearLayoutManager(mContext)
        binding.rcvTest.getRefreshableView().adapter = testAdapter
        binding.rcvTest.setOnRefreshListener(this)
        binding.rcvTest.setScrollingWhileRefreshingEnabled(true)
        binding.rcvTest.backUpView(binding.ivBackTop)

        onPullDownToRefresh(binding.rcvTest)

        binding.rcvTest.getLoadingLayoutProxy().setLastUpdatedLabel("2019年09月24日16:33:17")


        binding.rcvTest.apply {
            mRefreshableView.adapter = testAdapter
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

    var i = 0
    override fun onPullDownToRefresh(refreshView: PullToRefreshBase<RecyclerView>) {
        i = 0
        binding.rcvTest.setMode(PullToRefreshBase.Mode.BOTH)
        testAdapter.setData(getData())
        binding.rcvTest.onRefreshComplete()
    }

    override fun onPullUpToRefresh(refreshView: PullToRefreshBase<RecyclerView>) {
        if (i == 4) {
            binding.rcvTest.showNoMore()
            return
        }
        i ++
        testAdapter.addData(getData())
        binding.rcvTest.onAppendData()
        binding.rcvTest.onRefreshComplete()
    }
}



