package com.example.madcamp_week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.databinding.FragmentOneBinding
import com.example.madcamp_week2.databinding.FragmentTwoBinding
import com.example.madcamp_week2.databinding.GroupRecyclerviewBinding
import com.example.madcamp_week2.databinding.PostRecyclerviewBinding

data class Group(
    val area: String,
    val title: String,
    val maximmum: Int,
    var current: Int

)
interface OnGroupItemClickedListener {
    fun onGroupClick(group: Group)
}
class TwoViewHolder(val binding: GroupRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

class GroupAdapter(private val datas:MutableList<Group>, private val listener: OnGroupItemClickedListener) : RecyclerView.Adapter<TwoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoViewHolder {
        val itemBinding = GroupRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TwoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TwoViewHolder, position: Int) {
        val binding = (holder as TwoViewHolder).binding
        binding.groupTitle.text = datas[position].title
        binding.area.text = datas[position].area
        binding.current.text = datas[position].current.toString()+"/"
        binding.maximum.text = datas[position].maximmum.toString()


        holder.itemView.setOnClickListener {
            val group = datas[position]
            listener.onGroupClick(group)
        }
    }

    override fun getItemCount() = datas.size
}

class TwoFragment : Fragment(), OnGroupItemClickedListener{
    val datas = mutableListOf<Group>()
    private lateinit var binding: FragmentTwoBinding
    private var groupAdapter: GroupAdapter? = null
    private val PICK_IMAGE_REQUEST = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwoBinding.inflate(inflater, container, false)
        groupAdapter = GroupAdapter(datas, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = groupAdapter

        val areaSpinner: Spinner = binding.areaSpinner
        val areas = arrayOf("강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구",
            "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
            "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구",
            "중랑구")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areas)
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
        val maximumSpinner: Spinner = binding.maximumSpinner
        val maximum= Array(50) { i -> (i + 1).toString() }  // 1부터 50까지의 숫자를 생성합니다.
        val maximumSpinnertAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, maximum)
        maximumSpinnertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        maximumSpinner.adapter = maximumSpinnertAdapter

        maximumSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedMemberCount = parent.getItemAtPosition(position).toString().toInt()
                // 선택된 모집 인원 수를 처리하는 로직을 여기에 작성하세요.
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 해제될 때의 처리 로직을 여기에 작성하세요.
            }
        }
        binding.backButton.setOnClickListener{
            binding.groupNameText.setText("")
            binding.groupAboutText.setText("")
            binding.areaSpinner.setSelection(0)
            binding.maximumSpinner.setSelection(0)
            binding.makeGroup.visibility=View.GONE
            binding.toolbar.visibility=View.VISIBLE
            binding.recyclerView.visibility=View.VISIBLE
            binding.addButton.visibility=View.VISIBLE
        }
        binding.backButtonDetail.setOnClickListener{
            binding.groupInfo.visibility=View.GONE
            binding.makeGroup.visibility=View.GONE
            binding.toolbar.visibility=View.VISIBLE
            binding.recyclerView.visibility=View.VISIBLE
            binding.addButton.visibility=View.VISIBLE
        }
        binding.addImage.setOnClickListener{
            //갤러리에서 이미지 uri받아오기
        }
        binding.complete.setOnClickListener{
            // 완료누르면 정보들 group database에 넣기
            binding.groupNameText.setText("")
            binding.groupAboutText.setText("")
            binding.areaSpinner.setSelection(0)
            binding.maximumSpinner.setSelection(0)
            binding.makeGroup.visibility=View.GONE
            binding.toolbar.visibility=View.VISIBLE
            binding.recyclerView.visibility=View.VISIBLE
            binding.addButton.visibility=View.VISIBLE
        }
        binding.addImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        binding.addButton.setOnClickListener{
            val popupMenu = PopupMenu(requireContext(), binding.addButton)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.make_group ->makeGroup()
                    R.id.join_group ->joinGroup()
                }
                true
            }
            popupMenu.show()
        }

        datas.add(Group("Area 1", "Title 1", 5, 2))
        datas.add(Group("Area 2", "Title 2", 10, 3))
        datas.add(Group("Area 3", "Title 3", 8, 7))
        datas.add(Group("Area 4", "Title 4", 12, 6))

        return binding.root
    }
    override fun onGroupClick(group: Group) {
        binding.groupDatailName.text = group.title
        binding.groupInfo.visibility=View.VISIBLE
        binding.makeGroup.visibility=View.GONE
        binding.toolbar.visibility=View.GONE
        binding.recyclerView.visibility=View.GONE
        binding.addButton.visibility=View.GONE
    }



    private fun joinGroup() {
        val JoinGroupFragment = JoinGroupFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        binding.fragmentContainerJoin.visibility = View.VISIBLE
        binding.makeGroup.visibility=View.GONE
        binding.toolbar.visibility=View.GONE
        binding.recyclerView.visibility=View.GONE
        binding.addButton.visibility=View.GONE
        transaction.replace(R.id.fragment_container_Join, JoinGroupFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        JoinGroupFragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onFragmentDestroyed() {
                binding.fragmentContainerJoin.visibility = View.GONE
                binding.makeGroup.visibility = View.GONE
                binding.toolbar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                binding.addButton.visibility = View.VISIBLE
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data

            // Here you can use the uri to create a File object, open an input stream, etc.
            // ...
        }
    }
    private fun makeGroup() {
        binding.makeGroup.visibility=View.VISIBLE
        binding.toolbar.visibility=View.GONE
        binding.recyclerView.visibility=View.GONE
        binding.addButton.visibility=View.GONE
    }
}