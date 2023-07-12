package com.example.madcamp_week2

import android.R
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_week2.databinding.FragmentJoinGroupBinding
import com.example.madcamp_week2.databinding.GroupItemLayoutBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException



class GroupViewHolder1(val binding: GroupItemLayoutBinding) : RecyclerView.ViewHolder((binding.root))



class GroupAdapter1(private val groups: List<Group>,private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<GroupViewHolder1>() {
    interface OnItemClickListener {
        fun onItemClick(group: Group)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder1 {
        val itembinding = GroupItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder1(itembinding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder1, position: Int) {
        val group = groups[position]
        val binding = holder.binding
        binding.titleTextView.text = group.crew_name
        binding.areaTextView.text = group.crew_district
        binding.currentTextView.text = group.num_member.toString()
        binding.maximumTextView.text = group.max_member.toString()
        binding.GroupImage.clipToOutline =true
        Glide.with(binding.root)
            .load("http://172.10.5.168/" + group.crew_image_path)
            .into(binding.GroupImage)

        binding.root.setOnClickListener {
            // 가입하기 창을 띄우는 로직을 여기에 작성하세요.
            onItemClickListener.onItemClick(group)
        }


    }




    override fun getItemCount(): Int = groups.size
}

class JoinGroupFragment : Fragment(),GroupAdapter1.OnItemClickListener {
    private lateinit var binding: FragmentJoinGroupBinding
    private lateinit var groupRecyclerView: RecyclerView
    private val datas = mutableListOf<Group>()
    private var groupAdapter: GroupAdapter1? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinGroupBinding.inflate(inflater, container, false)
        groupRecyclerView = binding.groupRecyclerview
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        groupRecyclerView.layoutManager = LinearLayoutManager(context)

        val areaSpinner: Spinner = binding.groupAreaSpinner
        val areas = arrayOf(
            "전체", "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구",
            "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
            "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구",
            "중랑구"
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, areas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        areaSpinner.adapter = adapter

        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedArea = parent.getItemAtPosition(position).toString()
                // 선택된 구를 처리하는 로직을 여기에 작성하세요.
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 해제될 때의 처리 로직을 여기에 작성하세요.
            }
        }

        // fetchGroupData 함수 호출하여 그룹 데이터 가져오기
        fetchGroupData()

        return binding.root
    }

    private fun fetchGroupData() {
        val url = "http://172.10.5.168/group/"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패시 처리할 로직을 작성하세요
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val groupDataList = parseGroupData(responseData) // 그룹 데이터 리스트를 받아옴
                    if (groupDataList != null) {
                        activity?.runOnUiThread {
                            updateGroupDataList(groupDataList)
                        }
                    }
                } else {
                    // 응답이 실패한 경우 처리할 로직
                }
            }
        })
    }
    override public fun onItemClick(group: Group) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("그룹 가입하기")
        dialogBuilder.setMessage("해당 그룹에 가입하시겠습니까?")
        dialogBuilder.setPositiveButton("가입하기") { dialog, which ->
            Toast.makeText(requireContext(), "가입이 완료되었습니다", Toast.LENGTH_LONG).show()
        }
        dialogBuilder.setNegativeButton("취소") { dialog, which ->

        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }



    private fun parseGroupData(responseData: String?): List<Group>? {
        val gson = Gson()
        return gson.fromJson(responseData, object : TypeToken<List<Group>>() {}.type)
    }

    private fun updateGroupDataList(groupDataList: List<Group>) {
        datas.clear()
        datas.addAll(groupDataList)
        groupAdapter?.notifyDataSetChanged()

        // 그룹 어댑터를 초기화하고 데이터를 설정합니다.
        val groupAdapter = GroupAdapter1(datas, this@JoinGroupFragment)
        groupRecyclerView.adapter = groupAdapter
    }
}