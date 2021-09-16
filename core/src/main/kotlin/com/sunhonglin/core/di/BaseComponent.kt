//package com.sunhonglin.core.di
//
//
//import android.app.Service
//import androidx.activity.ComponentActivity
//import androidx.fragment.app.Fragment
//
//interface BaseComponent<T> {
//    fun inject(target: T)
//}
//
///**
// * Base dagger component for use in activities.
// */
//interface BaseActivityComponent<T : ComponentActivity> : BaseComponent<T>
//
///**
// * Base dagger components for use in services.
// */
//interface BaseServiceComponent<T : Service> : BaseComponent<T>
//
///**
// *  Base dagger components for use in fragments
// */
//interface BaseFragmentComponent<T : Fragment> : BaseComponent<T>