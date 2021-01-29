package com.sunhonglin.core.di.shangjia

import com.sunhonglin.core.di.zhainan.ZhaiNan
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class ShangjiaAModule {
    @Provides
    fun provideBaozi(): Baozi {
        return Baozi("豆沙包")
    }
    @Provides
    fun provideMoodl(): ZhaiNan.Noodle {
        return ZhaiNan.Noodle()
    }
}

class Baozi @Inject constructor(var name: String) {
    override fun toString(): String {
        return name
    }
}

class Noodle @Inject constructor() {

}