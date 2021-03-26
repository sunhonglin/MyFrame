package com.sunhonglin.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

class ActivityUtil {
    companion object {
        /**
         * Activity 跳转
         *
         * @param context 上下文
         * @param goal    目标activity
         */
        fun skipActivityAndFinish(
            context: Context,
            goal: Class<*>?
        ) {
            val intent = Intent(context, goal)
            context.startActivity(intent)
            (context as Activity).finish()
        }

        /**
         * Activity 跳转
         *
         * @param context 上下文
         * @param goal    目标activity
         * @param bundle  参数
         */
        fun skipActivityAndFinish(
            context: Context,
            goal: Class<*>?,
            bundle: Bundle?
        ) {
            val intent = Intent(context, goal)
            intent.putExtras(bundle!!)
            context.startActivity(intent)
            (context as Activity).finish()
        }


        /**
         * Activity 跳转
         *
         * @param context 上下文
         * @param goal    目标activity
         */
        fun skipActivity(
            context: Context,
            goal: Class<*>?
        ) {
            val intent = Intent(context, goal)
            context.startActivity(intent)
        }

        /**
         * Activity 跳转
         *
         * @param context 上下文
         * @param goal    目标activity
         * @param bundle  参数
         */
        fun skipActivity(
            context: Context,
            goal: Class<*>?,
            bundle: Bundle?
        ) {
            val intent = Intent(context, goal)
            intent.putExtras(bundle!!)
            context.startActivity(intent)
        }

        /**
         * 带请求码跳转
         *
         * @param context     上下文
         * @param goal        目标activity
         * @param requestCode 请求码
         */
        fun skipActivityForResult(
            context: Activity,
            goal: Class<*>?,
            requestCode: Int
        ) {
            val intent = Intent(context, goal)
            context.startActivityForResult(intent, requestCode)
        }

        /**
         * 带请求码跳转
         *
         * @param context     上下文
         * @param goal        目标activity
         * @param bundle      参数
         * @param requestCode 请求码
         */
        fun skipActivityForResult(
            context: Activity,
            goal: Class<*>?,
            bundle: Bundle?,
            requestCode: Int
        ) {
            val intent = Intent(context, goal)
            intent.putExtras(bundle!!)
            context.startActivityForResult(intent, requestCode)
        }

        fun setResultAndFinish(
            context: Activity,
            goal: Class<*>?,
            bundle: Bundle?,
            resultCode: Int
        ) {
            var intent = Intent(context, goal)
            intent.putExtras(bundle!!)
            context.setResult(resultCode, intent)
            context.finish()
        }
    }
}