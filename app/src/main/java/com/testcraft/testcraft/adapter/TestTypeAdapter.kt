package com.testcraft.testcraft.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.JsonArray
import com.testcraft.testcraft.R

@SuppressLint("SetTextI18n")
class TestTypeAdapter(val context: Context, val dataList: JsonArray) :
    RecyclerView.Adapter<TestTypeAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_test_type, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size()
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.test_name.text = dataList[p1].asJsonObject["Name"].asString
        p0.test_duration.text = "Time : " + dataList[p1].asJsonObject["Duration"].asString
        p0.test_quantity.text = dataList[p1].asJsonObject["Marks"].asString

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var test_name: TextView = itemView.findViewById(R.id.test_type_item_tvName)
        var test_duration: TextView = itemView.findViewById(R.id.test_type_item_tvDuration)
        var test_quantity: TextView = itemView.findViewById(R.id.test_type_item_tvQuantity)

    }

}
