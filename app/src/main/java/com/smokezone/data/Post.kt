package com.smokezone.data

import com.smokezone.ui.community.PostComment
import java.io.Serializable

data class Post(
    val title: String = "",
    val content: String = "",
    val author: String = "",
    val timestamp: Long = 0,
    val comments: List<PostComment> = emptyList(),
): Serializable
