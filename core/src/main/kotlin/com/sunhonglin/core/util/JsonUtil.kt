package com.sunhonglin.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

fun defaultJson(): Json {
    //    prettyPrint = true //json格式化
    //    isLenient = true //宽松解析，json格式异常也可解析，如：{name:"小红",age:"18"} + Person(val name:String,val age:Int) ->Person("小红",18)
    //    ignoreUnknownKeys = true //忽略未知键，如{"name":"小红","age":"18"} ->Person(val name:String)
    //    coerceInputValues =  true //强制输入值，如果json属性与对象格式不符，则使用对象默认值，如：{"name":"小红","age":null} + Person(val name:String = "小绿"，val age:Int = 18) ->Person("小红",18)
    //    encodeDefaults =  true //编码默认值,默认情况下，默认值的属性不会参与序列化，通过设置encodeDefaults = true,可让默认属性参与序列化(可参考上述例子)
    //    explicitNulls =  true //序列化时是否忽略null
    //    allowStructuredMapKeys =  true //允许结构化映射(map的key可以使用对象)
    //    allowSpecialFloatingPointValues =  true //特殊浮点值：允许Double为NaN或无穷大
    return Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        coerceInputValues = true
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
                    nodeNames.size - 1 -> return when (it.jsonObject[nodeNames[index]]) {
                        null -> null
                        else -> (it.jsonObject[nodeNames[index]] as JsonPrimitive).content
                    }
                    else -> jsonElement = it.jsonObject[nodeNames[index]]
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}