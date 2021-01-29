package com.sunhonglin.core.di.shangjia

import com.sunhonglin.core.di.zhainan.ZhaiNan
import dagger.Component

@Component(modules = [ShangjiaAModule::class])
interface WaimaiPintTai {
    fun waimai(): ZhaiNan
}