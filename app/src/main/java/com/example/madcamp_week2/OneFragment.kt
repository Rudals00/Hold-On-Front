package com.example.madcamp_week2

import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Nickname
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
import kotlinx.parcelize.Parcelize
import java.lang.Math.min


@Parcelize
data class Post(
    val postId: Int,
    val userId: Int,
    val userNickname: String,
    val category: String,
    val postText: String?,
    val postImgPaths: List<String>?,
    val postVideoPath: String?,
    val postUploadTime: String,
    var likeCount: Int,
    var commentCount: Int
): Parcelable
class MyViewHolder(val binding: PostRecyclerviewBinding,var selectedCategory: String?) : RecyclerView.ViewHolder(binding.root)
{
    val imgRecyclerView: RecyclerView = binding.innerRecyclerView
    var imgAdapter: ImageAdapter? = null
}

class ImageAdapter(private val imgPaths: List<String>?) : RecyclerView.Adapter<ImageAdapter.ImgViewHolder>() {
    inner class ImgViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
        val imageView = ImageView(parent.context)
        return ImgViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        Glide.with(holder.imageView.context).load(imgPaths?.get(position)).into(holder.imageView)
    }

    override fun getItemCount(): Int = imgPaths?.size ?: 0
}
class HeaderViewHolder(val binding: PostLogoBinding) : RecyclerView.ViewHolder(binding.root)

class Myadapter(val datas:MutableList<Post>, val fragmentOneBinding: FragmentOneBinding,var selectedCategory: String?,val activity: FragmentActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
            MyViewHolder(itemBinding,selectedCategory)
        }}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            val binding = (holder as HeaderViewHolder).binding
            binding.all.setOnClickListener {
                selectedCategory = "all"
                notifyDataSetChanged()
            }
            binding.category1.setOnClickListener {
                selectedCategory = "Category1"
                notifyDataSetChanged()
            }
            binding.category2.setOnClickListener {
                selectedCategory = "Category2"
                notifyDataSetChanged()
            }
            binding.category3.setOnClickListener {
                selectedCategory = "Category3"
                notifyDataSetChanged()
            }
        } else if (holder is MyViewHolder) {
            val binding = (holder as MyViewHolder).binding
            val currentCategory = selectedCategory ?: ""
            val imgPaths = datas[position].postImgPaths
            if (imgPaths == null || imgPaths.isEmpty()) {
                holder.imgRecyclerView.visibility = View.GONE
            }
            else {
                holder.imgRecyclerView.visibility = View.VISIBLE
                val sublist =
                    imgPaths?.subList(
                        0,
                        min(5, imgPaths.size)
                    )  // Get the first five images at most
                holder.imgAdapter = ImageAdapter(sublist)
                holder.imgRecyclerView.layoutManager = LinearLayoutManager(
                    holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                holder.imgRecyclerView.adapter = holder.imgAdapter
            }
            binding.userNickname.text =datas[position].userNickname
            binding.postText.text = datas[position].postText
            binding.uploadTimeTextView.text = datas[position].postUploadTime
            binding.commentCountTextView.text = "답글 "+datas[position].commentCount.toString()+"개"
            binding.likeCountTextView.text = "·좋아요 "+datas[position].likeCount.toString()+"개"
            if (currentCategory == "all" || datas[position].category == currentCategory) {
                binding.root.visibility = View.VISIBLE
                binding.root.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                binding.root.visibility = View.GONE
                binding.root.layoutParams.height = 0
            }
            binding.heartImage.setOnClickListener {
                var isLiked =false
                if (isLiked) {
                    binding.heartImage.setImageResource(R.drawable.kakao_login_medium_narrow)
                    datas[position].likeCount = datas[position].likeCount -1
                    binding.likeCountTextView.text = datas[position].likeCount.toString()
                } else {
                    binding.heartImage.setImageResource(R.drawable.kakao_login_medium_narrow)
                    datas[position].likeCount = datas[position].likeCount + 1
                    binding.likeCountTextView.text = datas[position].likeCount.toString()
                }
            }
            holder.itemView.setOnClickListener {
                val postDetailFragment = PostDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("post", datas[position]) // Assuming `Post` is Parcelable.
                    }
                }

                // PostDetailFragment의 생명주기를 관찰하여 리사이클러뷰의 가시성을 변경합니다.
                postDetailFragment.lifecycle.addObserver(object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onFragmentDestroyed() {
                        fragmentOneBinding.recyclerView.visibility = View.VISIBLE
                        fragmentOneBinding.fragmentContainer.visibility=View.GONE
                    }
                })

                // 리사이클러뷰를 숨기고 프래그먼트를 변경합니다.
                fragmentOneBinding.recyclerView.visibility = View.GONE
                fragmentOneBinding.fragmentContainer.visibility=View.VISIBLE
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, postDetailFragment)
                    .addToBackStack(null)
                    .commit()

        }


        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

}
class OneFragment : Fragment() {
    val datas = mutableListOf<Post>()
    private lateinit var binding: FragmentOneBinding
    private var adapter: Myadapter? = null
    private var selectedCategory: String? = "all"





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOneBinding.inflate(inflater, container, false)
        adapter = Myadapter(datas, binding, selectedCategory, requireActivity())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        datas.add(Post(
                0,
                0,
                "UserNickname0",
                "Category0",
                "PostText0",
                null,
                null,
                "UploadTime0",
                0,
                0)
        )// 로고를 위한 dummy data
        // 데이터 추가 예시
        datas.add(Post(1, 1, "UserNickname1", "Category1", "PostText1", null, null, "UploadTime1", 0, 0))
        datas.add(Post(2, 2, "UserNickname2", "Category2", "PostText2", null, null, "UploadTime2", 0, 0))
        datas.add(Post(2, 2, "UserNickname3", "Category3", "PostText3", null, null, "UploadTime3", 0, 0))



        return binding.root
    }


}


