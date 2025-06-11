package com.smokezone.data

data class Comment(
    val authorName: String = "",
    val content: String = "",
    val time: Long = System.currentTimeMillis()
)
