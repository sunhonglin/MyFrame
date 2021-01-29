package com.sunhonglin.core.di.zhainan

import javax.inject.Inject

class ZhaiNan @Inject constructor() {
    var baoZi: BaoZi? = null
        @Inject set
    var noodle: Noodle? = null
        @Inject set

    fun eat(): StringBuilder {
        var sb = StringBuilder()
        sb.append("我吃的是 ")
        baoZi?.let {
            sb.append(baoZi.toString())
        }
        noodle?.let {
            sb.append("  ")
            sb.append(noodle.toString())
        }
        return sb
    }

    class BaoZi @Inject constructor(){
        override fun toString(): String {
            return "小笼包"
        }
    }

    class Noodle @Inject constructor() {
        override fun toString(): String {
            return "面条"
        }
    }
}

