package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.models.PackageData

class TestTypeAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageTestType>) :
    RecyclerView.Adapter<TestTypeAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(com.testprep.R.layout.list_item_test_type, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.test_name.text = dataList[p1].TestTypeName
        p0.test_quantity.text = "30"

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var test_name: TextView = itemView.findViewById(com.testprep.R.id.test_type_item_tvName)
        var test_quantity: TextView = itemView.findViewById(com.testprep.R.id.test_type_item_tvQuantity)

    }

}
