package com.example.madcamp_week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AddReviewActivity: AppCompatActivity() {
    private val url = "http://172.10.5.168/review"
    private val PICK_IMAGE_REQUEST = 101
    private lateinit var addReviewImg: ImageView
    private var imageUri: String? = null
    private var gymId: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addreview)

        gymId = intent.getIntExtra("gym_id",0)

        val buttonRegister = findViewById<Button>(R.id.register_button)
        addReviewImg = findViewById(R.id.addReviewImg)

        addReviewImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        //등록하기 버튼에 클릭 리스너 설정
        buttonRegister.setOnClickListener {
            registerReview()
            val intent = Intent(this, GymInfoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            Glide.with(this)
                .load(uri)
                .into(addReviewImg)
            // Here you can use the uri to create a File object, open an input stream, etc.
            // ...
        }
    }

    private fun registerReview() {
        val client = OkHttpClient()

        val ratingBar = findViewById<RatingBar>(R.id.review_rating)
        val userRating = ratingBar.rating


        //리뷰 정보 가져오기 -> gym_id, review_image_path, rating, review_text를 넘겨야 함
        val review_text = findViewById<EditText>(R.id.review_text).text.toString()
        val imageUrl = imageUri

        val json = JSONObject() //json 데이터 생성
        json.put("gym_id", gymId)
        json.put("rating", userRating)
        json.put("review_text", review_text)
        json.put("imageUrl", imageUrl)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        //요청을 비동기로 실행
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    //메인 스레드에서 동작하는 코드 블록을 생성
                    runOnUiThread {
                        Toast.makeText(this@AddReviewActivity, "Review register successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@AddReviewActivity, GymInfoActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AddReviewActivity, "Review register failed", Toast.LENGTH_LONG).show()
                    }
                }
                Log.d("Register", "Response: ${response.body?.string()}")
            }

            override fun onFailure(call: Call, e:IOException) {
                Log.d("Register", "Failed to send request: ${e.localizedMessage}" )
            }
        })

    }
}