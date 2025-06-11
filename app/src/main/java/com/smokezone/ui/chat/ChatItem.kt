package com.smokezone.ui.chat

data class ChatItem(
    val senderId: String,
    val message: String
) {
    constructor(): this("", "")
}