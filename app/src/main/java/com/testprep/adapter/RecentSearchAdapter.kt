package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.DashboardActivity
import com.testprep.activity.TutorDetailActivity
import kotlinx.android.synthetic.main.fragment_explore.*

class RecentSearchAdapter(val context: Context, val dataList: ArrayList<String>) :
    RecyclerView.Adapter<RecentSearchAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)

            LayoutInflater.from(context).inflate(R.layout.recent_search_list_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.name.text = dataList[p1]

        p0.name.setOnClickListener {
            val intent = Intent(context, TutorDetailActivity::class.java)
            intent.putExtra("type", "explore")
            intent.putExtra("pname", "Packages")
            intent.putExtra("boardid", "")
            intent.putExtra("stdid", "")
            intent.putExtra("subid", "")
            intent.putExtra("tutorid", "")
            intent.putExtra("search_name", dataList[p1])
            context.startActivity(intent)
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.recent_item_ivName)

    }

}

