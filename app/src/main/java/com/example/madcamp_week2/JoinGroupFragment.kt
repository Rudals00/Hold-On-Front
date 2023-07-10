package com.example.madcamp_week2

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.databinding.FragmentJoinGroupBinding
import com.example.madcamp_week2.databinding.GroupItemLayoutBinding
class GroupViewHolder1(val binding: GroupItemLayoutBinding) : RecyclerView.ViewHolder((binding.root))
class GroupAdapter1(private val groups: List<Group>) : RecyclerView.Adapter<GroupViewHolder1>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder1 {
        val itembinding = GroupItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder1(itembinding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder1, position: Int) {
        val group = groups[position]
        val binding = (holder as GroupViewHolder1).binding
        // Bind data to views here
        binding.areaTextView.text = group.area
        binding.titleTextView.text = group.title
        binding.maximumTextView.text = "최대 인원: ${group.maximmum}"
        binding.currentTextView.text = "현재 인원: ${group.current}"
    }

    override fun getItemCount(): Int = groups.size
}

class JoinGroupFragment : Fragment() {
    private lateinit var binding: FragmentJoinGroupBinding
    private lateinit var groupRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinGroupBinding.inflate(inflater, container, false)
        groupRecyclerView = binding.groupRecyclerview
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.search.setOnClickListener{
            //해당 이름의 그룹 보여주기
        }

        val groups = listOf(
            Group("Location 1", "Group 1", 10, 5),
            Group("Location 2", "Group 2", 8, 3),
            Group("Location 3", "Group 3", 6, 2)
        )
        val groupAdapter = GroupAdapter1(groups)
        groupRecyclerView.layoutManager = LinearLayoutManager(context)
        groupRecyclerView.adapter = groupAdapter

        val areaSpinner: Spinner = binding.groupAreaSpinner
        val areas = arrayOf("전체", "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구",
            "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
            "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구",
            "중랑구")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, areas)
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

        return binding.root
    }
}