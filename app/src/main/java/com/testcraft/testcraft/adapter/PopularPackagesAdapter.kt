package com.testcraft.testcraft.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testcraft.testcraft.R

class PopularPackagesAdapter(val context: Context) :
    RecyclerView.Adapter<PopularPackagesAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.popular_pkg_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var std: TextView = itemView.findViewById(R.id.package_item_tvStd)
        var sub: TextView = itemView.findViewById(R.id.package_item_tvSub)
        var price: TextView = itemView.findViewById(R.id.package_item_tvPrice)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.mall)
        var tvBuy: ImageView = itemView.findViewById(R.id.package_item_ivCart)

    }
}
