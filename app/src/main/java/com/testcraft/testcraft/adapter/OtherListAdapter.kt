package com.testcraft.testcraft.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.*
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils

class OtherListAdapter(val context: Context, val dataList: ArrayList<String>) :
    RecyclerView.Adapter<OtherListAdapter.Viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Viewholder {

        return Viewholder(LayoutInflater.from(context).inflate(R.layout.other_list_item, p0, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: Viewholder, p1: Int) {
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
                "My Subscription" -> {
                    AppConstants.isFirst = 19
                    DashboardActivity.setFragments(null)
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
                "Change Preference" -> {

                    CommonWebCalls.callToken(
                        context,
                        "1",
                        "",
                        ActionIdData.C3305,
                        ActionIdData.T3305
                    )

                    val intent = Intent(context, NewActivity::class.java)
                    intent.putExtra("comeadater", "changeprefrence")
                    context.startActivity(intent)
                }

                "Refer a Friend" -> {

                    AppConstants.isFirst = 17
                    DashboardActivity.setFragments(null)
                }

                "Redeem Coupon" -> {

                    AppConstants.isFirst = 18
                    DashboardActivity.setFragments(null)
                }

                "Help" -> {

                    val intent = Intent(context, ViewInvoiceActivity::class.java)
                    intent.putExtra("header", "Help")
                    context.startActivity(intent)

                }

                "Contact Us" -> {

                    AppConstants.isFirst = 16
                    DashboardActivity.setFragments(null)

                }

                "Privacy Policy" -> {

                    val intent = Intent(context, ViewInvoiceActivity::class.java)
                    intent.putExtra("header", "Privacy Policy")
                    intent.putExtra(
                        "url",
                        "http://testcraft.in/PrivacyPolicyMobile.aspx"
                    )

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

                    AppConstants.isFirst = 0
                    DashboardActivity.signOut()
                }

                "Sign in" -> {

                    IntroActivity.disconnectFromFacebook()

                    DashboardActivity.mGoogleSignInClient!!.signOut()
                        .addOnCompleteListener(DashboardActivity.context as DashboardActivity) {
                            // ...

                            val intent = Intent(DashboardActivity.context, IntroActivity::class.java)
                            context.startActivity(intent)
                        }

                }

                "Sign Out" -> {

                    IntroActivity.disconnectFromFacebook()

                    AppConstants.isFirst = 0

                    Utils.clearPrefrence(context)

                    DashboardActivity.mGoogleSignInClient!!.signOut()
                        .addOnCompleteListener(DashboardActivity.context as DashboardActivity) {
                            // ...

                            val intent =
                                Intent(DashboardActivity.context, IntroActivity::class.java)
                            context.startActivity(intent)
                        }

                }

            }
        }
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text: TextView = itemView.findViewById(R.id.other_item_text)

    }
}
