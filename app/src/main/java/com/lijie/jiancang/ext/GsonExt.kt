package com.lijie.jiancang.ext

import com.google.gson.Gson
import org.json.JSONArray

val gson by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Gson()
}

inline fun <reified T> Gson.fromJson(json: String): List<T> {
    val jsonArray = JSONArray(json)
    val list = arrayListOf<T>()
    (0 until jsonArray.length()).forEach {
        val jsonObject = jsonArray[it]
        list.add(fromJson(jsonObject.toString(), T::class.java))
    }
    return list
}