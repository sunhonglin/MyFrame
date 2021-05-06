package com.sunhonglin.base.adapter.holder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        internal inline fun create(
            parent: ViewGroup,
            layoutId: Int,
            crossinline inflate: (layoutId: Int, container: ViewGroup, attach: Boolean) -> View
        ) = BaseViewHolder(inflate(layoutId, parent, false))
    }
}