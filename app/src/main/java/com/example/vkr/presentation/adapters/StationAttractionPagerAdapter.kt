package com.example.vkr.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.vkr.network.dto.StationAttractionResponse

class StationAttractionPagerAdapter(
    private val items: List<StationAttractionResponse>
) : RecyclerView.Adapter<StationAttractionPagerAdapter.AttractionViewHolder>() {

    class AttractionViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.attractionImage)
        val name: TextView = view.findViewById(R.id.attractionName)
        val distance: TextView = view.findViewById(R.id.attractionDistance)
        val price: TextView = view.findViewById(R.id.attractionPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attraction, parent, false)
        return AttractionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.name
        holder.distance.text = item.distance + " метров от вас"
        holder.price.text = "От " + item.price.toString() + " ₽"

        Glide.with(holder.image.context)
            .load(item.urlRef)
            .centerCrop()
            .into(holder.image)
    }

    override fun getItemCount(): Int = items.size
}