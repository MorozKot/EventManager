package com.example.eventmanager.presentation

import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanager.R
import com.squareup.picasso.Picasso

class EventItemViewHolder(
    val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val imageView: ImageView? = itemView.findViewById(R.id.image_weather)

    fun bind(imageUrl: String) {
        imageView?.let {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_weather)
                .into(imageView)
        }
    }
}
