package com.testcraft.testcraft.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.pg.secure.pgsdkv4.PGConstants
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer
import com.test.pg.secure.pgsdkv4.PaymentParams
import com.testcraft.testcraft.R
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.SampleAppConstants
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_payment_view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class TraknpayNewActivity : AppCompatActivity() {

    //    var pb: ProgressBar? = null
//    var transactionIdView: TextView? = null
//    var transactionStatusView: TextView? = null
    var order_id: String? = null

    //    var desc: String? = null
//    var card_number: String? = null
//    var card_name: String? = null
//    var card_exp_month: String? = null
//    var card_exp_year: String? = null
    private var extras: Bundle? = null

    var pkgname = ""
    var pkgprice = ""
    var comefrom = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traknpay_new)

//        pb.setVisibility(View.VISIBLE);
        extras = intent.extras
        if (extras != null) {
//            mode = "TEST";

            pkgname = intent.getStringExtra("pkgname")!!
            pkgprice = intent.getStringExtra("amount")!!
            order_id = intent.getStringExtra("order_id")
            comefrom = intent.getStringExtra("comefrom")!!

//            if (extras!!.containsKey("CardDetails")) {
//                val cardDetails = extras!!.getString("CardDetails")!!
//                val details =
//                    cardDetails.split("\\-".toRegex()).toTypedArray() //card.CardNumber + "-" + card.CardHolderName + " - " + card.ValidToDate
//                var expiryDate = details[2]
//                expiryDate =
//                    expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, expiryDate.length)
//                card_number = details[0]
//                //card_name = details[1].toString().replace("/", "").trim();22-12-2016 old
//                card_name =
//                    details[1].replace("[;\\/:*?\"<>|&']".toRegex(), "") //22-12-2016 navin
//                card_exp_month = expiryDate.split("\\/".toRegex()).toTypedArray()[0]
//                card_exp_year = expiryDate.split("\\/".toRegex()).toTypedArray()[1]
//            }
        }
        val rnd = Random()
        val n = 100000 + rnd.nextInt(900000)
        SampleAppConstants.PG_ORDER_ID = Integer.toString(n)
        val pgPaymentParams = PaymentParams()
        pgPaymentParams.aPiKey = SampleAppConstants.PG_API_KEY
        pgPaymentParams.amount = pkgprice
        pgPaymentParams.email = SampleAppConstants.PG_EMAIL
        pgPaymentParams.name = Utils.getStringValue(
            this@TraknpayNewActivity,
            AppConstants.FIRST_NAME,
            ""
        ) + " " + Utils.getStringValue(this@TraknpayNewActivity, AppConstants.LAST_NAME, "")
        pgPaymentParams.phone = SampleAppConstants.PG_PHONE
        pgPaymentParams.orderId = order_id
        pgPaymentParams.currency = SampleAppConstants.PG_CURRENCY
        pgPaymentParams.description = "purchase"
        pgPaymentParams.city = SampleAppConstants.PG_CITY
        pgPaymentParams.state = SampleAppConstants.PG_STATE
        pgPaymentParams.addressLine1 = SampleAppConstants.PG_ADD_1
        pgPaymentParams.addressLine2 = SampleAppConstants.PG_ADD_2
        pgPaymentParams.zipCode = SampleAppConstants.PG_ZIPCODE
        pgPaymentParams.country = SampleAppConstants.PG_COUNTRY
        pgPaymentParams.setReturnUrl(SampleAppConstants.PG_RETURN_URL)
        pgPaymentParams.mode = SampleAppConstants.PG_MODE
        pgPaymentParams.setUdf1(SampleAppConstants.PG_UDF1)
        pgPaymentParams.setUdf2(SampleAppConstants.PG_UDF2)
        pgPaymentParams.setUdf3(SampleAppConstants.PG_UDF3)
        pgPaymentParams.setUdf4(SampleAppConstants.PG_UDF4)
        pgPaymentParams.setUdf5(SampleAppConstants.PG_UDF5)
        pgPaymentParams.enableAutoRefund = "n"
        pgPaymentParams.offerCode = ""
        //pgPaymentParams.setSplitInfo("{\"vendors\":[{\"vendor_code\":\"24VEN985\",\"split_amount_percentage\":\"20\"}]}");
        val pgPaymentInitialzer =
            PaymentGatewayPaymentInitializer(pgPaymentParams, this@TraknpayNewActivity)
        pgPaymentInitialzer.initiatePaymentProcess()
    }

    public override fun onStop() {
        super.onStop()
        //        pb.setVisibility(View.GONE);
    }

    override fun onBackPressed() {
        super.onBackPressed()

//        DialogUtils.createConfirmDialog(this@TraknpayNewActivity, "",
//            "Are you sure you want to cancel payment?",
//            "OK", "Cancel",
////
//            DialogInterface.OnClickListener { dialog, which ->
////

//        AppConstants.isFirst = 3
//        val intent = Intent(this@TraknpayNewActivity, DashboardActivity::class.java)
//        startActivity(intent)
        finish()
//            },
//            DialogInterface.OnClickListener { dialog, which ->
//
//                recreate()
//                dialog.dismiss()
//
//            }).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PGConstants.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    val paymentResponse = data!!.getStringExtra(PGConstants.PAYMENT_RESPONSE)
                    println("paymentResponse: $paymentResponse")
                    if (paymentResponse == "null") {
                        println("Transaction Error!")

                        val response = JSONObject(paymentResponse)

                        val intent = Intent(applicationContext, PaymentSuccessScreen::class.java)
                        intent.putExtra("comefrom", comefrom)
                        intent.putExtra("transactionId", response.getString("transaction_id"))
                        intent.putExtra("responseCode", response.getString("response_code"))
                        intent.putExtra("amount", response.getString("amount"))
                        intent.putExtra("description", response.getString("description"))
                        intent.putExtra("order_id", response.getString("order_id"))
//                    intent.putExtra("pkgid", pkgid)
                        intent.putExtra("pkgname", pkgname)
                        intent.putExtra("pkgprice", pkgprice)
//                if (extras!!.containsKey("CardDetails")) {
//                    intent.putExtra("Trans_Type", "1")
//                } else {
//                    intent.putExtra("Trans_Type", "2")
//                }
                        startActivity(intent)
                        finish()

//                                            transactionIdView.setText("Transaction ID: NIL");
//                        transactionStatusView.setText("Transaction Status: Transaction Error!");
                    } else {
                        val response = JSONObject(paymentResponse)

                        if (response.getString("response_code").equals("0", ignoreCase = true)) {
//                    callAddTestPackageApi()

                            val intent =
                                Intent(applicationContext, PaymentSuccessScreen::class.java)
                            intent.putExtra("comefrom", comefrom)
                            intent.putExtra("transactionId", response.getString("transaction_id"))
                            intent.putExtra("responseCode", response.getString("response_code"))
                            intent.putExtra("amount", response.getString("amount"))
                            intent.putExtra("description", response.getString("description"))
                            intent.putExtra("order_id", response.getString("order_id"))
//                    intent.putExtra("pkgid", pkgid)
                            intent.putExtra("pkgname", pkgname)
                            intent.putExtra("pkgprice", pkgprice)
//                if (extras!!.containsKey("CardDetails")) {
//                    intent.putExtra("Trans_Type", "1")
//                } else {
//                    intent.putExtra("Trans_Type", "2")
//                }
                            startActivity(intent)
                            finish()

                        } else if (response.getString("response_code").equals("1043", ignoreCase = true)) {
                            finish()

                        } else {

                            val intent =
                                Intent(applicationContext, PaymentSuccessScreen::class.java)
                            intent.putExtra("comefrom", comefrom)
                            intent.putExtra("transactionId", response.getString("transaction_id"))
                            intent.putExtra("responseCode", response.getString("response_code"))
                            intent.putExtra("amount", response.getString("amount"))
                            intent.putExtra("description", response.getString("description"))
                            intent.putExtra("order_id", response.getString("order_id"))
//                    intent.putExtra("pkgid", pkgid)
                            intent.putExtra("pkgname", pkgname)
                            intent.putExtra("pkgprice", pkgprice)
//                if (extras!!.containsKey("CardDetails")) {
//                    intent.putExtra("Trans_Type", "1")
//                } else {
//                    intent.putExtra("Trans_Type", "2")
//                }
                            startActivity(intent)
                            finish()
                        }

                        //                        transactionIdView.setText("Transaction ID: "+response.getString("transaction_id"));
//                        transactionStatusView.setText("Transaction Status: "+response.getString("response_message"));

//                        val intent = Intent(applicationContext, PaymentSuccessScreen::class.java)
//                        intent.putExtra("transactionId", response.getString("transaction_id"))
//                        intent.putExtra("responseCode", response.getString("response_code"))
//                        intent.putExtra("amount", response.getString("amount"))
//                        intent.putExtra("description", response.getString("description"))
//                        intent.putExtra("order_id", response.getString("order_id"))
//                        if (extras!!.containsKey("CardDetails")) {
//                            intent.putExtra("Trans_Type", "1")
//                        } else {
//                            intent.putExtra("Trans_Type", "2")
//                        }
//                        startActivity(intent)
//                        finish()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result

//                    onBackPressed()
                finish()
            }
        }
    }

}
