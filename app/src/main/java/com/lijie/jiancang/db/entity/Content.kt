package com.lijie.jiancang.db.entity

sealed class ContentType(val type: String) {
    object Text : ContentType("text")
}

data class Content(
    var type: String,
    var content: String
)
