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
import com.testprep.models.MyPackageModel

class MainPackageAdapter(val context: Context, val list: ArrayList<MyPackageModel.MyPkgData>) :
    RecyclerView.Adapter<MainPackageAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)

            LayoutInflater.from(context).inflate(R.layout.item_coverflow, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        p0.rvList.layoutManager = LinearLayoutManager(context as Ac
//        tivity, LinearLayoutManager.HORIZONTAL, false)
//        p0.rvList.adapter = TestPackagesAdapter(context, dataList)

        p0.subject.text = list[p1].Name
        p0.test.text = list[p1].PackageList.size.toString() + " Packages"

//        Picasso.get().load("http://content.testcraft.co.in/question/" + list[p1].Icon).into(p0.ll)

        p0.ll.setOnClickListener {
            val intent = Intent(context, MyPackagesFragment::class.java)
            intent.putExtra("sub_id", list[p1].ID)
            intent.putExtra("sub_name", list[p1].Name)
            intent.putExtra("isCompetitive", list[p1].isCompetitive)
            context.startActivity(intent)
        }
    }



    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var subject: TextView = itemView.findViewById(R.id.choosemp_tvsujectname)
        var test: TextView = itemView.findViewById(R.id.choosemp_tvTest)
        var ll: ImageView = itemView.findViewById(R.id.image)

    }

}

