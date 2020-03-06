package com.testcraft.testcraft.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.activity.IntroActivity
import com.testcraft.testcraft.activity.TraknpayRequestActivity
import com.testcraft.testcraft.adapter.TestTypeAdapter
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.fragment_package_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackageDetailFragment : Fragment() {

    var pkgid = ""
    var tutor_id = ""

    var validation_id = "0"

    var purchaseCoin = ""
    var come = ""
    var oldpkgid = ""
    var bundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_package_detail, container, false)

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1100, ActionIdData.T1100)

        DashboardActivity.main_header!!.text = "Package Details"
        DashboardActivity.btnBack!!.visibility = View.VISIBLE

        bundle = this.arguments

        come = bundle!!.getString("come_from")!!
        oldpkgid = bundle!!.getString("pkgid")!!

//        Log.d("colr1", " " + pos + " " + package_detail_tvPname.text.length)

//        package_detail_image1.setImageDrawable(Utils.createDrawable(pos, package_detail_tvPname.text.length))
        package_detail_tvlprice.paintFlags =
            package_detail_tvlprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        Utils.setFont(activity!!, "fonts/Inter-Medium.ttf", package_detail_tvlprice)
        Utils.setFont(activity!!, "fonts/Inter-Medium.ttf", package_detail_tvsprice)

        package_detail_rvList.isNestedScrollingEnabled = false
        package_detail_rvList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        if (come == "mypackage") {
//            package_detail_btnAddTocart.visibility = View.GONE
            package_detail_createdby.visibility = View.GONE
        }

        package_detail_btnPcode.setOnClickListener {

            if (package_detail_etPcode.text.toString() != "") {
                callValidateCCode()

            } else {

                package_detail_tvValid.visibility = View.VISIBLE
                package_detail_tvValid!!.setTextColor(resources.getColor(R.color.red))
                package_detail_tvValid!!.text = "Please enter coupon code"
            }
        }

        package_detail_createdby.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1102, ActionIdData.T1102)

//            val intent = Intent(activity!!, TutorProfileFragment::class.java)
//            intent.putExtra("tutor_id", tutor_id)
//            startActivity(intent)

            AppConstants.isFirst = 15
            val bundle = Bundle()
            bundle.putString("tutor_id", tutor_id)
            setFragments(bundle)

        }

//        package_detail_ivBack.setOnClickListener {
//            onBackPressed()
//        }

        package_detail_btnAddTocart.setOnClickListener {

            if (Utils.getStringValue(activity!!, AppConstants.IS_LOGIN, "") == "true") {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C1101,
                    ActionIdData.T1101
                )

                //            val dialog = Dialog(activity)
//            dialog.setContentView(R.layout.dialog_verify_number)
//            dialog.setCanceledOnTouchOutside(false)
//            dialog.setCancelable(false)
//
//            val header: TextView = dialog.findViewById(R.id.dialog_verify_tvHeader)
//
//            val etMobile: EditText =
//                dialog.findViewById(R.id.dialog_verify_etMobile)
//            val submit: TextView =
//                dialog.findViewById(R.id.dialog_verify_btnSubmit)
//
//            header.text = "Please enter your promo code"
//            etMobile.hint = "Promo code"
//
//            val skip: TextView = dialog!!.findViewById(R.id.dialog_verify_btnSkip)
//
//            skip.visibility = View.VISIBLE
//
//                skip.setOnClickListener{
//                    dialog!!.dismiss()
//                }
//
//            submit.setOnClickListener {
//
//            }
//
//            dialog.show()`    

                DialogUtils.createConfirmDialog(
                    activity!!,
                    "",
                    "Are you sure you want to buy this package?",
                    "Yes",
                    "No",
                    DialogInterface.OnClickListener { dialog, which ->

                        if (DialogUtils.isNetworkConnected(activity!!)) {

//            if (isVersionCodeUpdated) {

//                        if(!purchaseCoin.equals("free", true)) {

                            if (validation_id == "1" || validation_id == "0") {
                                PackagePurchase.callAddToCart("freetest",
                                    oldpkgid,
                                    activity!!,
                                    package_detail_etPcode.text.toString()
                                )
                            } else {
                                PackagePurchase.callAddToCart("freetest",
                                    oldpkgid,
                                    activity!!,
                                    ""
                                )
                            }

//                        }else{
//                            callAddTestPackageApi(intent.getStringExtra("pkgid"))
//                        }

//            } else {
//                Utils.openVersionDialogCharge(this@CoinActivity)
//
//            }

                        } else {
                            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
                        }

                    },
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()

                    }).show()
            } else {

                val intent = Intent(activity!!, IntroActivity::class.java)
                startActivity(intent)

            }
        }

        callTestPackageDetailApi()
    }

    fun chargeBtnLogic() {
//        com.testprep.utils.AppConstants.planid = "0"
        if (AppConstants.API_KEY.length > 5 && AppConstants.SECRET_KEY.length > 5) {
            var amount: Array<String>? = null

            val amt = purchaseCoin.replace("₹", "").trim()

            if (amt.contains("."))
                amount = amt.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()//to check decimal places

            if (amt.equals("", ignoreCase = true)) {
                Utils.ping(activity!!, resources.getString(R.string.enter_coin))
            } else if (amount != null && amount[1].length > 2) {
                Utils.ping(activity!!, "Please provide upto 2 decimal places only.")
            } else {
                //SHRENIK IS HERE.....
                /*if(1==2) {
                        //Insta mojo code.
                        fetchTokenAndTransactionID();
                    }*/
                try {
                    //                            verifyCard();
//                    val cardResult = verifyCardFromUSBReader()
//                    if (cardResult.equals("NoReader", ignoreCase = true)) {
//                        isCardPresent = false
//                        openDialog(
//                            "No Card Reader Found",
//                            "If you have a card reader then please RETRY by inserting a card reader with a valid card or click OK to proceed to Manual Entry."
//                        )
//
//                    } else if (cardResult.equals("NoCard", ignoreCase = true)) {
//                        isCardPresent = false
//                        openDialog(
//                            "Card Not Found",
//                            "Either the card is not inserted or the card you inserted is not supported so please RETRY by inserting a valid card with a chip or click OK to proceed for the manual entry."
//                        )
//
//                    } else {
//                        isCardPresent = true
//                        Log.v("Carddetail: ", cardResult)
//                        generateTrackNPayRequest()
                    //                            fetchTokenAndTransactionID();
                    //Custom UI

                    val temp = amt.replace(",", "")
                    val temp1 = temp.toFloat()

                    generateTrackNPayRequest(activity!!, temp1.toString())

//                    }
                } catch (e: Exception) {
                    //SendMessage(MESSAGE_ERROR, e.getMessage());
                    Utils.ping(activity!!, e.message.toString())
                }

            }/*else if (Double.parseDouble(edtAmount.getText().toString()) < 2.00) {
                        com.testprep.utils.Utils.ping(this@CoinActivity, "Amount can't be more than Rs. 100000.00");
                    }*/
//            Instamojo.setLogLevel(Log.DEBUG)
        } else {
            Utils.openInvalidApiKeyDialog(activity!!)
        }
    }

    fun generateTrackNPayRequest(context: Context, coin: String) {
        if (!DialogUtils.isNetworkConnected(context)) {
            Utils.ping(context, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(context)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

//        if (isBoolean_permission_phoneState) {
//            hashMap["IMEINumber"] = Utils.getIMEI(context)
//        } else {
//            hashMap["IMEINumber"] = ""
//        }
//
//        if (isBoolean_permission_location) {
//            val loc = Utils.getLocation(context)
//            hashMap["Latitude"] = loc[0].toString()
//            hashMap["Longitude"] = loc[1].toString()
//        } else {
//            hashMap["Latitude"] = ""
//            hashMap["Longitude"] = ""
//        }

        val call = apiService.getPayment(
            WebRequests.getPaymentParams(
                "0", Utils.getStringValue(
                    context,
                    AppConstants.USER_ID,
                    ""
                )!!, coin
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Log.v(
                            "order_id: ",
                            "" + response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].toString().replace(
                                "\"",
                                ""
                            )
                        )

                        val intent = Intent(context, TraknpayRequestActivity::class.java)
                        intent.putExtra(
                            "order_id",
                            response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].toString().replace(
                                "\"",
                                ""
                            )
                        )
                        intent.putExtra("amount", coin)
                        intent.putExtra("pkgid", pkgid)
                        intent.putExtra("pkgname", package_detail_tvPname.text.toString())
                        intent.putExtra("pkgprice", purchaseCoin)
                        context.startActivity(intent)
//                        (context as DashboardActivity).finish()

                    } else {
                        Toast.makeText(
                            context,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })

    }

    fun callTestPackageDetailApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getPackageDetail(
            oldpkgid,
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        purchaseCoin =
                            response.body()!!.get("data").asJsonObject.get("TestPackageSalePrice")
                                .asString

                        package_detail_tvPname.text =
                            response.body()!!.get("data").asJsonObject.get("TestPackageName")
                                .asString

                        val testList: JsonArray? =
                            response.body()!!.get("data").asJsonObject.get("TestList").asJsonArray

                        if (response.body()!!.get("data").asJsonObject.get("Icon").asString != null) {
                            Picasso.get()
                                .load(
                                    AppConstants.IMAGE_BASE_URL + response.body()!!.get("data").asJsonObject.get(
                                        "Icon"
                                    ).asString
                                )
                                .into(package_detail_image1)
                        }

                        if (response.body()!!.get("data").asJsonObject.get("TestPackageSalePrice").asString.equals(
                                "Free",
                                true
                            )
                        ) {

                            package_detail_btnAddTocart.text = "Start Test"

                            package_detail_etPcode!!.visibility = View.GONE
                            package_detail_btnPcode!!.visibility = View.GONE

                            package_detail_tvsprice.text =
                                response.body()!!.get("data")
                                    .asJsonObject.get("TestPackageSalePrice").asString

                            package_detail_tvlprice.text =
                                response.body()!!.get("data")
                                    .asJsonObject.get("TestPackageListPrice").asString.trim()

                        } else {

                            package_detail_btnAddTocart.text = "Buy"

                            if (testList!!.size() == 1) {
                                package_detail_etPcode!!.visibility = View.VISIBLE
                                package_detail_btnPcode!!.visibility = View.VISIBLE
                            } else {
                                package_detail_etPcode!!.visibility = View.GONE
                                package_detail_btnPcode!!.visibility = View.GONE
                            }

//                            package_detail_tvlpricetxt.visibility = View.GONE

                            package_detail_tvsprice.text =
                                response.body()!!.get("data")
                                    .asJsonObject.get("TestPackageSalePrice").asString

                            package_detail_tvlprice.text = ""
                        }

//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        package_detail_tvDesc.text =
                            response.body()!!.get("data").asJsonObject.get("TestPackageDescription")
                                .asString

//                        } else {
//                            package_detail_tvDesc.text =
//                                Html.fromHtml(response.body()!!.get("data").asJsonObject.get("TestPackageDescription").asString)
//                        }

                        if (response.body()!!.get("data").asJsonObject.get("TestPackageName").asString != "") {
                            package_detail_name_short.text =
                                response.body()!!.get("data").asJsonObject.get("TestPackageName")
                                    .asString.substring(0, 1)
                        }

//                        if (response.body()!!.get("data").asJsonObject.get("InstituteName").asString != "" && response.body()!!.get(
//                                "data"
//                            ).asJsonObject.get("InstituteName").asString != null
//                        ) {
//                            package_detail_createdby.text =
//                                Html.fromHtml(
//                                    "Created by " + "<font color=\"#3ea7e0\">" + response.body()!!.get(
//                                        "data"
//                                    ).asJsonObject.get(
//                                        "InstituteName"
//                                    ).asString + "</font>"
//                                )
//                        } else {

                        package_detail_tvSubject.text =
                            response.body()!!.get("data").asJsonObject.get("SubjectName").asString

                        package_detail_createdby.text =
                            Html.fromHtml(
                                "By " + "<font color=\"#3ea7e0\">" + response.body()!!.get(
                                    "data"
                                ).asJsonObject.get(
                                    "TutorName"
                                ).asString + "</font>"
                            )
//                        }

                        Log.d("pkgid", oldpkgid)

                        pkgid =
                            response.body()!!.get("data").asJsonObject.get("TestPackageID").asString
                        tutor_id =
                            response.body()!!.get("data").asJsonObject.get("TutorID").asString

//                        var pos = intent.getCharExtra("position", 'a')

                        if (testList!!.size() > 0) {
                            package_detail_rvList.adapter = TestTypeAdapter(activity!!, testList)

                        }

                    } else {

                        Toast.makeText(
                            activity!!,
                            response.body()!!["Msg"].toString().replace("\"", ""),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callAddTestPackageApi(pkgid: String) {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.addTestPackage(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            pkgid
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        AppConstants.isFirst = 1

//                        fragmentManager!!.beginTransaction().replace(R.id.container, ChooseMarketPlaceFragment()).commit()

                        val intent = Intent(activity!!, DashboardActivity::class.java)
                        startActivity(intent)
                        (context as DashboardActivity).finish()

                        Toast.makeText(
                            activity!!,
                            response.body()!!["Msg"].toString().replace("\"", ""),
                            Toast.LENGTH_SHORT
                        ).show()
//                        onBackPressed()

                    } else {

                        Toast.makeText(
                            activity!!,
                            response.body()!!["Msg"].toString().replace("\"", ""),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callValidateCCode() {

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getValidateCouponCode(
            Utils.getStringValue(
                activity!!,
                AppConstants.USER_ID,
                "0"
            )!!, package_detail_etPcode.text.toString()
        )

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                package_detail_tvValid.visibility = View.VISIBLE
                package_detail_tvValid!!.text =
                    response.body()!!.get("data").asJsonObject["ValidationDesc"].asString

                validation_id = response.body()!!.get("data").asJsonObject["ValidationID"].asString

                if (response.body()!!.get("Status").asString == "true") {

                    package_detail_tvValid!!.setTextColor(resources.getColor(R.color.green))

                } else {

                    package_detail_tvValid!!.setTextColor(resources.getColor(R.color.red))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }


}
