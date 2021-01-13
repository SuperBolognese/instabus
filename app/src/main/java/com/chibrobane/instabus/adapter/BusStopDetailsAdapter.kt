package com.chibrobane.instabus.adapter

import android.app.Activity
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        val title : TextView = itemView.findViewById(R.id.image_title)
        val desc : TextView = itemView.findViewById(R.id.image_date)
        val image : ImageView = itemView.findViewById(R.id.image_stop)

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            // Ici, nous faisons en sorte de créer un menu contextuel afin de les supprimer par la suite
            // (Quand on fait un click long sur un des éléments)
            val menuInflater = MenuInflater(v?.context)
            menuInflater.inflate(R.menu.bus_stop_image_menu, menu)
        }
    }
}