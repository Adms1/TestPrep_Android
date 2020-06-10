package com.testcraft.testcraft.adapter

import android.content.Context
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.SubscriptionActivity
import com.testcraft.testcraft.interfaces.CoinInteface
import com.testcraft.testcraft.interfaces.SubscriptionInterface
import com.testcraft.testcraft.models.GetSubscriptionModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionSubjectAdapter(val context: Context, val dataList: ArrayList<GetSubscriptionModel.GetSubscriptionData>, val iinterface: SubscriptionInterface) : RecyclerView.Adapter<SubscriptionSubjectAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(LayoutInflater.from(context).inflate(R.layout.subscription_subject_item_layout, p0, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        Picasso.get().load(dataList[p1].).into(p0.simg)

        if(dataList[p1].CourseTypeID == "1") {
            p0.name.text = dataList[p1].BoardName + "\n" + dataList[p1].StandardName
        }else{
            p0.name.text = dataList[p1].CourseName
        }

        p0.remove.setOnClickListener {

            iinterface.getRemove(dataList[p1].CourseTypeID, dataList[p1].CourseID, dataList[p1].StandardID, dataList[p1].BoardID)

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        val simg: ImageView = itemView.findViewById(R.id.ss_image)
        val remove: ImageView = itemView.findViewById(R.id.ss_remove)
        val name: TextView = itemView.findViewById(R.id.ss_name_short)

    }

}