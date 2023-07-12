package com.example.madcamp_week2

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_week2.databinding.FragmentOneBinding
import com.example.madcamp_week2.databinding.PostLogoBinding
import com.example.madcamp_week2.databinding.PostRecyclerviewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Math.min

@Parcelize
data class Comment(
    val userNickname: String,
    val commentText: String
) : Parcelable
private class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = spacing // 오른쪽 여백 설정
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
            outRect.left = spacing // 왼쪽 여백 설정 (마지막 아이템 제외)
        }
    }
}
@Parcelize
data class Post(
    val postId: Int,
    val userId: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val category: String,
    val post_text: String?, // TEXT에 맞게 String 타입으로 변경하고 null 허용
    val postImgPath: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val img_path_1: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val img_path_2: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val img_path_3: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val img_path_4: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val img_path_5: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val postVideoPath: String?, // VARCHAR(255)에 맞게 String 타입으로 변경하고 null 허용
    val postCreatedAt: String? // DATETIME에 맞게 String 타입으로 변경하고 null 허용
) : Parcelable

class MyViewHolder(val binding: PostRecyclerviewBinding, var selectedCategory: String?) :
    RecyclerView.ViewHolder(binding.root) {
    val imgRecyclerView: RecyclerView = binding.innerRecyclerView
    var imgAdapter: ImageAdapter? = null
    init {
        val layoutParams = imgRecyclerView.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        imgRecyclerView.layoutParams = layoutParams
        val itemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = 10 // 오른쪽 여백 설정
                outRect.left = 10
            }
        }
        imgRecyclerView.addItemDecoration(itemDecoration)
    }
}

class ImageAdapter(private val imgPaths: List<String>?) :
    RecyclerView.Adapter<ImageAdapter.ImgViewHolder>() {
    inner class ImgViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
        val imageView = ImageView(parent.context)
        return ImgViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        val imgPath = imgPaths?.get(position)
        if (imgPath != null) {
            Glide.with(holder.imageView.context).load("http://172.10.5.168/"+imgPath).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = imgPaths?.size ?: 0
}

class HeaderViewHolder(val binding: PostLogoBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(
    val datas: MutableList<Post>,
    val fragmentOneBinding: FragmentOneBinding,
    var selectedCategory: String?,
    val activity: FragmentActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            // 첫 번째 아이템에 해당하는 뷰 홀더 생성
            val headerBinding = PostLogoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            HeaderViewHolder(headerBinding)
        } else {
            // 일반 아이템에 해당하는 뷰 홀더 생성
            val itemBinding = PostRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            MyViewHolder(itemBinding, selectedCategory)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            val binding = (holder as HeaderViewHolder).binding
            binding.all.setOnClickListener {
                selectedCategory = "all"
                notifyDataSetChanged()
            }
            binding.category1.setOnClickListener {
                selectedCategory = "일상"
                notifyDataSetChanged()
            }
            binding.category2.setOnClickListener {
                selectedCategory = "자유"
                notifyDataSetChanged()
            }
            binding.category3.setOnClickListener {
                selectedCategory = "질문"
                notifyDataSetChanged()
            }
        } else if (holder is MyViewHolder) {
            val binding = (holder as MyViewHolder).binding
            val currentCategory = selectedCategory ?: ""
            val imgPaths = listOfNotNull(
                datas[position].img_path_1,
                datas[position].img_path_2,
                datas[position].img_path_3,
                datas[position].img_path_4,
                datas[position].img_path_5
            )

            if (imgPaths.isEmpty()) {
                holder.imgRecyclerView.visibility = View.GONE
            } else {
                holder.imgRecyclerView.visibility = View.VISIBLE
                holder.imgAdapter = ImageAdapter(imgPaths)
                holder.imgRecyclerView.layoutManager = LinearLayoutManager(
                    holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                holder.imgRecyclerView.adapter = holder.imgAdapter
            }
//            binding.userNickname.text =datas[position].userNickname
            if (datas[position].post_text != null) {
                binding.postText.text = datas[position].post_text
            } else {
                binding.postText.text = "No post text available"
            }
            datas[1].post_text?.let { Log.d("feawfeawfaewf", it) }
//            binding.uploadTimeTextView.text = datas[position].postUploadTime
//            binding.commentCountTextView.text = "답글 "+datas[position].commentCount.toString()+"개"
//            binding.likeCountTextView.text = "·좋아요 "+datas[position].likeCount.toString()+"개"
            if (currentCategory == "all" || datas[position].category == currentCategory) {
                binding.root.visibility = View.VISIBLE
                binding.root.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                binding.root.visibility = View.GONE
                binding.root.layoutParams.height = 0
            }
//            binding.heartImage.setOnClickListener {
//                var isLiked =false
//                if (isLiked) {
//                    binding.heartImage.setImageResource(R.drawable.kakao_login_medium_narrow)
//                    datas[position].likeCount = datas[position].likeCount -1
//                    binding.likeCountTextView.text = datas[position].likeCount.toString()
//                } else {
//                    binding.heartImage.setImageResource(R.drawable.kakao_login_medium_narrow)
//                    datas[position].likeCount = datas[position].likeCount + 1
//                    binding.likeCountTextView.text = datas[position].likeCount.toString()
//                }
//            }
//            holder.itemView.setOnClickListener {
//                val postDetailFragment = PostDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable("post", datas[position]) // Assuming `Post` is Parcelable.
//                    }
//                }
//
//                // PostDetailFragment의 생명주기를 관찰하여 리사이클러뷰의 가시성을 변경합니다.
//                postDetailFragment.lifecycle.addObserver(object : LifecycleObserver {
//                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//                    fun onFragmentDestroyed() {
//                        fragmentOneBinding.recyclerView.visibility = View.VISIBLE
//                        fragmentOneBinding.fragmentContainer.visibility = View.GONE
//                    }
//                })
//
//                // 리사이클러뷰를 숨기고 프래그먼트를 변경합니다.
//                fragmentOneBinding.recyclerView.visibility = View.GONE
//                fragmentOneBinding.fragmentContainer.visibility = View.VISIBLE
//                activity.supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, postDetailFragment)
//                    .addToBackStack(null)
//                    .commit()
//
//            }
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}

class OneFragment : Fragment() {
    private val datas = mutableListOf<Post>()
    private lateinit var binding: FragmentOneBinding
    private var adapter: MyAdapter? = null
    private var selectedCategory: String? = "all"
    private var user_ID: String = ""
    override fun onResume() {
        super.onResume()
        // 데이터 로드
        loadData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOneBinding.inflate(inflater, container, false)
        adapter = MyAdapter(datas, binding, selectedCategory, requireActivity())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        datas.add(
            Post(
                0,
                "0",
                "UserNickname0",
                "Category0",
                "PostText0",
                null,
                null,
                "UploadTime0",
                null,
                null,
                null,
                null
            )
        )// 로고를 위한 dummy data
        return binding.root
    }

    fun setResponseData(userID: String) {
        // 받은 데이터를 처리하고 UI에 반영하는 로직을 작성하세요.
        this.user_ID = userID
        // 예시: TextView에 데이터를 설정하는 경우
        // binding.textView.text = responseData
    }

    private fun updatePosts(posts: List<Post>) {
        // 받아온 게시물 리스트를 어댑터의 datas에 대입하여 업데이트하고 RecyclerView에 변경 내용을 반영
        adapter?.datas?.clear()
        adapter?.datas?.addAll(posts)
        adapter?.notifyDataSetChanged()
    }

    private fun loadData() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://172.10.5.168/posts") // 게시물 데이터를 가져오는 API 엔드포인트 URL로 변경
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // 요청 실패 시 처리할 로직을 작성하세요.
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    Log.d("Data1", "Response Data: $responseData")
                    if (!responseData.isNullOrEmpty()) {
                        val posts = parsePosts(responseData)
                        activity?.runOnUiThread {
                            updatePosts(posts)
                        }
                    }
                } else {
                    // 응답이 실패한 경우 처리할 로직을 작성하세요.
                }
            }
        })
    }

    }
    private fun parsePosts(responseData: String): List<Post> {
        // JSON 데이터를 파싱하여 게시물 리스트로 변환하는 로직을 작성하세요.
        // 예시: Gson 라이브러리를 사용하여 JSON을 객체로 변환
        val gson = Gson()
        val posts: List<Post> = gson.fromJson(responseData, object : TypeToken<List<Post>>() {}.type)
        Log.d("Data", "Parsed Posts: $posts")

        return posts
    }

