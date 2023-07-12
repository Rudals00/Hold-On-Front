package com.example.madcamp_week2

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class GymInfoActivity: AppCompatActivity() {
    //private lateinit var webView: WebView
    private val ADD_REVIEW_REQUEST_CODE =1
    private val reviewList = ArrayList<GymReview>() //리뷰 데이터 리스트, review 클래스 따로구현
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter //리뷰 어댑터 클래스 작성하기

    @SuppressLint("MissingInflatedId")
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
            if (gymimage.isNotEmpty()) {
                val id = resources.getIdentifier(gymimage, "drawable", packageName)
                info_gymimage.setImageResource(id)
            }
        }

        //리뷰 리사이클러뷰 설정
        recyclerView = findViewById(R.id.info_recyclerview)
        adapter = ReviewAdapter(reviewList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //리뷰 작성 버튼
        val addReviewButton = findViewById<ImageButton>(R.id.new_review_button)
        addReviewButton.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java)
            intent.putExtra("gym_id", gymId)
            startActivityForResult(intent, ADD_REVIEW_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_REVIEW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //리뷰 작성 액티비티에서 돌아온 후 처리할 내용
            val rating = data?.getFloatExtra("rating", 0.0f)
            val reviewText = data?.getStringExtra("review_text")
            if(rating!=null && reviewText!=null) {
                val newReview = GymReview(rating, reviewText)
                reviewList.add(newReview)
                adapter.notifyDataSetChanged()
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        val webViewContainer = findViewById<ViewGroup>(R.id.info_mapview)
//        webViewContainer.removeAllViews()
//        webView.destroy()
//    }
}