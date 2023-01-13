package com.sunhonglin.myframe.ui

import android.os.Bundle
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.myframe.databinding.ActivityDetailBinding

class DetailActivity : DefaultToolbarActivity() {
    companion object {
        const val DETAIL = "detail"
    }

    private lateinit var binding: ActivityDetailBinding
    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        binding = ActivityDetailBinding.inflate(layoutInflater, parentBinding.content, true)

        intent.extras?.getString(DETAIL)?.let {
            binding.tvDetail.text = it
        }
    }
}