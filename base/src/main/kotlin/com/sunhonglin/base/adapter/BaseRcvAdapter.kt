package com.sunhonglin.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.sunhonglin.base.adapter.holder.BaseViewHolder

abstract class BaseRcvAdapter<T>(
    @LayoutRes private val layoutId: Int
) : RecyclerView.Adapter<BaseViewHolder>() {

    var selectedIndex = -1

    open val mList = mutableListOf<T>()

    @SuppressLint("NotifyDataSetChanged")
    open fun setData(list: MutableList<T>?) {
        mList.clear()
        mList.addAll(list ?: mutableListOf())
        notifyDataSetChanged()
    }

    /**
     * 添加数据
     * @param list 数据
     */
    @SuppressLint("NotifyDataSetChanged")
    open fun addData(list: MutableList<T>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    open fun getItem(position: Int): T {
        return mList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.create(parent, layoutId, LayoutInflater.from(parent.context)::inflate)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBind(holder, getItem(position), position)
    }

    abstract fun onBind(holder: BaseViewHolder, item: T, i: Int)
}