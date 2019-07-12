package com.testprep.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.activity.CartActivity
import com.testprep.adapter.TutorsAdapter
import kotlinx.android.synthetic.main.fragment_tutor_profile.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TutorProfileFragment : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.fragment_tutor_profile)

        tutor_item_rvCuratorList.layoutManager =
            LinearLayoutManager(this@TutorProfileFragment, LinearLayoutManager.VERTICAL, false)
        tutor_item_rvCuratorList.adapter = TutorsAdapter(this@TutorProfileFragment)

        tutor_profile_ivCart.setOnClickListener {
            val intent = Intent(this@TutorProfileFragment, CartActivity::class.java)
            startActivity(intent)
        }

        tutor_profile_ivBack.setOnClickListener { onBackPressed() }

        llRating.setOnClickListener {
            val intent = Intent(this@TutorProfileFragment, TutorsReviewFragment::class.java)
            startActivity(intent)
        }

//        tutor_profile_tvCount.setOnClickListener {
//            val intent = Intent(this@TutorProfileFragment, TutorsReviewFragment::class.java)
//            startActivity(intent)
//        }

//        tutor_profile_tvViewProfile.setOnClickListener {
//
//            val intent = Intent(this@TutorProfileFragment, TutorProfileFragment::class.java)
//            startActivity(intent)
//
//        }

    }

}
