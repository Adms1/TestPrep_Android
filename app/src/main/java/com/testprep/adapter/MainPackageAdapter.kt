package com.testprep.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.TutorDetailActivity
import com.testprep.models.PackageData

class MainPackageAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<MainPackageAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)

            LayoutInflater.from(context).inflate(R.layout.main_pkg_list_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return if (dataList != null && dataList.size > 0) {
            dataList.size
        } else {
            1
        }
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.rvList.layoutManager = LinearLayoutManager(context as Activity, LinearLayoutManager.HORIZONTAL, false)
        p0.rvList.adapter = TestPackagesAdapter(context, dataList)

        p0.tvsee.setOnClickListener {
            val intent = Intent(context, TutorDetailActivity::class.java)
            intent.putExtra("pname", "Standard 9")
            intent.putExtra("parr", dataList)
            context.startActivity(intent)
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var rvList: RecyclerView = itemView.findViewById(R.id.main_pkg_item_rvList)
        var tvsee: TextView = itemView.findViewById(R.id.main_pkg_item_tvSeeall)

    }

}

