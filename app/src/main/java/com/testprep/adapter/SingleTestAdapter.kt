package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R

class SingleTestAdapter(val context: Context) :
    RecyclerView.Adapter<SingleTestAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_single_test, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.std.text = "Std:12th - Science"
        p0.std.text = "Mathematics"
        p0.std.text = "₹1500"

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var std: TextView = itemView.findViewById(R.id.singleTest_item_tvStd)
        var sub: TextView = itemView.findViewById(R.id.singleTest_item_tvSub)
        var price: TextView = itemView.findViewById(R.id.singleTest_item_tvPrice)
        var addcart: TextView = itemView.findViewById(R.id.singleTest_item_btnAddCart)
    }

}

