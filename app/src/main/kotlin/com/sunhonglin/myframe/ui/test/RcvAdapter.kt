package com.sunhonglin.myframe.ui.test

import com.sunhonglin.base.adapter.BaseRcvAdapter
import com.sunhonglin.base.adapter.holder.BaseViewHolder
import com.sunhonglin.myframe.R
import com.sunhonglin.myframe.databinding.LayoutItemTestBinding

class RcvAdapter : BaseRcvAdapter<String>(R.layout.layout_item_test) {

    override fun onBind(holder: BaseViewHolder, item: String?, i: Int) {
        LayoutItemTestBinding.bind(holder.itemView).tvStr.text = item
    }
}