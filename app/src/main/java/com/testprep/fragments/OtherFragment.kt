package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.activity.DashboardActivity.Companion.setFragments
import com.testprep.adapter.OtherListAdapter
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_other.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class OtherFragment : Fragment() {

    var menuList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.testprep.R.layout.fragment_other, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "") == "1") {
//            menuList.add("Edit Profile")
            menuList.add("My Payments")
            menuList.add("Change Password")
            menuList.add("Change Prefrence")
//            menuList.add("Help")
//            menuList.add("About Us")
            menuList.add("Logout")
        } else {
//            menuList.add("Edit Profile")
            menuList.add("My Payments")
            menuList.add("Change Prefrence")
//            menuList.add("Help")
//            menuList.add("About Us")
            menuList.add("Logout")
        }
//        menuList.add("Logout")

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

        other_tvUserEmail.text = Utils.getStringValue(activity!!, AppConstants.USER_EMAIL, "")

    }
}
