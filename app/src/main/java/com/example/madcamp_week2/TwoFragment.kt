package com.example.madcamp_week2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_week2.databinding.FragmentOneBinding
import com.example.madcamp_week2.databinding.FragmentTwoBinding
import com.example.madcamp_week2.databinding.GroupRecyclerviewBinding
import com.example.madcamp_week2.databinding.PostRecyclerviewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

//user가 속한 crew정보를 담고있는 클래스
data class CrewData(
    val crew1: String,
    val crew2: String,
    val crew3: String,
    val crew4: String,
    val crew5: String
    )

data class Group(
    val crew_id: Int,
    val crew_name: String,
    val crew_district: String,
    val max_member: Int,
    val num_member: Int,
    val notice: String?,
    val explanation: String?,
    val crew_image_path: String?
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
        binding.groupTitle.text = datas[position].crew_name
        binding.area.text = datas[position].crew_district
        binding.current.text = datas[position].num_member.toString()
        binding.maximum.text = datas[position].max_member.toString()
        Glide.with(holder.binding.root)
            .load("http://172.10.5.168/" + datas[position].crew_image_path)
            .into(binding.GroupImage)



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
    private var user_ID: String = ""
    private var crew_name: String = ""
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwoBinding.inflate(inflater, container, false)
        groupAdapter = GroupAdapter(datas, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = groupAdapter

        fetchCrewData()

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
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"
            val packageManager = requireContext().packageManager
            val activities = packageManager.queryIntentActivities(intent, 0)
            val isIntentSafe = activities.isNotEmpty()
            if (isIntentSafe) {
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } else {
                Toast.makeText(requireContext(), "No gallery app found.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.complete.setOnClickListener{
            // 완료누르면 정보들 group database에 넣기
            val group_text = binding.groupAboutText.text.toString()
            val group_name = binding.groupNameText.text.toString()
            val group_area = binding.areaSpinner.selectedItem.toString()
            val group_maximum = binding.maximumSpinner.selectedItem.toString().toInt()

            uploadGroup(group_text, group_name, group_area,group_maximum)
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
            val packageManager = requireContext().packageManager
            val activities = packageManager.queryIntentActivities(intent, 0)
            val isIntentSafe = activities.isNotEmpty()
            if (isIntentSafe) {
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } else {
                Toast.makeText(requireContext(), "No gallery app found.", Toast.LENGTH_SHORT).show()
            }
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
        fetchGroupData()


        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            if (uri != null) {
                imageUri = uri
            }
            Glide.with(this)
                .load(uri)
                .into(binding.addImage)
            // Here you can use the uri to create a File object, open an input stream, etc.
            // ...
        }
    }


    private fun uploadGroup(explanation: String, crew_name: String, district: String, group_maximum: Int) {
        val client = OkHttpClient()

        val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBodyBuilder.addFormDataPart("crew_name", crew_name)
        requestBodyBuilder.addFormDataPart("crew_district", district)
        requestBodyBuilder.addFormDataPart("max_member", group_maximum.toString())
        requestBodyBuilder.addFormDataPart("explanation", explanation)

        val imagePath = getPathFromURI(imageUri)
        if (imagePath != null) {
            val file = File(imagePath)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            requestBodyBuilder.addFormDataPart("crew_image_path", file.name, requestFile)
        }

        val requestBody = requestBodyBuilder.build()

        val request = Request.Builder()
            .url("http://172.10.5.168/uploadCrew") // Update with your server address
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle request failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Crew Make successfully", Toast.LENGTH_SHORT).show()
                        // Clear input fields or perform any other UI updates
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        })
    }

    private fun fetchCrewData() {
        val url = "http://172.10.5.168/crew/$user_ID"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.run {

            newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    //요청 실패시 처리할 로직
                }

                override fun onResponse(call:Call, response: Response) {
                    if(response.isSuccessful) {
                        val responseData = response.body?.string()
                        val crewData = parseCrewData(responseData)
                    } else {
                        //응답이 실패한 경우 처리할 로직
                    }
                }

            })
        }
    }

    private fun parseCrewData(responseData: String?): CrewData? {
        try {
            val jsonObject = JSONObject(responseData)
            val crew1 = jsonObject.getString("crew1")
            val crew2 = jsonObject.getString("crew2")
            val crew3 = jsonObject.getString("crew3")
            val crew4 = jsonObject.getString("crew4")
            val crew5 = jsonObject.getString("crew5")

            return CrewData(crew1, crew2, crew3, crew4, crew5)
        } catch(e: JSONException) {
            e.printStackTrace()
        }
        return null
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

    private fun parseGroupData(responseData: String?): List<Group>? {
        val gson = Gson()
        return gson.fromJson(responseData, object : TypeToken<List<Group>>() {}.type)
    }

    private fun updateGroupDataList(groupDataList: List<Group>) {
        datas.clear()
        datas.addAll(groupDataList)
        groupAdapter?.notifyDataSetChanged()
    }
    private fun setGroupData(Group: Group) {
        binding.groupDatailName.text=Group.crew_name
    }

    override fun onGroupClick(group: Group) {
        binding.groupDatailName.text = group.crew_name
        binding.groupInfo.visibility=View.VISIBLE
        binding.makeGroup.visibility=View.GONE
        binding.toolbar.visibility=View.GONE
        binding.recyclerView.visibility=View.GONE
        binding.addButton.visibility=View.GONE
    }
    fun setResponseData(userID: String) {
        // 받은 데이터를 처리하고 UI에 반영하는 로직을 작성하세요.
        this.user_ID = userID
        // 예시: TextView에 데이터를 설정하는 경우
        // binding.textView.text = responseData
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


    private fun getPathFromURI(uri: Uri): String? {
        val projection = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return null
    }
    private fun makeGroup() {
        binding.makeGroup.visibility=View.VISIBLE
        binding.toolbar.visibility=View.GONE
        binding.recyclerView.visibility=View.GONE
        binding.addButton.visibility=View.GONE
    }



}
