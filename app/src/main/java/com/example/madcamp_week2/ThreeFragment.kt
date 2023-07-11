package com.example.madcamp_week2

import android.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.databinding.FragmentThreeBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException

class ThreeFragment : Fragment() {
    private lateinit var binding: FragmentThreeBinding
    private val PICK_IMAGE_REQUEST = 101
    private var user_ID: String = ""
    private var imageUris: MutableList<Uri> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThreeBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            binding.inputPost.setText("")
        }

        binding.galleryButton.setOnClickListener {
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

        binding.complete.setOnClickListener {
            val category = binding.postCategorySpinner.selectedItem.toString()
            val postText = binding.inputPost.text.toString()

            if (imageUris.size > 5) {
                Toast.makeText(requireContext(), "You can only select up to 5 images.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (user_ID.isNotEmpty() && postText.isNotEmpty()) {
                uploadPost(category, postText)
            } else {
                Toast.makeText(requireContext(), "Please provide user ID and post text.", Toast.LENGTH_SHORT).show()
            }
        }

        val categorySpinner: Spinner = binding.postCategorySpinner
        val category = arrayOf("일상", "자유", "질문")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, category)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                // 선택된 구를 처리하는 로직을 여기에 작성하세요.
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 해제될 때의 처리 로직을 여기에 작성하세요.
            }
        }

        return binding.root
    }

    private fun uploadPost(category: String, postText: String) {
        val client = OkHttpClient()

        val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBodyBuilder.addFormDataPart("user_ID", user_ID)
        requestBodyBuilder.addFormDataPart("category", category)
        requestBodyBuilder.addFormDataPart("post_text", postText)

        for (i in 0 until imageUris.size) {
            val imageUri = imageUris[i]
            val imagePath = getPathFromURI(imageUri)
            if (imagePath != null) {
                val file = File(imagePath)
                requestBodyBuilder.addFormDataPart(
                    "images",
                    file.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)
                )
            }
        }

        val requestBody = requestBodyBuilder.build()

        val request = Request.Builder()
            .url("http://172.10.5.168/uploadPost") // Node.js 서버 주소로 변경해야 합니다.
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // 요청 실패 시 처리할 로직을 작성하세요.
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Post uploaded successfully", Toast.LENGTH_SHORT).show()
                        binding.inputPost.text.clear()
                        imageUris.clear()
                    }
                } else {
                    // 응답이 실패한 경우 처리할 로직을 작성하세요.
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.clipData != null) { // handle multiple images
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    imageUris.add(imageUri)
                }
            } else if (data.data != null) { // handle single image
                val imageUri = data.data
                if (imageUri != null) {
                    imageUris.add(imageUri)
                }
            }
        }
    }

    fun setResponseData(userID: String) {
        this.user_ID = userID
    }
}
