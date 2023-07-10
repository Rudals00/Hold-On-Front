package com.example.madcamp_week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class joinmemberShipActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 101
    private lateinit var addProfileImg : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joinmembership)
        val buttonJoin = findViewById<Button>(R.id.buttonJoin)
        addProfileImg = findViewById(R.id.addprofileImg)

        addProfileImg.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        // 가입하기 버튼에 클릭 리스너 설정
        buttonJoin.setOnClickListener {
            // SecondActivity를 시작하는 Intent 생성
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent) // Intent를 사용하여 SecondActivity 시작
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            Glide.with(this)
                .load(uri)
                .into(addProfileImg)
            // Here you can use the uri to create a File object, open an input stream, etc.
            // ...
        }
    }
}