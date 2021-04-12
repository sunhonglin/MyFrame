package com.sunhonglin.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.sunhonglin.base.adapter.holder.BaseViewHolder

open class BaseRcvAdapter<T>(
    @LayoutRes private val layoutId: Int,
    private val onBind: ((BaseViewHolder, MutableList<T>, Int) -> Unit)
) : RecyclerView.Adapter<BaseViewHolder>() {
    open val mList = mutableListOf<T>()

    open fun setData(list: MutableList<T>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * 添加数据
     * @param datas 数据
     */
    open fun addData(list: MutableList<T>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    open fun getItem(position: Int): Any? {
        return mList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.create(parent, layoutId, LayoutInflater.from(parent.context)::inflate)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBind(holder, mList, position)
    }
}