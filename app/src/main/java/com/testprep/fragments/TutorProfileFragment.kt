package com.testprep.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.testprep.R
import com.testprep.activity.CartActivity
import com.testprep.adapter.TutorPackageAdapter
import com.testprep.models.PackageData
import com.testprep.models.TutorModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_tutor_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        callTutorPrfile()
        callSmilarPkgs()

    }

    fun callTutorPrfile() {

        if (!DialogUtils.isNetworkConnected(this@TutorProfileFragment)) {
            Utils.ping(this@TutorProfileFragment, "Connetion not available")
        }

        DialogUtils.showDialog(this@TutorProfileFragment)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorProfile(intent.getStringExtra("tutor_id"))
        call.enqueue(object : Callback<TutorModel> {
            override fun onResponse(call: Call<TutorModel>, response: Response<TutorModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        tutor_profile_header.text = response.body()!!.data[0].InstituteName
                        tutor_profile_tvName.text = response.body()!!.data[0].TutorName
                        tutor_profile_tvEmail.text = response.body()!!.data[0].TutorEmail
                        tutor_profile_tvMobile.text = response.body()!!.data[0].TutorPhoneNumber

                        tutor_profile_image.text = response.body()!!.data[0].TutorName.substring(0, 1)

                    } else {

//                        Toast.makeText(
//                            this@TutorProfileFragment,
//                            response.body()!!.Msg.replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<TutorModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callSmilarPkgs() {

        if (!DialogUtils.isNetworkConnected(this@TutorProfileFragment)) {
            Utils.ping(this@TutorProfileFragment, "Connetion not available")
        }

        DialogUtils.showDialog(this@TutorProfileFragment)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorSimilarPkgs(intent.getStringExtra("tutor_id"))
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        tutor_item_rvCuratorList.adapter =
                            TutorPackageAdapter(this@TutorProfileFragment, response.body()!!.data)

                    } else {

//                        Toast.makeText(
//                            this@TutorProfileFragment,
//                            response.body()!!.Msg.replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

}
