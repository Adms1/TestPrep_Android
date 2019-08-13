package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.fragments.MyPackagesFragment

class MainPackageAdapter(val context: Context) :
    RecyclerView.Adapter<MainPackageAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)

            LayoutInflater.from(context).inflate(R.layout.item_coverflow, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return 5

    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        p0.rvList.layoutManager = LinearLayoutManager(context as Activity, LinearLayoutManager.HORIZONTAL, false)
//        p0.rvList.adapter = TestPackagesAdapter(context, dataList)

        p0.subject.text = "Mathematics"
        p0.test.text = "03/10 Pending Test"

        p0.ll.setOnClickListener {
            val intent = Intent(context, MyPackagesFragment::class.java)
            context.startActivity(intent)
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var subject: TextView = itemView.findViewById(R.id.choosemp_tvsujectname)
        var test: TextView = itemView.findViewById(R.id.choosemp_tvTest)
        var ll: ImageView = itemView.findViewById(R.id.image)

    }

}

