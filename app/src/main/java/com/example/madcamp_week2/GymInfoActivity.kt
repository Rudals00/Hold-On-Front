package com.example.madcamp_week2

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class GymInfoActivity: AppCompatActivity() {
    //private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyminfo)

        //기본 정보 표시
        val info_gymimage = findViewById<ImageView>(R.id.info_gymimage)
        val info_gymname = findViewById<TextView>(R.id.info_gymname)
        val info_gymlocation = findViewById<TextView>(R.id.info_gymlocation)
        val info_gymrating = findViewById<TextView>(R.id.info_rating)
        val info_ratingBar = findViewById<RatingBar>(R.id.info_ratingbar)

        val intent = intent

        val gymId = intent.getIntExtra("gym_id", 0)
        val gymname = intent.getStringExtra("name")
        val gymlocation = intent.getStringExtra("location")
        val gymlatitude = intent.getDoubleExtra("latitude", 0.0)
        val gymlongitude = intent.getDoubleExtra("longitude", 0.0)
        val gymrating = intent.getStringExtra("rating")
        val gymratingfloat = intent.getStringExtra("rating")?.toFloat()
        val gymimage = intent.getStringExtra("image")

        info_gymname.text = gymname
        info_gymlocation.text = gymlocation
        info_gymrating.text = gymrating

        if(gymratingfloat!=null) {
            info_ratingBar.rating = gymratingfloat
        }

        if (gymimage != null) {
            if(gymimage.isNotEmpty()) {
                val id = resources.getIdentifier(gymimage, "drawable", packageName)
                info_gymimage.setImageResource(id)
            }
        }

//        webView  = findViewById(R.id.info_mapview)
//        webView.webViewClient = WebViewClient()
//        val webSettings: WebSettings = webView.settings
//        webSettings.javaScriptEnabled = true
//        webView.loadUrl("https://map.kakao.com/")

        val latitude = gymlatitude
        val longitude = gymlongitude

//        //지도 중심 좌표 설정
//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true)
//
//        //마커 추가
//        val marker = MapPOIItem()
//        marker.itemName = gymname
//        marker.tag=0
//        marker.mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
//        marker.markerType = MapPOIItem.MarkerType.BluePin
//        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
//        mapView.addPOIItem(marker)


        //리뷰 작성 버튼
        val addReviewButton = findViewById<ImageButton>(R.id.new_review_button)
        addReviewButton.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java)
            intent.putExtra("gym_id", gymId)
            startActivity(intent)
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        val webViewContainer = findViewById<ViewGroup>(R.id.info_mapview)
//        webViewContainer.removeAllViews()
//        webView.destroy()
//    }
}