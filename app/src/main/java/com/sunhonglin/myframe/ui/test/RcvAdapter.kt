package com.sunhonglin.myframe.ui.test

import com.sunhonglin.base.adapter.BaseRcvAdapter
import com.sunhonglin.base.adapter.holder.BaseViewHolder
import com.sunhonglin.myframe.R
import com.sunhonglin.myframe.databinding.LayoutItemTestBinding

class RcvAdapter(
) : BaseRcvAdapter<String>(R.layout.layout_item_test, ::bind) {

    companion object {
        fun bind(baseViewHolder: BaseViewHolder, mutableList: MutableList<String>, i: Int) {
            LayoutItemTestBinding.bind(baseViewHolder.itemView).tvStr.text = mutableList[i]
        }
    }
}

