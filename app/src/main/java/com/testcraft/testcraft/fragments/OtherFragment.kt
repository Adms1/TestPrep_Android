package com.testcraft.testcraft.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.activity.IntroActivity
import com.testcraft.testcraft.adapter.OtherListAdapter
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.fragment_other.*

class OtherFragment : Fragment() {

    var menuList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C3300, ActionIdData.T3300)

        when (Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "")) {
            "1"  -> {
                menuList.add("My Payments")
                menuList.add("My subscription")
                menuList.add("Change Preference")
                menuList.add("Refer a Friend")
                menuList.add("Privacy Policy")
                menuList.add("Contact Us")
                menuList.add("Sign out")
            }
            "5"  -> {
                menuList.add("My Payments")
                menuList.add("My subscription")
                menuList.add("Change Preference")
                menuList.add("Refer a Friend")
                menuList.add("Privacy Policy")
                menuList.add("Contact Us")
                menuList.add("Sign out")
            }
            else -> {
                menuList.add("My Payments")
                menuList.add("My subscription")
                menuList.add("Change Preference")
                menuList.add("Refer a Friend")
                menuList.add("Privacy Policy")
                menuList.add("Contact Us")
                menuList.add("Sign out")
            }
        }
//        menuList.add("Logout")

        other_tvChangeUser!!.setOnClickListener {

            IntroActivity.disconnectFromFacebook()

            DashboardActivity.mGoogleSignInClient!!.signOut()
                .addOnCompleteListener(DashboardActivity.context as DashboardActivity) {
                    // ...

                    val intent = Intent(DashboardActivity.context, IntroActivity::class.java)
                    context!!.startActivity(intent)
                }

//            val intent = Intent(context, IntroActivity::class.java)
//            startActivity(intent)
        }

//        if (Utils.getStringValue(activity!!, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {

//            other_ivEdit.visibility = View.VISIBLE
//
//        } else {
//            other_ivEdit.visibility = View.GONE
//        }

        other_lvList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        other_lvList.adapter = OtherListAdapter(activity!!, menuList)

        other_ivEdit.setOnClickListener {
            AppConstants.isFirst = 8
            setFragments(null)
        }

        if (Utils.getStringValue(activity!!, AppConstants.user_profile, "") != "") {

            other_tvLogo.visibility = View.GONE
            Picasso.get().load(Utils.getStringValue(activity!!, AppConstants.user_profile, ""))
                .placeholder(R.drawable.person_placeholder).into(other_ivLogo)

        } else {

            other_tvLogo.visibility = View.VISIBLE

            other_ivLogo.setImageResource(R.drawable.blue_round)
            other_tvLogo.text =
                Utils.getStringValue(activity!!, AppConstants.FIRST_NAME, "").toString()
                    .substring(0, 1).toUpperCase()

        }
        other_tvUserName.text =
            Utils.getStringValue(
                activity!!,
                AppConstants.FIRST_NAME,
                ""
            ) + " " + Utils.getStringValue(
                activity!!,
                AppConstants.LAST_NAME,
                ""
            )

        if(Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "") == "5"){
            other_tvUserEmail.text = Utils.getStringValue(activity!!, AppConstants.USER_MOBILE, "")
        }else {
            other_tvUserEmail.text = Utils.getStringValue(activity!!, AppConstants.USER_EMAIL, "")
        }

    }
}
