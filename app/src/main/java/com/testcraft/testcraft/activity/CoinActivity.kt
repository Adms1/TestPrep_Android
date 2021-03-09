package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.CoinAdapter
import com.testcraft.testcraft.interfaces.CoinInteface
import com.testcraft.testcraft.models.CoinModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import com.testcraft.testcraft.utils.WebRequests
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.fragment_coin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CoinActivity : AppCompatActivity(), CoinInteface {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    //    internal var isBoolean_permission_location = false
//    internal var isBoolean_permission_phoneState = false
//    val REQUEST_PERMISSIONS_Location = 1
//    val REQUEST_PERMISSIONS_PhoneState = 2
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        coinInteface = this

        setContentView(R.layout.fragment_coin)

//        val heading = this@CoinActivity.findViewById(R.id.dashboard_tvTitle) as TextView
//        heading.text = "Coin"

        connectivity = Connectivity()

        coinList()

        coin_rvList.layoutManager = GridLayoutManager(this@CoinActivity, 3)
        coin_rvList.adapter = CoinAdapter(this@CoinActivity, coinArr, coinInteface!!)

        coin_btnCharge.setOnClickListener {
            if (DialogUtils.isNetworkConnected(this@CoinActivity)) {

//            if (isVersionCodeUpdated) {
                chargeBtnLogic()

//            } else {
//                Utils.openVersionDialogCharge(this@CoinActivity)
//
//            }

            } else {
                Utils.ping(this@CoinActivity, AppConstants.NETWORK_MSG)
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
//            Utils.ping(this@CoinActivity, "Oops!! Payment was cancelled")
//        }
//    }

    fun coinList() {

        val coinModel = CoinModel()
        coinModel.coin = "100"
        coinModel.rupees = "100"
        coinArr.add(coinModel)

        val coinModel1 = CoinModel()
        coinModel1.coin = "200"
        coinModel1.rupees = "200"
        coinArr.add(coinModel1)

        val coinModel2 = CoinModel()
        coinModel2.coin = "400"
        coinModel2.rupees = "400"
        coinArr.add(coinModel2)

        val coinModel3 = CoinModel()
        coinModel3.coin = "600"
        coinModel3.rupees = "600"
        coinArr.add(coinModel3)

        val coinModel4 = CoinModel()
        coinModel4.coin = "800"
        coinModel4.rupees = "800"
        coinArr.add(coinModel4)

        val coinModel5 = CoinModel()
        coinModel5.coin = "1000"
        coinModel5.rupees = "1000"
        coinArr.add(coinModel5)

    }

    fun chargeBtnLogic() {
//        com.testprep.utils.AppConstants.planid = "0"
        if (AppConstants.API_KEY.length > 5 && AppConstants.SECRET_KEY.length > 5) {
            var amount: Array<String>? = null
            if (purchaseCoin.contains("."))
                amount = purchaseCoin.split("\\.".toRegex()).toTypedArray()//to check decimal places

            if (purchaseCoin.equals("", ignoreCase = true)) {
                Utils.ping(
                    this@CoinActivity,
                    this@CoinActivity.resources.getString(R.string.enter_coin)
                )
            } else if (amount != null && amount[1].length > 2) {
                Utils.ping(this@CoinActivity, "Please provide upto 2 decimal places only.")
            } else if (java.lang.Double.parseDouble(purchaseCoin) < 2.00) {
                Utils.ping(this@CoinActivity, "Amount can't be less than Rs. 2.00")
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

                    generateTrackNPayRequest(this@CoinActivity, purchaseCoin)

//                    }
                } catch (e: Exception) {
                    //SendMessage(MESSAGE_ERROR, e.getMessage());
                    Utils.ping(this@CoinActivity, e.message.toString())
                }

            }/*else if (Double.parseDouble(edtAmount.getText().toString()) < 2.00) {
                        com.testprep.utils.Utils.ping(this@CoinActivity, "Amount can't be more than Rs. 100000.00");
                    }*/
//            Instamojo.setLogLevel(Log.DEBUG)
        } else {
            Utils.openInvalidApiKeyDialog(this@CoinActivity)
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
                        context.startActivity(intent)
//                        finish()

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

    override fun getCoin(str: String) {

        purchaseCoin = str
    }

}
