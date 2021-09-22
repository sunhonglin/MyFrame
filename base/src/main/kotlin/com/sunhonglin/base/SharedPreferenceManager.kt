package com.sunhonglin.base

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity

interface SharedPreferenceManager {

    fun pullStringSharedPreference(key: String): String?

    fun pushStringSharedPreference(key: String, value: String)

}

class DefaultSharedPreferenceManager(private val activity: FragmentActivity) :
    SharedPreferenceManager {
    private val sharedPreferences: SharedPreferences by lazy {
        activity.getSharedPreferences("com_sunhonglin_frame", Context.MODE_PRIVATE)
    }


    override fun pullStringSharedPreference(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun pushStringSharedPreference(key: String, value: String) {
        val edit = sharedPreferences.edit()
        edit.putString(key, value)
        edit.apply()
    }
}