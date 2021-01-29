package com.sunhonglin.myframe.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunhonglin.core.data.db.entity.Student
import com.sunhonglin.myframe.R
import com.sunhonglin.myframe.databinding.LayoutMainItemBinding

class MainAdapter : RecyclerView.Adapter<ViewHolder>() {

    var studentList = mutableListOf<Student>()

    fun setData(list: MutableList<Student>) {
        studentList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvId.text = studentList[position].id.toString()
        holder.tvName.text = studentList[position].name
        holder.tvAge.text = studentList[position].age.toString()
    }
}

class ViewHolder(binding: LayoutMainItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var tvId: TextView = binding.tvId
    var tvName: TextView = binding.tvName
    var tvAge: TextView = binding.tvAge
}