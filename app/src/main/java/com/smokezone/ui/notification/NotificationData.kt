package com.smokezone.ui.notification

import java.util.UUID

data class NotificationData(
    val hour: Int,
    val minute: Int,
    val uuid: UUID
)
