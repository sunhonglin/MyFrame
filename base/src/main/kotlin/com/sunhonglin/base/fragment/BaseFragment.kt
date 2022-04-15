package com.sunhonglin.base.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sunhonglin.base.ProgressDialogManager
import com.sunhonglin.base.UiThreadExecutor

open class BaseFragment constructor(
    uiThreadExecutor: UiThreadExecutor,
    progressDialogManager: ProgressDialogManager
) : Fragment(),
    UiThreadExecutor by uiThreadExecutor,
    ProgressDialogManager by progressDialogManager {

    companion object {
        private const val KEY_FRAGMENT_HIDDEN: String = "key_fragment_hidden"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(KEY_FRAGMENT_HIDDEN)) {
                parentFragmentManager.beginTransaction().hide(this).commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_FRAGMENT_HIDDEN, isHidden)
    }

    protected inline fun <reified T> Context.requiredImplement(): T {
        return this as? T
            ?: throw ClassCastException("$this must implement ${T::class.simpleName}")
    }
}
