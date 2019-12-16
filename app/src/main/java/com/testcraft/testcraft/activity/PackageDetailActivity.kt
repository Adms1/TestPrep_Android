package com.testcraft.testcraft.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.adapter.TestTypeAdapter
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import com.testcraft.testcraft.utils.WebRequests
import kotlinx.android.synthetic.main.activity_package_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackageDetailActivity : Fragment() {

    var pkgid = ""
    var tutor_id = ""

    var purchaseCoin = ""
    var come = ""
    var oldpkgid = ""
    var bundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_package_detail, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DashboardActivity.main_header!!.text = "Package Details"
        DashboardActivity.btnBack!!.visibility = View.VISIBLE

        bundle = this.arguments

        come = bundle!!.getString("come_from")!!
        oldpkgid = bundle!!.getString("pkgid")!!

//        Log.d("colr1", " " + pos + " " + package_detail_tvPname.text.length)

//        package_detail_image1.setImageDrawable(Utils.createDrawable(pos, package_detail_tvPname.text.length))
        package_detail_tvlprice.paintFlags =
            package_detail_tvlprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        package_detail_rvList.isNestedScrollingEnabled = false
        package_detail_rvList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        if (come == "mypackage") {
//            package_detail_btnAddTocart.visibility = View.GONE
            package_detail_createdby.visibility = View.GONE
        }

        package_detail_createdby.setOnClickListener {

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

                        callAddToCart(oldpkgid)

//                        }else{
//                            callAddTestPackageApi(intent.getStringExtra("pkgid"))
//                        }

//            } else {
//                Utils.openVersionDialogCharge(this@CoinActivity)
//
//            }

                    } else {
                        Utils.ping(activity!!, "Network not available")
                    }

                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()


                }).show()
        }

        callTestPackageDetailApi()
    }

    fun chargeBtnLogic() {
//        com.testprep.utils.AppConstants.planid = "0"
        if (AppConstants.API_KEY.length > 5 && AppConstants.SECRET_KEY.length > 5) {
            var amount: Array<String>? = null

            var amt = purchaseCoin.toString().replace("₹", "").trim()

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

                    var temp = amt.replace(",", "")
                    var temp1 = temp.toFloat()

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
            Utils.ping(context, "Connetion not available")
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
            Utils.ping(activity!!, "Connetion not available")
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

                            package_detail_tvsprice.text =
                                response.body()!!.get("data")
                                    .asJsonObject.get("TestPackageSalePrice").asString

                            package_detail_tvlprice.text =
                                response.body()!!.get("data")
                                    .asJsonObject.get("TestPackageListPrice").asString.trim()
                        } else {

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


                        var testList: JsonArray? =
                            response.body()!!.get("data").asJsonObject.get("TestList").asJsonArray

                        package_detail_rvList.adapter = TestTypeAdapter(
                            activity!!, testList!!
                        )
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
            Utils.ping(activity!!, "Connetion not available")
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

    //    {"Status":"true","data":"24","Msg":"Package added into cart"}
    fun callAddToCart(pkgid: String) {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.addToCart(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            pkgid
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    if (response.body()!!["Status"].asString == "true") {

//                        fragmentManager!!.beginTransaction().replace(R.id.container, ChooseMarketPlaceFragment()).commit()

//                        Toast.makeText(
//                            activity!!,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()

//                        val intent = Intent(activity, CartActivity::class.java)
//                        startActivity(intent)
//
                        callCheckout()

                    } else {

                        DialogUtils.dismissDialog()

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

    //    {"Status":"true","data":[{"PaymentTransactionID":92,"OrderID":"TP190905112816426","PaymentAmount":"100"}],"Msg":"Generate New payment Order ID "}
    fun callCheckout() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkout(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        if (!response.body()!!["data"].asJsonArray[0].asJsonObject["PaymentAmount"].asString.equals(
                                "0",
                                true
                            )
                        ) {

                            Log.v(
                                "order_id: ",
                                "" + response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
                            )

                            val intent = Intent(activity!!, TraknpayRequestActivity::class.java)
                            intent.putExtra(
                                "order_id",
                                response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
                            )
                            intent.putExtra(
                                "amount",
                                response.body()!!["data"].asJsonArray[0].asJsonObject["PaymentAmount"].asString
                            )
                            intent.putExtra("pkgid", pkgid)
                            intent.putExtra("pkgname", package_detail_tvPname.text.toString())
                            intent.putExtra("pkgprice", purchaseCoin)
                            startActivity(intent)
//                            (context as DashboardActivity).finish()

                        } else {
                            updatePaymentStatus(
                                "Success",
                                response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
                            )
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

    fun updatePaymentStatus(transaction_status: String, order_id: String) {
        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.updatePaymentStatus(
            WebRequests.getPaymentStatusParams(
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "")!!,
                order_id,
                "0",
                transaction_status
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

//                    if (response.body()!!["Status"].asString == "true") {

//                        if (transaction_status == "Success") {

//                            Toast.makeText(
//                                activity!!,
//                               "Free package buy successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()

//                            Handler().postDelayed(
//
//                                /* Runnable
//                                 * Showing splash screen with a timer. This will be useful when you
//                                 * want to show case your app logo / company
//                                 */
//
//                                {
//                                    // This method will be executed once the timer is over
//                                    // Start your app main activity
////                                    val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
////                                    startActivity(intent)
////                                    overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
//
//                                    // close this activity
////                                    finish()
//                                    onBackPressed()
//                                }, 1500


//                            )

//                            callAddTestPackageApi()

//                        }

//                    } else {

                    AppConstants.isFirst = 1

                    var intent = Intent(activity!!, DashboardActivity::class.java)
                    startActivity(intent)
                    (context as DashboardActivity).finish()

//                        Toast.makeText(activity!!, response.body()!!["Msg"].asString, Toast.LENGTH_LONG)
//                            .show()


//                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })

    }

}