package com.example.madcamp_week2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_week2.databinding.FragmentPostDetailBinding

class ImageAdapter1 : RecyclerView.Adapter<ImageAdapter1.ImageViewHolder1>() {
    private var images: List<String> = listOf()

    inner class ImageViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.postimages)
    }

    fun setImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder1 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_recyclerview, parent, false)
        return ImageViewHolder1(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder1, position: Int) {
        Glide.with(holder.itemView.context).load(images[position]).into(holder.image)
    }

    override fun getItemCount(): Int = images.size
}

class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var post: Post // Post is your post data model.
    private lateinit var backButton: ImageView
    private lateinit var postText: TextView
    private lateinit var postImages: RecyclerView
    private lateinit var userNickname : TextView
    private lateinit var uploadTime : TextView
    private lateinit var commentCount : TextView
    private lateinit var likeCount : TextView
    private lateinit var imageAdapter: ImageAdapter1 // Adapter for post images RecyclerView

    // Add newInstance factory method to get Post data.
    companion object {
        fun newInstance(post: Post): PostDetailFragment {
            val fragment = PostDetailFragment()
            val args = Bundle()
            args.putParcelable("post", post)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        post = (arguments?.getParcelable("post") as? Post)!!
        binding = FragmentPostDetailBinding.inflate(inflater,container,false)
        val view = binding.root
        backButton = binding.backButton
        postText = binding.postText
        postImages = binding.postimages
        userNickname = binding.userNickname
        uploadTime = binding.uploadTimeTextView
        commentCount = binding.commentCountTextView
        likeCount = binding.likeCountTextView

        postText.text = post.postText
        userNickname.text = post.userNickname
        uploadTime.text = post.postUploadTime
        commentCount.text = "답글 "+post.commentCount.toString()+"개"
        likeCount.text = "·좋아요 " + post.likeCount.toString() + "개"

        // Initialize image adapter and set to the RecyclerView
        imageAdapter = ImageAdapter1()
        postImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postImages.adapter = imageAdapter

        // Load images into RecyclerView
        post.postImgPaths?.let {
            if(it.isEmpty()) {
                postImages.visibility = View.GONE
            } else {
                postImages.visibility = View.VISIBLE
                imageAdapter.setImages(it)
            }
        } ?: run {
            postImages.visibility = View.GONE
        }

        // Set click listener for back button
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }
}