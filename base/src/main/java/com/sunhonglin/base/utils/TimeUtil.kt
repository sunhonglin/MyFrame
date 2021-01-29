package com.sunhonglin.base.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {
    companion object {
        private val DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"

        /**
         * 获取SimpleDateFormat
         *
         * @param pattern
         * @return 默认返回yyyy-MM-dd HH:mm:ss格式。
         */
        private fun getSimpleDateFormat(pattern: String?): SimpleDateFormat {
            var p = pattern
            if (TextUtils.isEmpty(p)) {
                p = DEFAULT_PATTERN
            }
            return SimpleDateFormat(p)
        }

        /**
         * 获取当前时间
         *
         * 格式为用户自定义
         *
         * @param format 时间格式
         * @return 时间字符串
         */
        fun getCurTimeString(format: String?): String {
            return date2String(Date(), format)
        }

        /**
         * 将date转换成format格式的日期
         *
         * @param pattern 格式
         * @param date   日期
         * @return
         */
        fun date2String(date: Date?, pattern: String?): String {
            return if (date == null) ""
            else getSimpleDateFormat(pattern).format(date)
        }
    }
}