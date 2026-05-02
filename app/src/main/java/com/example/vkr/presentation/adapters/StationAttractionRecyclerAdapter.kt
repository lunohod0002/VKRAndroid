package com.example.vkr.presentation.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.vkr.network.dto.StationAttractionResponse


class StationAttractionRecyclerAdapter(
    private val onItemClick: (StationAttractionResponse) -> Unit
) : ListAdapter<StationAttractionResponse, StationAttractionRecyclerAdapter.AttractionViewHolder>(DIFF) {

    class AttractionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.stationAttractionsImage)
        val name: TextView = itemView.findViewById(R.id.stationAttractionName)
        val distance: TextView = itemView.findViewById(R.id.stationAttractionDistance)
        val price: TextView = itemView.findViewById(R.id.stationAttractionPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_station_attraction, parent, false)
        return AttractionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.name
        holder.distance.text = formatDistance(item.distance)
        holder.price.text = "От ${item.price} ₽"

        Glide.with(holder.image)
            .load(item.urlRef)
            .centerCrop()
            .into(holder.image)


        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    private fun formatDistance(meters: Int): String =
        if (meters < 1000) "$meters метров от вас"
        else "%.1f км от вас".format(meters / 1000.0)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<StationAttractionResponse>() {
            override fun areItemsTheSame(oldItem: StationAttractionResponse, newItem: StationAttractionResponse) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: StationAttractionResponse, newItem: StationAttractionResponse) =
                oldItem == newItem
        }
    }
}