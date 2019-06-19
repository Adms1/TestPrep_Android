package com.testprep.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.activity.TraknpayRequestActivity
import com.testprep.adapter.CoinAdapter
import com.testprep.interfaces.CoinInteface
import com.testprep.models.CoinModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.fragment_coin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CoinFragment : AppCompatActivity(), CoinInteface {

    internal var isBoolean_permission_location = false
    internal var isBoolean_permission_phoneState = false
    val REQUEST_PERMISSIONS_Location = 1
    val REQUEST_PERMISSIONS_PhoneState = 2
    var coinInteface: CoinInteface? = null

    var coinArr: ArrayList<CoinModel> = ArrayList()
    var purchaseCoin = ""

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_coin, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        coinInteface = this

        setContentView(R.layout.fragment_coin)

//        val heading = this@CoinFragment.findViewById(R.id.dashboard_tvTitle) as TextView
//        heading.text = "Coin"

        coinList()

        coin_rvList.layoutManager = GridLayoutManager(this@CoinFragment, 3)
        coin_rvList.adapter = CoinAdapter(this@CoinFragment, coinArr, coinInteface!!)

        coin_btnCharge.setOnClickListener {
            if (DialogUtils.isNetworkConnected(this@CoinFragment)) {

//            if (isVersionCodeUpdated) {
                chargeBtnLogic()

//            } else {
//                Utils.openVersionDialogCharge(this@CoinFragment)
//
//            }

            } else {
                Utils.ping(this@CoinFragment, "Network not available")
            }
        }

        coin_ivBack.setOnClickListener { onBackPressed() }

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
//            Utils.ping(this@CoinFragment, "Oops!! Payment was cancelled")
//        }
//    }

    fun coinList() {

        val coinModel = CoinModel()
        coinModel.coin = "50"
        coinModel.rupees = "100"
        coinArr.add(coinModel)

        val coinModel1 = CoinModel()
        coinModel1.coin = "100"
        coinModel1.rupees = "200"
        coinArr.add(coinModel1)

        val coinModel2 = CoinModel()
        coinModel2.coin = "200"
        coinModel2.rupees = "400"
        coinArr.add(coinModel2)

        val coinModel3 = CoinModel()
        coinModel3.coin = "300"
        coinModel3.rupees = "600"
        coinArr.add(coinModel3)

        val coinModel4 = CoinModel()
        coinModel4.coin = "400"
        coinModel4.rupees = "800"
        coinArr.add(coinModel4)

        val coinModel5 = CoinModel()
        coinModel5.coin = "500"
        coinModel5.rupees = "1000"
        coinArr.add(coinModel5)

    }

    fun chargeBtnLogic() {
//        com.testprep.utils.AppConstants.planid = "0"
        if (AppConstants.API_KEY.length > 5 && AppConstants.SECRET_KEY.length > 5) {
            var amount: Array<String>? = null
            if (purchaseCoin.contains("."))
                amount = purchaseCoin.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()//to check decimal places

            if (purchaseCoin.equals("", ignoreCase = true)) {
                Utils.ping(this@CoinFragment, this@CoinFragment.resources.getString(R.string.enter_coin))
            } else if (amount != null && amount[1].length > 2) {
                Utils.ping(this@CoinFragment, "Please provide upto 2 decimal places only.")
            } else if (java.lang.Double.parseDouble(purchaseCoin) < 2.00) {
                Utils.ping(this@CoinFragment, "Amount can't be less than Rs. 2.00")
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

                    generateTrackNPayRequest(this@CoinFragment, purchaseCoin)

//                    }
                } catch (e: Exception) {
                    //SendMessage(MESSAGE_ERROR, e.getMessage());
                    Utils.ping(this@CoinFragment, e.message.toString())
                }

            }/*else if (Double.parseDouble(edtAmount.getText().toString()) < 2.00) {
                        com.testprep.utils.Utils.ping(this@CoinFragment, "Amount can't be more than Rs. 100000.00");
                    }*/
//            Instamojo.setLogLevel(Log.DEBUG)
        } else {
            Utils.openInvalidApiKeyDialog(this@CoinFragment)
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
                        context.startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(context, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

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

    override fun getCoin(str: String) {

        purchaseCoin = str
    }

}
