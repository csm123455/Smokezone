package com.smokezone

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.naver.maps.map.NaverMapSdk

class SmokeZoneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
                NaverMapSdk.NaverCloudPlatformClient("fbjm1cpffe")
    }
}

// url 링크 변경
fun Context.startNoSmokeLink() {
    val url = "https://nosmk.khepi.or.kr/nsk/ntcc/index.do"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}