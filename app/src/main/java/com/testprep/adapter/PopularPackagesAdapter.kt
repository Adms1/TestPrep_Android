package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R

class PopularPackagesAdapter(val context: Context) : RecyclerView.Adapter<PopularPackagesAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(LayoutInflater.from(context).inflate(R.layout.popular_pkg_list_item, p0, false))
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        var coin: TextView = itemView.findViewById(R.id.coin_item_short)
//        var rupees: TextView = itemView.findViewById(R.id.coin_item_rupees)
//        var image: ImageView = itemView.findViewById(R.id.coin_item_image)

    }
}
