package com.sunhonglin.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

fun defaultJson(): Json {
    return Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}

inline fun <reified T> T.encodeToString(): String {
    return defaultJson().encodeToString(this)
}

fun <T> KSerializer<T>.decodeFromString(str: String?): T? {
    if (str.isNullOrEmpty()) return null
    return defaultJson().decodeFromString(this, str)
}

fun String.getNodeValue(vararg nodeNames: String?): String? {
    if (isNullOrEmpty()) return null
    if (nodeNames.isNullOrEmpty()) return this
    try {
        var jsonElement: JsonElement? = defaultJson().parseToJsonElement(this)
        nodeNames.forEachIndexed { index, _ ->
            jsonElement?.let {
                when (index) {
                    nodeNames.size - 1 -> return it.jsonObject[nodeNames[index]].toString()
                    else -> jsonElement = it.jsonObject[nodeNames[index]]
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}