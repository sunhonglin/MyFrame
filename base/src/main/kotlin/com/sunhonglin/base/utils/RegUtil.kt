package com.sunhonglin.base.utils

import com.sunhonglin.base.utils.ConstUtil.REGEX_MOBILE_EXACT
import com.sunhonglin.base.utils.ConstUtil.REGEX_MOBILE_SIMPLE
import com.sunhonglin.base.utils.ConstUtil.REGEX_URL
import java.util.regex.Pattern

/**
 * string是否匹配regex正则表达式字符串
 */
fun String.isMatch(regex: String): Boolean {
    return !isNullOrEmpty() && Pattern.matches(regex, this)
}

/**
 * 验证手机号（简单）,以1开头十一位数
 */
fun String.isMobileSimple(): Boolean {
    return isMatch(REGEX_MOBILE_SIMPLE)
}

/**
 * 验证手机号（精确），匹配前三位
 */
fun String.isMobileExact(): Boolean {
    return isMatch(REGEX_MOBILE_EXACT)
}

/**
 * 验证URL
 *
 * @return `true`: 匹配<br></br>`false`: 不匹配
 */
fun String.isURL(): Boolean {
    return isMatch(REGEX_URL)
}