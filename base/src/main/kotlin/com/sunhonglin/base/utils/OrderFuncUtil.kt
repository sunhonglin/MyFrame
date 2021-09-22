package com.sunhonglin.base.utils

import java.util.LinkedList

object OrderFuncUtil {

    private var queueFun = LinkedList<Function>()

    private var currentFun: (() -> Unit?)? = null// 当前任务

    /**
     * 添加任务到末尾
     */
    fun addFunc(function: () -> Unit?) {
        val func = Function(function)
        doFunc(func)
    }

    /**
     * 在指定位置添加任务
     * 如果index = 0 会打断当前任务，立即执行插入任务
     * addCurrent 是否将当前任务恢复，用于当前任务有前置条件的情况
     */
    fun addFuncIndex(index:Int, function: () -> Unit?, addCurrent: Boolean) {
        if (addCurrent && currentFun != null) {
            queueFun.add(0, Function(currentFun!!))
        }

        queueFun.add(index, Function(function))

        if (index == 0) {
            val funNow = queueFun.poll()
            currentFun = funNow?.function
            currentFun?.invoke()
        }
    }

    /**
     * 结束当前任务
     */
    private fun finishFunc() {
        doFunc(null)
    }

    /**
     * 结束所有任务
     */
    fun finishAll() {
        queueFun.clear()
        finishFunc()
    }

    private fun doFunc(func: Function?) {
        if (func != null) {
            queueFun.offer(func)
        } else {
            currentFun = null
        }
        if (currentFun == null) {
            if (queueFun.size != 0) {
                val funNow = queueFun.poll()
                currentFun = funNow?.function
                currentFun?.invoke()
            }
        }
    }

    fun hasNext(): Boolean {
        return queueFun.size > 0
    }


    data class Function(var function: () -> Unit? = {})
}