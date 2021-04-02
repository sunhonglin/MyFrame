package com.haier.detect.android.core.di.qualifiers

import javax.inject.Qualifier

/**
 * A qualifier used to identify the [android.content.Context] dependency is the ApplicationContext,
 * it is provided by [com.medkey.android.core.di.CoreComponent].
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext
