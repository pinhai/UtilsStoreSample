package com.hp.utils_store.utils

import java.lang.reflect.Modifier
import java.util.*

/**
 * Map和Object互转工具类
 */
object ObjectUtil {

    fun mapToObject(map: Map<String?, String?>?, beanClass: Class<*>): Any? {
        if (map == null) return null
        var obj: Any? = null
        try {
            obj = beanClass.newInstance()
            val fields = obj.javaClass.declaredFields
            for (field in fields) {
                val mod = field.modifiers
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue
                }
                field.isAccessible = true
                field[obj] = map[field.name]
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
        return obj
    }

    fun objectToMap(obj: Any?): Map<String, Any>? {
        if (obj == null) {
            return null
        }
        val map: MutableMap<String, Any> = HashMap()
        val declaredFields = obj.javaClass.declaredFields
        for (field in declaredFields) {
            field.isAccessible = true
            try {
                val o = field[obj]
                if (o != null) {
                    if (o is String && o.toString() === "null") {
                        continue
                    } else {
                        map[field.name] = field[obj]
                    }
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        return map
    }
}