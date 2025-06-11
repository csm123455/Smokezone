package com.smokezone.data

import com.smokezone.Location

data class SmokeArea(
    var id: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var title: String? = null, // 지역 이름
    var userId: String? = null, // 사용자 ID
    var comments: Map<String,Comment>? = null, // 코멘트 내용
    var currentLocation: Location? = null, // 현재 위치.
    var targetLocation: Location? = null // 타겟 위치.
)