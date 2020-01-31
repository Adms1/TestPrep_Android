package com.testcraft.testcraft.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devs.readmoreoption.ReadMoreOption
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.CartActivity
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.TutorReviewActivity
import com.testcraft.testcraft.adapter.TutorPackageAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.models.TutorModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.fragment_tutor_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class TutorProfileFragment : Fragment() {

    var bundle: Bundle? = null

    var tutorid = ""
    var readMoreOption: ReadMoreOption? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_tutor_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2800, ActionIdData.T2800)

        bundle = this.arguments

        tutorid = bundle!!.getString("tutor_id")!!

        AppConstants.isFirst = 14

        readMoreOption = ReadMoreOption.Builder(activity!!)
            .textLength(3, ReadMoreOption.TYPE_LINE) // OR
//.textLength(300, ReadMoreOption.TYPE_CHARACTER)
            .moreLabel("View more")
            .lessLabel("...View less")
            .moreLabelColor(Color.BLUE)
            .lessLabelColor(Color.BLUE)
            .labelUnderLine(false)
            .expandAnimation(true)
            .build()

        tutor_item_rvCuratorList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        tutor_item_rvCuratorList.isNestedScrollingEnabled = false

        tutor_profile_ivCart.setOnClickListener {
            val intent = Intent(activity!!, CartActivity::class.java)
            startActivity(intent)
        }

//        tutor_profile_ivBack.setOnClickListener { onBackPressed() }

        llRating.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2801, ActionIdData.T2801)

            val intent = Intent(activity!!, TutorReviewActivity::class.java)
            intent.putExtra("header", tutor_profile_tvName.text.toString())
            intent.putExtra("tutorid", tutorid)
            startActivity(intent)
            activity!!.finish()
        }

        tutor_profile_tvMobile.setOnClickListener {
            val u = Uri.parse("tel:" + tutor_profile_tvMobile.text.toString())
            startActivity(Intent(Intent.ACTION_DIAL, u))
        }

        tutor_profile_tvEmail.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", tutor_profile_tvEmail.text.toString(), null)
            )
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            intent.putExtra(Intent.EXTRA_TEXT, "Sent from android")
            startActivity(intent)
        }

        callTutorPrfile()
        callSmilarPkgs()

    }

    fun callTutorPrfile() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorProfile(tutorid)
        call.enqueue(object : Callback<TutorModel> {
            override fun onResponse(call: Call<TutorModel>, response: Response<TutorModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        if (response.body()!!.data[0].InstituteName != "") {
                            DashboardActivity.main_header!!.text =
                                response.body()!!.data[0].InstituteName
//                            tutor_profile_header.text = response.body()!!.data[0].InstituteName
                        } else {
                            DashboardActivity.main_header!!.text =
                                response.body()!!.data[0].TutorName
//                            tutor_profile_header.text = response.body()!!.data[0].TutorName
                        }
                        tutor_profile_tvName.text = response.body()!!.data[0].TutorName
                        tutor_profile_tvEmail.text = response.body()!!.data[0].TutorEmail
                        tutor_profile_tvMobile.text = response.body()!!.data[0].TutorPhoneNumber
//                        tutor_profile_tvDesc.text = response.body()!!.data[0].TutorDescription + " "

                        val strdesc = response.body()!!.data[0].TutorDescription

                        readMoreOption!!.addReadMoreTo(tutor_profile_tvDesc, strdesc)

                        tutor_profile_tvCount.text =
                            "(" + response.body()!!.data[0].TotalRateCount + ")"
                        tutor_profile_rating.rating = response.body()!!.data[0].TutorStars.toFloat()

                        if (response.body()!!.data[0].TutorName != "") {
                            Picasso.get()
                                .load(AppConstants.IMAGE_BASE_URL + response.body()!!.data[0].Icon)
                                .into(tutor_profile_image)
                        }

//                        tutor_profile_image.text = response.body()!!.data[0].TutorName.substring(0, 1)

                    } else {

                        Log.e("", response.body()!!.Msg)
//                        Toast.makeText(
//                            activity!!,
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

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorSimilarPkgs(tutorid)
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        tutor_item_rvCuratorList.adapter =
                            TutorPackageAdapter(activity!!, response.body()!!.data)

                    } else {

                        Log.e("", response.body()!!.Msg)

//                        Toast.makeText(
//                            activity!!,
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
