package com.example.madcamp_week2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R

class ReviewAdapter(private val reviewList: List<GymReview>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingBar: RatingBar = itemView.findViewById(R.id.review_item_ratingBar)
        val ratingTextView: TextView = itemView.findViewById(R.id.review_item_rating)
        val reviewTextView: TextView = itemView.findViewById(R.id.review_item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_gyminfo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = reviewList[position]
        holder.ratingBar.rating = currentItem.rating.toFloat()
        holder.ratingTextView.text = "(${currentItem.rating})"
        holder.reviewTextView.text = currentItem.reviewText
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}
