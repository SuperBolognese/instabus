package com.chibrobane.instabus.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chibrobane.instabus.R
import com.chibrobane.instabus.data.BusStopDetails

class BusStopDetailsAdapter
    (val activity: Activity,
     val items: MutableList<BusStopDetails>): RecyclerView.Adapter<BusStopDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity)
                .inflate(R.layout.item_bus_image, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.desc.text = ("Le " + item.date_of_creation)
        holder.image.setImageURI(item.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.image_title)
        val desc : TextView = itemView.findViewById(R.id.image_date)
        val image : ImageView = itemView.findViewById(R.id.image_stop)
    }
}