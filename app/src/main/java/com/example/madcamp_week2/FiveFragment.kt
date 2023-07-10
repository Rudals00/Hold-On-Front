package com.example.madcamp_week2

import android.content.Intent
import android.os.AsyncTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.madcamp_week2.databinding.FragmentFiveBinding
import com.example.madcamp_week2.databinding.FragmentOneBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jetbrains.annotations.Nullable
import java.io.IOException
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FiveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FiveFragment : Fragment() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        val rootView = inflater.inflate(R.layout.fragment_five, container, false)

        val settingsButton = rootView.findViewById<ImageButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
        return rootView
    }
//    private var _binding: FragmentFiveBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentFiveBinding.inflate(inflater, container, false)
//        val rootView = binding.root
//
//        binding.checkConnectionButton.setOnClickListener {
//            checkConnection()
//        }
//
//        return rootView
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun checkConnection() {
//        val client = OkHttpClient.Builder()
//            .connectTimeout(20, TimeUnit.SECONDS) // Connect Timeout
//            .writeTimeout(20, TimeUnit.SECONDS) // Write Timeout
//            .readTimeout(30, TimeUnit.SECONDS) // Read Timeout
//            .build()
//
//        val request = Request.Builder()
//            .url("https://7805-192-249-19-234.jp.ngrok.io") // 서버의 주소로 변경해야 함
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                activity?.runOnUiThread {
//                    Log.e("Connection", "Connection error: ${e.message}")
//                    binding.connectionStatusTextView.text = "Disconnected"
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val isConnected = response.isSuccessful
//                activity?.runOnUiThread {
//                    if (isConnected) {
//                        Log.d("Connection", "Connected to server")
//                        binding.connectionStatusTextView.text = "Connected"
//                    } else {
//                        Log.d("Connection", "Failed to connect to server")
//                        binding.connectionStatusTextView.text = "Disconnected"
//                    }
//                }
//            }
//        })
//    }
}