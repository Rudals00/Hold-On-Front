package com.example.madcamp_week2
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.madcamp_week2.databinding.FragmentFiveBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

data class ProfileData(val profileImagePath: String, val nickname: String)
class FiveFragment : Fragment() {
    private val SETTINGS_REQUEST_CODE =102
    private lateinit var selectedCrew: String
    private lateinit var selectedGrade: String
    private lateinit var gradeTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var crewTextView: TextView

    private lateinit var binding: FragmentFiveBinding
    private var user_ID: String = ""
    private var profileImageUri: String = ""
    private var nickname: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFiveBinding.inflate(inflater, container, false)

        binding.settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivityForResult(intent, SETTINGS_REQUEST_CODE)
        }

        val rootView = binding.root
        crewTextView = rootView.findViewById(R.id.fragfive_crew)
        gradeTextView = rootView.findViewById(R.id.fragfive_grade)
        nameTextView = rootView.findViewById(R.id.fragfive_username)

        nickname?.let {
            binding.fragfiveUsername.text = it
        }
        fetchProfileData()

        return binding.root
    }

    private fun fetchProfileData() {
        val url = "http://172.10.5.168/profile/$user_ID" // 사용자의 ID에 해당하는 URL로 변경해야 함

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 시 처리할 로직을 작성하세요.
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val profileData = parseProfileData(responseData)
                    if (profileData != null) {
                        activity?.runOnUiThread {
                            setProfileData(profileData)
                        }
                    }
                } else {
                    // 응답이 실패한 경우 처리할 로직을 작성하세요.
                }
            }
        })
    }

    private fun parseProfileData(responseData: String?): ProfileData? {
        try {
            val jsonObject = JSONObject(responseData)
            val profileImagePath = jsonObject.getString("profile_image_path")
            val nickname = jsonObject.getString("nickname")
            return ProfileData(profileImagePath, nickname)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    private fun setProfileData(profileData: ProfileData) {
        // 프로필 이미지 설정
        Log.d("path",profileData.profileImagePath)
        Glide.with(this)
            .load("http://172.10.5.168/"+profileData.profileImagePath)
//            .apply(RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
            .into(binding.fragfiveUserprofile)

        // 닉네임 설정
        binding.fragfiveUsername.text = profileData.nickname
    }
    fun setResponseData(userID: String) {
        // 받은 데이터를 처리하고 UI에 반영하는 로직을 작성하세요.
        this.user_ID = userID
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                selectedGrade = data.getStringExtra("selectedGrade")?: ""
                selectedCrew = data.getStringExtra("selectedCrew")?:""
            }
            binding.fragfiveGrade.text = selectedGrade
            binding.fragfiveCrew.text = selectedCrew
        }
    }

}