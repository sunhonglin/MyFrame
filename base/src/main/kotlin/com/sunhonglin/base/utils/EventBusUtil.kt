package com.sunhonglin.base.utils

import org.greenrobot.eventbus.EventBus

class EventBusUtil {
    companion object {
        fun register(subscriber: Any?) {
            EventBus.getDefault().register(subscriber)
        }

        fun unregister(subscriber: Any?) {
            EventBus.getDefault().unregister(subscriber)
        }

        fun sendEvent(event: Any?) {
            EventBus.getDefault().post(event)
        }

        fun sendStickyEvent(event: Any?) {
            EventBus.getDefault().postSticky(event)
        }
    }
}