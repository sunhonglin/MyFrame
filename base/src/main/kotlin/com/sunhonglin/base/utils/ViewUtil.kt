package com.sunhonglin.base.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import dagger.hilt.android.internal.managers.ViewComponentManager

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.changeVisible(visible: Boolean) {
    when (visible) {
        true -> visible()
        else -> gone()
    }
}

fun View.fixedContext(): Context {
    return when (context) {
        is ViewComponentManager.FragmentContextWrapper -> (context as ViewComponentManager.FragmentContextWrapper).baseContext
        else -> context
    }
}

fun View.hideInputKeyBoard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}