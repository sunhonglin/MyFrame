package com.sunhonglin.base.interfaces

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

@Scope
@Retention(RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.CLASS)
annotation class BindSkinManager