package com.sunhonglin.base.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.util.*

class GsonUtil {
    companion object {
        private var gson = Gson()

        /**
         * 转成json
         *
         * @param obj object
         * @return json类型string
         */
        fun toGson(obj: Any): String {
            return gson.toJson(obj)
        }

        /**
         * 转成bean
         *
         * @param jsonStr string
         * @param cls     目标类
         * @return bean
         */
        fun <T> GsonToBean(jsonStr: String, cls: Class<T>): T {
            return gson.fromJson(jsonStr, cls)
        }

        /**
         * 转成list
         *
         * @param jsonStr string
         * @param cls     目标类
         * @return list
         */
        fun <T> GsonToList(
            jsonStr: String,
            cls: Class<T>
        ): MutableList<T> {
            if (jsonStr == "") return mutableListOf()
            val list: MutableList<T> = ArrayList()
            val array = JsonParser.parseString(jsonStr).asJsonArray
            array?.forEach {
                list.add(gson.fromJson(it, cls))
            }
            return list
        }

        /**
         * 转成list中有map的
         *
         * @param jsonStr string
         * @return list
         */
        fun <T> GsonToListMaps(jsonStr: String?): List<Map<String, T>>? {
            return gson.fromJson(
                jsonStr,
                object :
                    TypeToken<List<Map<String?, T>?>?>() {}.type
            )
        }

        /**
         * 转成map的
         *
         * @param jsonStr string
         * @return map
         */
        fun <T> GsonToMaps(jsonStr: String?): Map<String, T>? {
            return gson.fromJson(
                jsonStr,
                object :
                    TypeToken<Map<String?, T>?>() {}.type
            )
        }

        /**
         * 获取某个节点的值
         * @param json 需要处理的json数据，为空时返回null
         * @param nodeNames 需要获取的节点名称，可以传入多个，为层级结构
         * @return 当获取到的节点值为非JsonObject时返回null。
         */
        fun getNodeValue(json: String?, vararg nodeNames: String?): String? {
            if (json == null || json == "") return null
            if (nodeNames.isEmpty()) return json
            var jsonElement = JsonParser.parseString(json)
            jsonElement?.let {
                if (!it.isJsonObject) { //如果传入的非json，返回json
                    return null
                }
                var jsonObject = it.asJsonObject
                nodeNames.forEachIndexed { index, s ->
                    jsonElement = jsonObject[nodeNames[index]]
                    if (jsonElement == null) return null
                    if (!jsonElement.isJsonObject) { //当前为非JsonObject
                        return when (index) { //如果当前节点获取到的是一个Array并且是最后一个节点，则返回获取到的数据。
                            nodeNames.size - 1 -> if (jsonElement.isJsonArray) {
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
    }
}