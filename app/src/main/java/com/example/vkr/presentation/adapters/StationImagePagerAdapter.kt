package com.example.vkr.presentation.adapters // поместите в свой пакет

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemImageBinding

class StationImagePagerAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<StationImagePagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // картинка-заглушка пока грузится
            .into(holder.binding.imageViewItem)
    }

    override fun getItemCount(): Int = imageUrls.size
}