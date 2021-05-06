package com.sunhonglin.base.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {
    companion object {
        private const val DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"

        /**
         * 获取SimpleDateFormat
         *
         * @param pattern
         * @return 默认返回yyyy-MM-dd HH:mm:ss格式。
         */
        private fun simpleDateFormat(pattern: String?): SimpleDateFormat {
            var p = pattern
            if (TextUtils.isEmpty(p)) {
                p = DEFAULT_PATTERN
            }
            return SimpleDateFormat(p, Locale.getDefault())
        }

        /**
         * 获取当前时间
         *
         * 格式为用户自定义
         *
         * @param format 时间格式
         * @return 时间字符串
         */
        fun curTimeString(format: String?): String {
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
            else simpleDateFormat(pattern).format(date)
        }

        private var mExitTime = 0L
        /**
         * 双击返回退出
         *
         * @param timeDiff 时间差
         */
        fun doubleClickExit(timeDiff: Long): Boolean {
            if (System.currentTimeMillis() - mExitTime > timeDiff) {
                mExitTime = System.currentTimeMillis()
                return false
            }
            return true
        }
    }
}