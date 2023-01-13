package com.sunhonglin.myframe.ui.room

import android.content.Intent
import com.sunhonglin.base.adapter.BaseRcvAdapter
import com.sunhonglin.base.adapter.holder.BaseViewHolder
import com.sunhonglin.myframe.R
import com.sunhonglin.myframe.data.db.entity.Student
import com.sunhonglin.myframe.databinding.LayoutItemRoomBinding
import com.sunhonglin.myframe.ui.DetailActivity
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class RoomAdapter : BaseRcvAdapter<Student>(R.layout.layout_item_room) {

    private val prettyPrintJson = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            coerceInputValues = true
        }

    override fun onBind(holder: BaseViewHolder, item: Student, position: Int) {
        LayoutItemRoomBinding.bind(holder.itemView).apply {
            tvId.text = item.id.toString()
            tvName.text = item.name
            tvAge.text = item.age.toString()
            tvSex.text = when (item.sex) {
                1 -> tvSex.context.getString(R.string.room_sex_man)
                2 -> tvSex.context.getString(R.string.room_sex_woman)
                else -> tvSex.context.getString(R.string.room_sex_none)
            }
            root.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra(
                    DetailActivity.DETAIL,
                    prettyPrintJson.encodeToString(item)
                )
                it.context.startActivity(intent)
            }
        }
    }
}