package com.sunhonglin.base.utils
//
//import android.content.Context
//import android.content.SharedPreferences
//import com.sunhonglin.base.BaseApplication
//
//class SpUtil {
//    companion object {
//        private var spName = "sp_values"
//        private var editor: SharedPreferences.Editor
//
//        private var sp: SharedPreferences = BaseApplication.app.getSharedPreferences(
//            spName,
//            Context.MODE_PRIVATE
//        )
//
//        init {
//            editor = sp.edit()
//        }
//
//        /**
//         * SP中写入String类型value
//         *
//         * @param key   键
//         * @param value 值
//         */
//        fun putString(key: String, value: String) {
//            editor.putString(key, value)
//            editor.apply()
//        }
//
//        /**
//         * SP中读取String
//         *
//         * @param key 键
//         * @return 存在返回对应值，不存在返回空字符串
//         */
//        fun getString(key: String): String {
//            return getString(key, "")
//        }
//
//        /**
//         * SP中读取String
//         *
//         * @param key      键
//         * @param defValue 默认值
//         * @return 存在返回对应值，不存在返回默认值defValue
//         */
//        fun getString(key: String, defValue: String): String {
//            return sp.getString(key, defValue)!!
//        }
//
//        /**
//         * SP中写入int类型value
//         *
//         * @param key   键
//         * @param value 值
//         */
//        fun putInt(key: String, value: Int) {
//            editor.putInt(key, value)
//            editor.apply()
//        }
//
//        /**
//         * SP中读取int
//         *
//         * @param key 键
//         * @return 存在返回对应值，不存在返回默认值-1
//         */
//        fun getInt(key: String): Int {
//            return sp.getInt(key, -1)
//        }
//
//        /**
//         * SP中读取int
//         *
//         * @param key      键
//         * @param defValue 默认值
//         * @return 存在返回对应值，不存在返回默认值defValue
//         */
//        fun getInt(key: String, defValue: Int): Int {
//            return sp.getInt(key, defValue)
//        }
//
//        /**
//         * SP中写入long类型value
//         *
//         * @param key   键
//         * @param value 值
//         */
//        fun putLong(key: String, value: Long) {
//            editor.putLong(key, value)
//            editor.apply()
//        }
//
//        /**
//         * SP中读取long
//         *
//         * @param key 键
//         * @return 存在返回对应值，不存在返回默认值-1
//         */
//        fun getLong(key: String): Long {
//            return sp.getLong(key, -1L)
//        }
//
//        /**
//         * SP中读取long
//         *
//         * @param key      键
//         * @param defValue 默认值
//         * @return 存在返回对应值，不存在返回默认值defValue
//         */
//        fun getLong(key: String, defValue: Long): Long {
//            return sp.getLong(key, defValue)
//        }
//
//        /**
//         * SP中写入float类型value
//         *
//         * @param key   键
//         * @param value 值
//         */
//        fun putFloat(key: String, value: Float) {
//            editor.putFloat(key, value)
//            editor.apply()
//        }
//
//        /**
//         * SP中读取float
//         *
//         * @param key 键
//         * @return 存在返回对应值，不存在返回默认值-1F
//         */
//        fun getFloat(key: String): Float {
//            return sp.getFloat(key, -1f)
//        }
//
//        /**
//         * SP中读取float
//         *
//         * @param key      键
//         * @param defValue 默认值
//         * @return 存在返回对应值，不存在返回默认值defValue
//         */
//        fun getFloat(key: String, defValue: Float): Float {
//            return sp.getFloat(key, defValue)
//        }
//
//        /**
//         * SP中写入boolean类型value
//         *
//         * @param key   键
//         * @param value 值
//         */
//        fun putBoolean(key: String, value: Boolean) {
//            editor.putBoolean(key, value)
//            editor.apply()
//        }
//
//        /**
//         * SP中读取boolean
//         *
//         * @param key      键
//         * @param defValue 默认值
//         * @return 存在返回对应值，不存在返回默认值defValue
//         */
//        fun getBoolean(key: String, defValue: Boolean): Boolean {
//            return sp.getBoolean(key, defValue)
//        }
//
//        /**
//         * SP中移除该key
//         *
//         * @param key 键
//         */
//        fun remove(key: String) {
//            editor.remove(key).apply()
//        }
//    }
//}