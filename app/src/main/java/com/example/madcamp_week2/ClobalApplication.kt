package com.example.madcamp_week2

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,"f09be48f22f6a3ba7e4839bbab2d9604")
    }
}