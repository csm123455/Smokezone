package com.smokezone.ui.chat

data class ChatListItem(
    val roomName: String,
    val key: Long,
) {
    constructor() : this("", 0)
}