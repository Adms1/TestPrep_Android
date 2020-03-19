package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R

class NotificationAdapter(val context: Context) :
    RecyclerView.Adapter<NotificationAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.notification_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
