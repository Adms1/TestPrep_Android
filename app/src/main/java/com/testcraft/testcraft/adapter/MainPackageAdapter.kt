package com.testcraft.testcraft.adapter

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.models.MyPackageModel
import com.testcraft.testcraft.utils.AppConstants

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
        p0.standard.text = list[p1].StandardName
        p0.test.text = list[p1].PackageList.size.toString() + " Packages"

        //            val intent1 = Intent(context, DashboardActivity::class.java)
//            intent1.putExtra("sub_id", list[p1].ID)
//            intent1.putExtra("sub_name", list[p1].Name)
//            intent1.putExtra("isCompetitive", list[p1].isCompetitive)
//            intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//            context.startActivity(intent1)
//            (context as DashboardActivity).finish()

//            val intent = Intent(context, MyPackagesFragment::class.java)
//            intent.putExtra("sub_id", list[p1].ID)
//            intent.putExtra("sub_name", list[p1].Name)
//            intent.putExtra("isCompetitive", list[p1].isCompetitive)
//            context.startActivity(intent)
        when (list[p1].Name) {

            "Social Science" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.social_science))
            }
            "Science" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.science))
            }
            "Statistics" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.economics))
            }
            "Hindi" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.hindi))
            }
            "English" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.english_2))
            }
            "Accountancy" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.account))
            }
            "Physics" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.physics))
            }
            "Gujarati" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.gujarati))
            }
            "Biology" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.biology))
            }
            "Economics" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.economics))
            }
            "JEE Main" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.jee_main_four))
            }
            "JEE Advance" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.jee_main_four))
            }
            "NEET" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.neet))
            }
            "Mathematics" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.mathematics))
            }
            "GUJCET" -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.gujcet))
            }
            else -> {
                p0.ll.setImageDrawable(context.resources.getDrawable(R.drawable.pp_2))
            }

            //        Picasso.get().load("http://content.testcraft.co.in/question/" + list[p1].Icon).into(p0.ll)
        }

//        Picasso.get().load("http://content.testcraft.co.in/question/" + list[p1].Icon).into(p0.ll)

        p0.ll.setOnClickListener {

            AppConstants.isFirst = 11
            val bundle = Bundle()
            bundle.putInt("sub_id", list[p1].ID)
            bundle.putString("std_id", list[p1].StandardID)
            bundle.putString("sub_name", list[p1].Name)
            bundle.putBoolean("isCompetitive", list[p1].isCompetitive)
            setFragments(bundle)

//            val intent1 = Intent(context, DashboardActivity::class.java)
//            intent1.putExtra("sub_id", list[p1].ID)
//            intent1.putExtra("sub_name", list[p1].Name)
//            intent1.putExtra("isCompetitive", list[p1].isCompetitive)
//            intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//            context.startActivity(intent1)
//            (context as DashboardActivity).finish()

//            val intent = Intent(context, MyPackagesFragment::class.java)
//            intent.putExtra("sub_id", list[p1].ID)
//            intent.putExtra("sub_name", list[p1].Name)
//            intent.putExtra("isCompetitive", list[p1].isCompetitive)
//            context.startActivity(intent)
        }
    }


    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var standard: TextView = itemView.findViewById(R.id.choosemp_tvstandardname)
        var subject: TextView = itemView.findViewById(R.id.choosemp_tvsujectname)
        var test: TextView = itemView.findViewById(R.id.choosemp_tvTest)
        var ll: ImageView = itemView.findViewById(R.id.image)

    }

}

