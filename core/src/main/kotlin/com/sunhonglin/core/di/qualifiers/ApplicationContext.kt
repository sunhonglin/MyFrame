package com.sunhonglin.core.di.qualifiers

import javax.inject.Qualifier

/**
 * A qualifier used to identify the [android.content.Context] dependency is the ApplicationContext,
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext
