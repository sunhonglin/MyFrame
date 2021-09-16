//package com.sunhonglin.core.di
//
//import android.content.Context
//import dagger.BindsInstance
//import dagger.Component
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.serialization.json.Json
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Converter
//
///**
// * Component providing application wide singletons.
// * To call this make use of Application.coreComponent or the
// * Activity.coreComponent extension function.
// */
//@Component(modules = [CoreModule::class])
//interface CoreComponent {
//
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun applicationContext(@ApplicationContext context: Context): Builder
//        fun build(): CoreComponent
//    }
//
//    fun providesOkHttpClient(): OkHttpClient
//    fun providesLoggingInterceptor(): HttpLoggingInterceptor
//    fun providesJson(): Json
//    fun providesJsonConverterFactory(): Converter.Factory
//}