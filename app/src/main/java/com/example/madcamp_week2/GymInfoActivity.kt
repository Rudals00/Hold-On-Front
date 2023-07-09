package com.example.madcamp_week2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.daum.mf.map.api.MapView

class GymInfoActivity: AppCompatActivity() {
    private lateinit var mapView: MapView

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

        val gymname = intent.getStringExtra("name")
        val gymlocation = intent.getStringExtra("location")
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

        //리뷰 작성 버튼
        val addReviewButton = findViewById<ImageButton>(R.id.new_review_button)
        addReviewButton.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java)
            startActivity(intent)
        }
    }
}