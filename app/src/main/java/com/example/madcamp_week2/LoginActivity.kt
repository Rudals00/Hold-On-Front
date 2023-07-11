package com.example.madcamp_week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private lateinit var etID: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private val SERVER_URL = "http://172.10.5.168/login" // Node.js 서버 URL을 입력하세요.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etID = findViewById(R.id.memberID)
        etPassword = findViewById(R.id.memberPassword)
        btnLogin = findViewById(R.id.buttonJoin)

        btnLogin.setOnClickListener {
            val ID = etID.text.toString().trim()
            val password = etPassword.text.toString().trim()
            login(ID, password)
        }
    }

    private fun login(ID: String, password: String) {
        val client = OkHttpClient()

        val json = JSONObject()
        json.put("user_id", ID)
        json.put("password", password)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, SecondActivity::class.java)
                        intent.putExtra("ID", ID) // ID를 다음 액티비티로 전달
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "서버에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
                Log.e("LoginActivity", "Error: ${e.message}")
            }
        })
    }
}