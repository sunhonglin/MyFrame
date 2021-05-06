package com.sunhonglin.base.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

fun toGson(obj: Any?): String {
    return Gson().toJson(obj)
}

inline fun <reified T> gsonToBean(jsonStr: String): T? {
    return Gson().fromJson(jsonStr, T::class.java)
}

fun getNodeValue(json: String?, vararg nodeNames: String?): String? {
    if (json.isNullOrEmpty()) return null
    if (nodeNames.isNullOrEmpty()) return json
    var jsonElement = JsonParser.parseString(json)
    jsonElement?.let {
        if (it !is JsonObject) {
            return null
        }
        var jsonObject = it.asJsonObject
        nodeNames.forEachIndexed { index, _ ->
            jsonElement = jsonObject[nodeNames[index]]
            if (jsonElement == null) return null
            if (!jsonElement.isJsonObject) { //当前为非JsonObject
                return when (index) { //如果当前节点获取到的是一个Array并且是最后一个节点，则返回获取到的数据。
                    nodeNames.size - 1 ->
                        if (jsonElement.isJsonArray) {
                            jsonElement.toString()
                        } else {
                            jsonElement.asString
                        }
                    else -> null
                }
            }
            jsonObject = jsonElement.asJsonObject
        }
        return jsonObject.toString()
    }
    return null
}