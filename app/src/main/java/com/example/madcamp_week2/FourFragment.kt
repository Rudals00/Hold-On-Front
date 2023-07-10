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
import org.jetbrains.annotations.Nullable
import org.json.JSONArray
import org.json.JSONException
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

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_four, container, false)
        initUI(rootView)

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
        return rootView
    }

    @Throws(IOException::class, JSONException::class)
    private fun initUI(rootView: View) {
        val nameDataset = ArrayList<String>()
        val locationDataset = ArrayList<String>()
        val ratingDataset = ArrayList<String>()
        val imageDataset = ArrayList<String>()

        nameDataset.add("서울숲 뚝섬")
        nameDataset.add("PEAKERS 클라이밍 종로")
        nameDataset.add("락랜드")
        locationDataset.add("서울특별시 성동구 성수일로 19")
        locationDataset.add("서울특별시 종로구 돈화문로5가길 1")
        locationDataset.add("서울특별시 강북구 도봉로 315")
        ratingDataset.add("4.75")
        ratingDataset.add("4.5")
        ratingDataset.add("4.6")
        imageDataset.add("@drawable/seoulforest_ts")
        imageDataset.add("@drawable/peakers_guro")
        imageDataset.add("@drawable/rockland")

        //리사이클러뷰 초기화
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.fragfour_recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        //RatingBar 초기화
        val ratingBar = rootView.findViewById<RatingBar>(R.id.ratingBar)

        val customAdapter = MapCustomAdapter(nameDataset, locationDataset, ratingDataset, imageDataset)
        recyclerView.adapter = customAdapter

        //click event implementation
        customAdapter.setOnItemClickListener(object : MapCustomAdapter.OnItemClickListener {
            override fun onItemClicked(position: Int, data: String) {
                val intent = Intent(requireContext(), GymInfoActivity::class.java)
                startActivity(intent)
            }
        })

    }

//    @Throws(IOException::class)
//    private fun copyAssetFileToInternalStorage(fileName: String) {
//        val assetManager = requireContext().assets
//        val inputStream = assetManager.open(fileName)
//        val outputFile = File(requireContext().filesDir, fileName)
//        val outputStream = FileOutputStream(outputFile)
//
//        val buffer = ByteArray(1024)
//        var read: Int = inputStream.read(buffer)
//        while (read != -1) {
//            outputStream.write(buffer, 0, read)
//            read = inputStream.read(buffer)
//        }
//
//        outputStream.flush()
//        outputStream.close()
//        inputStream.close()
//    }
}