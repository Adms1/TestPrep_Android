package com.testcraft.testcraft.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.CoinActivity
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.NewActivity
import com.testcraft.testcraft.activity.ViewInvoiceActivity
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls

class OtherListAdapter(val context: Context, val dataList: ArrayList<String>) :
    RecyclerView.Adapter<OtherListAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(LayoutInflater.from(context).inflate(R.layout.other_list_item, p0, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {
        p0.text.text = dataList[p1]

        p0.text.setOnClickListener {
            when (dataList[p1]) {
                "Edit Profile" -> {

                    CommonWebCalls.callToken(
                        context,
                        "1",
                        "",
                        ActionIdData.C3301,
                        ActionIdData.T3301
                    )

                    AppConstants.isFirst = 8
                    DashboardActivity.setFragments(null)

//                    val intent1 = Intent(activity, DashboardActivity::class.java)
//                    intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent1)
//                    activity!!.finish()

//                    val intent = Intent(activity, UpdateProfileActivity::class.java)
//                    startActivity(intent)
                }
                "My Payments" -> {

                    CommonWebCalls.callToken(
                        context,
                        "1",
                        "",
                        ActionIdData.C3302,
                        ActionIdData.T3302
                    )

                    AppConstants.isFirst = 5
                    DashboardActivity.setFragments(null)

//                    val intent1 = Intent(activity, DashboardActivity::class.java)
//                    intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent1)
//                    activity!!.finish()

//                    val intent = Intent(activity, MyPaymentActivity::class.java)
//                    startActivity(intent)
                }
                "Add Amount" -> {

                    val intent = Intent(context, CoinActivity::class.java)
                    context.startActivity(intent)
                }
                "Change Password" -> {

                    CommonWebCalls.callToken(
                        context,
                        "1",
                        "",
                        ActionIdData.C3303,
                        ActionIdData.T3303
                    )

                    AppConstants.isFirst = 6
                    DashboardActivity.setFragments(null)

//                    val intent1 = Intent(activity, DashboardActivity::class.java)
//                    intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent1)
//                    activity!!.finish()

//                    val intent = Intent(activity, ChangePasswordActivity::class.java)
//                    intent.putExtra("come_from", "other")
//                    startActivity(intent)
                }
                "Change Prefrence" -> {

                    CommonWebCalls.callToken(
                        context,
                        "1",
                        "",
                        ActionIdData.C3305,
                        ActionIdData.T3305
                    )

                    val intent = Intent(context, NewActivity::class.java)
                    context.startActivity(intent)
                }

                "Help" -> {

                    val intent = Intent(context, ViewInvoiceActivity::class.java)
                    intent.putExtra("header", "Help")
                    context.startActivity(intent)

                }

                "Logout" -> {

                    CommonWebCalls.callToken(
                        context,
                        "1",
                        "",
                        ActionIdData.C3304,
                        ActionIdData.T3304
                    )

                    DashboardActivity.signOut()
                }

            }
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text: TextView = itemView.findViewById(R.id.other_item_text)

    }
}
