package com.example.madcamp_week2

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.madcamp_week2.databinding.FragmentThreeBinding
import com.kakao.sdk.user.UserApiClient

class ThreeFragment : Fragment() {
    private lateinit var binding: FragmentThreeBinding
    private val PICK_IMAGE_REQUEST = 101
    private var user_ID: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThreeBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            binding.inputPost.setText("")

        }
        binding.galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        binding.complete.setOnClickListener {

        }

        val categorySpinner: Spinner = binding.postCategorySpinner
        val category = arrayOf("카테고리1", "카테고리2", "카테고리3")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, category)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                // 선택된 구를 처리하는 로직을 여기에 작성하세요.
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 해제될 때의 처리 로직을 여기에 작성하세요.
            }
        }


        return binding.root
    }

    fun setResponseData(userID: String) {
        // 받은 데이터를 처리하고 UI에 반영하는 로직을 작성하세요.
        this.user_ID = userID
        // 예시: TextView에 데이터를 설정하는 경우
        // binding.textView.text = responseData
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.clipData != null) { // handle multiple images
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    // Use imageUri as you want
                }
            } else if (data.data != null) { // handle single image
                val imageUri = data.data
                // Use imageUri as you want
            }
        }
    }
}