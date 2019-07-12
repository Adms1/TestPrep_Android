package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.fragments.TutorPackagesFragment

class TutorsAdapter(val context: Context) :
    RecyclerView.Adapter<TutorsAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_tutors, p0, false)
//            LayoutInflater.from(context).inflate(R.layout.new_prefrence_item_layout, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {
//        if (dataList[p1].Icon == null) {
//
//            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
//            p0.stitle.text = "M"
//            p0.p_select.visibility = View.VISIBLE
//
//        } else {
//            Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)
//
//        }

        p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
        p0.stitle.text = "B"

        p0.clMain.setOnClickListener {

            val intent = Intent(context, TutorPackagesFragment::class.java)
            context.startActivity(intent)

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.tutor_image)
        var title: TextView = itemView.findViewById(R.id.tutor_item_tvName)
        var stitle: TextView = itemView.findViewById(R.id.tutor_name_short)
        var next: ImageView = itemView.findViewById(R.id.tutor_item_btnNext)
        var clMain: ConstraintLayout = itemView.findViewById(R.id.tutor_clMain)
    }
}


