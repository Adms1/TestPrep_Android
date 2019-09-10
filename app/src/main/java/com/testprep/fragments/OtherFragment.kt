package com.testprep.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import com.testprep.R
import com.testprep.activity.*
import com.testprep.activity.NewActivity
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
            menuList.add("Edit Profile")
            menuList.add("My Payments")
            menuList.add("Change Password")
            menuList.add("Change Prefrence")
        } else {
            menuList.add("Edit Profile")
            menuList.add("My Payments")
            menuList.add("Change Prefrence")
        }
//        menuList.add("Logout")

        other_lvList.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, menuList)

        other_lvList.onItemClickListener = OnItemClickListener { parent, view, position, id ->

            when {
                menuList[position] == "Edit Profile" -> {

                    AppConstants.isFirst = 8
                    val intent1 = Intent(activity, DashboardActivity::class.java)
                    intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent1)
                    activity!!.finish()

//                    val intent = Intent(activity, UpdateProfileActivity::class.java)
//                    startActivity(intent)
                }
                menuList[position] == "My Payments" -> {

                    AppConstants.isFirst = 5
                    val intent1 = Intent(activity, DashboardActivity::class.java)
                    intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent1)
                    activity!!.finish()
//                    val intent = Intent(activity, MyPaymentActivity::class.java)
//                    startActivity(intent)
                }
                menuList[position] == "Add Amount" -> {

                    val intent = Intent(activity, CoinActivity::class.java)
                    startActivity(intent)
                }
                menuList[position] == "Change Password" -> {

                    AppConstants.isFirst = 6
                    val intent1 = Intent(activity, DashboardActivity::class.java)
                    intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent1)
                    activity!!.finish()

//                    val intent = Intent(activity, ChangePasswordActivity::class.java)
//                    intent.putExtra("come_from", "other")
//                    startActivity(intent)
                }
                menuList[position] == "Change Prefrence" -> {

                    val intent = Intent(activity, NewActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

}
