package com.example.madcamp_week2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * A simple [Fragment] subclass.
 * Use the [FourFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FourFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_four, container, false)
        try {
            initUI(rootView)
        } catch (e:IOException) {
            e.printStackTrace()
        } catch (e:JSONException) {
            e.printStackTrace()
        }
        return rootView
    }

    @Throws(IOException::class, JSONException::class)
    private fun initUI(rootView: View) {
        val nameDataset = ArrayList<String>()
        val locationDataset = ArrayList<String>()
        val ratingDataset = ArrayList<String>()
        val imageDataset = ArrayList<String>()

        val istream = requireContext().openFileInput("gym.json")
        val reader = BufferedReader(InputStreamReader(istream))

        val buffer = StringBuffer()
        var line = reader.readLine()
        while (line != null) {
            buffer.append("$line\n")
            line = reader.readLine()
        }
        istream.close()

        val jsonData = buffer.toString()
        val jsonArray = JSONArray(jsonData)

        for (i in 0 until jsonArray.length()) {
            val jo = jsonArray.getJSONObject(i)
            val name = jo.optString("name", "")
            val location = jo.optString("location", "")
            val rating = jo.optString("rating", "")
            val imgpath = jo.optString("image", "")
            nameDataset.add(name)
            locationDataset.add(location)
            ratingDataset.add(rating)
            imageDataset.add(imgpath)
        }

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.fragfour_recyclerView)

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        val customAdapter = MapCustomAdapter(nameDataset, locationDataset, ratingDataset, imageDataset)

        //click event implementation
        customAdapter.setOnItemClickListener(object : MapCustomAdapter.OnItemClickListener {
            override fun onItemClicked(position: Int, data: String) {
                val intent = Intent(requireContext(), GymInfoActivity::class.java)
                startActivity(intent)
            }
        })
        recyclerView.adapter = customAdapter
    }
}