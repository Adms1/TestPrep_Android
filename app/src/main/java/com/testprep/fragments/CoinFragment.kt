package com.testprep.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.activity.TraknpayRequestActivity
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_coin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CoinFragment : Fragment() {

    internal var isBoolean_permission_location = false
    internal var isBoolean_permission_phoneState = false
    val REQUEST_PERMISSIONS_Location = 1
    val REQUEST_PERMISSIONS_PhoneState = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coin_btnCharge.setOnClickListener {
            if (DialogUtils.isNetworkConnected(activity!!)) {

//            if (isVersionCodeUpdated) {
                chargeBtnLogic()

//            } else {
//                Utils.openVersionDialogCharge(activity!!)
//
//            }

            } else {
                Utils.ping(activity!!, "Network not available")
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == AppConstants.REQUEST_CODE) {// && data != null) {
//            /*String orderID = data.getStringExtra(Constants.ORDER_ID);
//            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
//            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);*/
//
//            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
//            //            if (orderID != null && transactionID != null && paymentID != null) {
//            checkPaymentStatus()//transactionID);
//            //            } else {
//            //                Utility.ping(mContext, "Oops!! Payment was cancelled");
//            //            }
//        } else {
//            Utils.ping(activity!!, "Oops!! Payment was cancelled")
//        }
//    }

    fun chargeBtnLogic() {
//        com.testprep.utils.AppConstants.planid = "0"
        if (AppConstants.API_KEY.length > 5 && AppConstants.SECRET_KEY.length > 5) {
            var amount: Array<String>? = null
            if (coin_etCoin.text.toString().contains("."))
                amount = coin_etCoin.text.toString().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()//to check decimal places

            if (coin_etCoin.text.toString().equals("", ignoreCase = true)) {
                Utils.ping(activity!!, activity!!.resources.getString(R.string.enter_coin))
            } else if (amount != null && amount[1].length > 2) {
                Utils.ping(activity!!, "Please provide upto 2 decimal places only.")
            } else if (java.lang.Double.parseDouble(coin_etCoin.text.toString()) < 2.00) {
                Utils.ping(activity!!, "Amount can't be less than Rs. 2.00")
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

                    val intent = Intent(activity!!, TraknpayRequestActivity::class.java)
//                                            if (isCardPresent) {
//                                                intent.putExtra("CardDetails", cardDetail)
//                                            }
//                                            intent.putExtra("order_id", transactionID)
//                                            intent.putExtra("amount", edtAmount.getText().toString())
//                                            intent.putExtra("mode", "LIVE")
//                                            intent.putExtra("description", edtNarration.getText().toString())
                    startActivity(intent)

//                    }
                } catch (e: Exception) {
                    //SendMessage(MESSAGE_ERROR, e.getMessage());
                    Utils.ping(activity!!, e.message.toString())
                }

            }/*else if (Double.parseDouble(edtAmount.getText().toString()) < 2.00) {
                        com.testprep.utils.Utils.ping(activity!!, "Amount can't be more than Rs. 100000.00");
                    }*/
//            Instamojo.setLogLevel(Log.DEBUG)
        } else {
            Utils.openInvalidApiKeyDialog(activity!!)
        }
    }

    fun generateTrackNPayRequest() {
        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashMap = HashMap<String, String>()
        hashMap["CustomerID"] = AppConstants.USER_ID
        hashMap["Name"] = AppConstants.FIRST_NAME + " " + AppConstants.LAST_NAME
        hashMap["Email"] = AppConstants.USER_EMAIL
        hashMap["Mobile"] = AppConstants.USER_MOBILE
        hashMap["Amount"] = coin_etCoin.text.toString().trim { it <= ' ' }
        hashMap["Description"] = "purchase coin"

        if (isBoolean_permission_phoneState) {
            hashMap["IMEINumber"] = Utils.getIMEI(activity!!)
        } else {
            hashMap["IMEINumber"] = ""
        }

        if (isBoolean_permission_location) {
            val loc = Utils.getLocation(activity!!)
            hashMap["Latitude"] = loc[0].toString()
            hashMap["Longitude"] = loc[1].toString()
        } else {
            hashMap["Latitude"] = ""
            hashMap["Longitude"] = ""
        }
//        if (isCardPresent) {
//            hashMap["Trans_Type"] = "1" /* card*/
//        } else {
//            hashMap["Trans_Type"] = "2" /* manually */
//        }

        val call = apiService.getPayment(hashMap)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

//                                        if (hashMapResult.size > 0) {
//                                            transactionID = hashMapResult.get("TransactionID")
//                                        }
//                                        if (transactionID != null) {
//                                            Log.v("CardPresent: ", isCardPresent.toString())
                        val intent = Intent(activity!!, TraknpayRequestActivity::class.java)
//                                            if (isCardPresent) {
//                                                intent.putExtra("CardDetails", cardDetail)
//                                            }
//                                            intent.putExtra("order_id", transactionID)
//                                            intent.putExtra("amount", edtAmount.getText().toString())
//                                            intent.putExtra("mode", "LIVE")
//                                            intent.putExtra("description", edtNarration.getText().toString())
                        startActivity(intent)
//                                        } else {
//                                            Utils.ping(mContext, "Transaction ID not found. Please try again")
//                                        }
//
//                                    fun OnResponseFail(output: Any) {
//                                        dialog.dismiss()
//                                        Utils.ping(mContext, "Transaction ID not found. Please try again")
//                                    }

                    } else {
                        Toast.makeText(activity!!, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

//                    Log.d("loginresponse", response.body()!!.asString)
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

}
