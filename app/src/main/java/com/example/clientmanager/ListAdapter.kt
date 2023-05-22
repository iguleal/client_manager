package com.example.clientmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.clientmanager.model.Client
import java.text.SimpleDateFormat
import java.util.Locale

class ListAdapter(
    private val listClient: List<Client>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listClient.size
    }

    override fun onBindViewHolder(holder: ListAdapter.ListViewHolder, position: Int) {
        val itemCurrent = listClient[position]
        holder.bind(itemCurrent)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.txt_client_name)
        private val mobile: TextView = itemView.findViewById(R.id.txt_mobile_name)
        private val cardView: CardView = itemView.findViewById(R.id.cv_list)
        private val dateCover: TextView = itemView.findViewById(R.id.txt_date)

        fun bind(itemCurrent: Client) {
            name.text = itemCurrent.name
            mobile.text = itemCurrent.mobile

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            val date = sdf.format(itemCurrent.dateStart)
            dateCover.text = date

            cardView.setOnClickListener {
                listener.onClick(itemCurrent.id)
            }
            cardView.setOnLongClickListener {
                listener.onLongClickListener(itemCurrent,adapterPosition)
                true
            }

        }
    }
}