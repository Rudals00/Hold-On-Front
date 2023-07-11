package com.example.madcamp_week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class joinmemberShipActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 101
    private lateinit var addProfileImg : ImageView
    private var imageUri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joinmembership)
        val buttonJoin = findViewById<Button>(R.id.buttonJoin)
        addProfileImg = findViewById(R.id.addprofileImg)
        val buttonlogin = findViewById<Button>(R.id.buttonlogin)
        addProfileImg.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        // 가입하기 버튼에 클릭 리스너 설정
        buttonJoin.setOnClickListener {
            signUp()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent) // Intent를 사용하여 SecondActivity 시작
        }
        buttonlogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            imageUri = uri.toString()
            Glide.with(this)
                .load(uri)
                .into(addProfileImg)
            // Here you can use the uri to create a File object, open an input stream, etc.
            // ...
        }
    }
    private fun signUp() {
        val client = OkHttpClient()

        // 사용자 정보를 가져옵니다.
        val ID = findViewById<EditText>(R.id.memberID).text.toString()
        val nickname = findViewById<EditText>(R.id.memberNickname).text.toString()
        val password = findViewById<EditText>(R.id.memberPassword).text.toString()
        val email = findViewById<EditText>(R.id.memberEmail).text.toString()
        val imageUrl = imageUri // 이미지의 URL을 어떻게 가져올지 결정해야 합니다.

        // JSON 데이터를 생성합니다.
        val json = JSONObject()
        json.put("ID",ID)
        json.put("nickname", nickname)
        json.put("password", password)
        json.put("email", email)
        json.put("imageUrl", imageUrl)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        // POST 요청을 생성합니다.
        val request = Request.Builder()
            .url("http://172.10.5.168/signup") // 서버의 주소를 입력해주세요.
            .post(body)
            .build()

        // 요청을 비동기로 실행합니다.
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // 메인 스레드에서 동작하는 코드 블록을 생성
                    runOnUiThread {
                        Toast.makeText(this@joinmemberShipActivity, "Signup successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@joinmemberShipActivity, SecondActivity::class.java)
                        intent.putExtra("ID", ID) // ID를 다음 액티비티로 전달
                        startActivity(intent)
                        startActivity(intent) // 가입 성공 후 SecondActivity 시작
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@joinmemberShipActivity, "Signup failed!", Toast.LENGTH_LONG).show()
                    }
                }
                Log.d("SignUp", "Response: ${response.body?.string()}")
            }

            override fun onFailure(call: Call, e: IOException) {
                // TODO: 네트워크 요청이 실패했을 때 적절한 동작을 수행하세요.
                Log.d("SignUp", "Failed to send request: ${e.localizedMessage}")
            }
        })
    }
}