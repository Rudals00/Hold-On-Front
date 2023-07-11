package com.example.madcamp_week2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.databinding.FragmentTwoBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jetbrains.annotations.Nullable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * A simple [Fragment] subclass.
 * Use the [FourFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FourFragment : Fragment() {
    private val gymIdDataset = ArrayList<Int>()
    private val nameDataset = ArrayList<String>()
    private val locationDataset = ArrayList<String>()
    private val latitudeDataset = ArrayList<Double>()
    private val longitudeDataset = ArrayList<Double>()
    private val ratingDataset = ArrayList<String>()
    private val imageDataset = ArrayList<String>()
    private var user_ID: String = ""

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_four, container, false)

        val areaSpinner = rootView.findViewById<Spinner>(R.id.fragfour_areaSpinner)

        val areas = arrayOf("강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구",
            "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
            "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구",
            "중랑구")

        val adapter = ArrayAdapter(rootView.context, android.R.layout.simple_spinner_item, areas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        areaSpinner.adapter = adapter

        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedArea = parent.getItemAtPosition(position).toString()
                // 선택된 구를 처리하는 로직을 여기에 작성하세요.
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 해제될 때의 처리 로직을 여기에 작성하세요.
            }
        }
        initUI(rootView)
        return rootView
    }
    fun setResponseData(userID: String) {
        // 받은 데이터를 처리하고 UI에 반영하는 로직을 작성하세요.
        this.user_ID = userID
        // 예시: TextView에 데이터를 설정하는 경우
        // binding.textView.text = responseData
    }
    private fun initUI(rootView: View) {
        //서버 엔드포인트 url
        val url = "http://172.10.5.168/gymData"

        //OkHttpClient 생성
        val client = OkHttpClient()

        //Get 요청 생성
        val request = Request.Builder().url(url).build()

        //요청 전송
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                //요청 실패 처리
                //예외 처리 혹은 오류 메세지 표시
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "서버에 연결할 수 없습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                //응답 처리
                val responseBody = response.body?.string()

                try {
                    //JSON 데이터 파싱
                    val jsonArray = JSONArray(responseBody)
                    for(i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        //필요한 데이터 추출
                        val gymId = jsonObject.getInt("gym_id")
                        val gymName = jsonObject.getString("gym_name")
                        val gymAddress = jsonObject.getString("gym_address")
                        val latitude = jsonObject.getDouble("latitude")
                        val longitude = jsonObject.getDouble("longitude")

                        gymIdDataset.add(gymId)
                        nameDataset.add(gymName)
                        locationDataset.add(gymAddress)
                        latitudeDataset.add(latitude)
                        longitudeDataset.add(longitude)
                        ratingDataset.add("4.7") //이후 rating 받아와서 .add(rating.toString())
                        imageDataset.add("@drawable/seoulforest_ts") //이후 이미지 받아와서 imagePath 추가
                    }

                    //ui 업데이트
                    requireActivity().runOnUiThread {
                        //리사이클러뷰 초기화
                        val recyclerView = rootView.findViewById<RecyclerView>(R.id.fragfour_recyclerView)
                        val layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.layoutManager = layoutManager

                        //RatingBar 초기화
                        val ratingBar = rootView.findViewById<RatingBar>(R.id.ratingBar)

                        val customAdapter = MapCustomAdapter(gymIdDataset, nameDataset, locationDataset, ratingDataset, imageDataset, latitudeDataset, longitudeDataset)
                        recyclerView.adapter = customAdapter

                        //click event implementation
                        customAdapter.setOnItemClickListener(object : MapCustomAdapter.OnItemClickListener {
                            override fun onItemClicked(position: Int, data: String) {
                                val intent = Intent(requireContext(), GymInfoActivity::class.java)
                                startActivity(intent)
                            }
                        })
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "데이터를 파싱하는데 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

//        nameDataset.add("서울숲 뚝섬")
//        nameDataset.add("PEAKERS 클라이밍 종로")
//        nameDataset.add("락랜드")
//        locationDataset.add("서울특별시 성동구 성수일로 19")
//        locationDataset.add("서울특별시 종로구 돈화문로5가길 1")
//        locationDataset.add("서울특별시 강북구 도봉로 315")
//        ratingDataset.add("4.75")
//        ratingDataset.add("4.5")
//        ratingDataset.add("4.6")
//        imageDataset.add("@drawable/seoulforest_ts")
//        imageDataset.add("@drawable/peakers_guro")
//        imageDataset.add("@drawable/rockland")

    }
}