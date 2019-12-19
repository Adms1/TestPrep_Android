package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and

class TraknpayRequestActivity : AppCompatActivity() {

    private val TAG = "TNPRequestDebugTag"
    //        var transid = ""
    var pkgname = ""
    var pkgprice = ""
//    private var extras: Bundle? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_traknpay_request)

//        transid = intent.getStringExtra("transid")
        pkgname = intent.getStringExtra("pkgname")
//        pkgprice = intent.getStringExtra("pkgprice")
        pkgprice = intent.getStringExtra("amount")

        val return_url = "https://biz.traknpay.in/tnp/return_page_android.php"
        var mode: String? = AppConstants.PAYMENT_MODE
        var order_id: String? = intent.getStringExtra("order_id")
        var amount: String? = intent.getStringExtra("amount")
        val currency = "INR"
        var description: String? = "coin purchase"
        val name = Utils.getStringValue(
            this@TraknpayRequestActivity,
            AppConstants.FIRST_NAME,
            ""
        ) + " " + Utils.getStringValue(this@TraknpayRequestActivity, AppConstants.LAST_NAME, "")
        val email = "tech@testprep.in"
        val phone = "7433988267"
        val address_line_1 = "-"
        val address_line_2 = "-"
        val city = "Ahmedabad"
        val state = "Gujarat"
        val zip_code = "380015"
        val country = "IND"
        val udf1 = Utils.getStringValue(this@TraknpayRequestActivity, AppConstants.USER_ID, "")
        val udf2 = "udf2"
        val udf3 = "udf3"
        val udf4 = "udf4"
        val udf5 = "udf5"
//        var card_number = ""
//        var card_name = ""
//        var card_exp_month = ""
//        var card_exp_year = ""
        val show_saved_cards = "n"

//        extras = intent.extras
//        if (extras != null) {
//            mode = extras!!.getString("mode")
//            amount = extras!!.getString("amount")
//            order_id = extras!!.getString("order_id")
//            description = extras!!.getString("description")
//
//            if (extras!!.containsKey("CardDetails")) {
//
//                val cardDetails = extras!!.getString("CardDetails")
//                val details = cardDetails!!.split("\\-".toRegex()).dropLastWhile { it.isEmpty() }
//                    .toTypedArray()//card.CardNumber + "-" + card.CardHolderName + " - " + card.ValidToDate
//                var expiryDate = details[2].trim { it <= ' ' }
//                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, expiryDate.length)
//
//                card_number = details[0].trim { it <= ' ' }
//                //card_name = details[1].toString().replace("/", "").trim();22-12-2016 old
//                card_name = details[1].replace("[;\\/:*?\"<>|&']".toRegex(), "").trim { it <= ' ' }//22-12-2016 navin
//
//                card_exp_month = expiryDate.split("\\/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//                card_exp_year = expiryDate.split("\\/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
//            }
//
//        }

        val mapHashData = HashMap<String, String>()
        mapHashData["address_line_1"] = address_line_1
        mapHashData["address_line_2"] = address_line_2
        mapHashData["amount"] = amount!!
        mapHashData["api_key"] = AppConstants.API_KEY
        mapHashData["city"] = city
        mapHashData["country"] = country
        mapHashData["currency"] = currency
        mapHashData["description"] = description!!
        mapHashData["email"] = email
        mapHashData["mode"] = mode!!
        mapHashData["name"] = name.trim()
        mapHashData["order_id"] = order_id!!
        mapHashData["phone"] = phone
        mapHashData["return_url"] = return_url
        mapHashData["state"] = state
        mapHashData["udf1"] = udf1!!
        mapHashData["udf2"] = udf2
        mapHashData["udf3"] = udf3
        mapHashData["udf4"] = udf4
        mapHashData["udf5"] = udf5
        mapHashData["zip_code"] = zip_code
        mapHashData["show_saved_cards"] = show_saved_cards

        val mapPostData = HashMap<String, String>()
        mapPostData["address_line_1"] = address_line_1
        mapPostData["address_line_2"] = address_line_2
        mapPostData["amount"] = amount
        mapPostData["api_key"] = AppConstants.API_KEY

//        if (extras!!.containsKey("CardDetails")) {
//            mapPostData["card_exp_month"] = card_exp_month
//            mapPostData["card_exp_year"] = "20$card_exp_year"
//            mapPostData["card_name"] = card_name
//            mapPostData["card_number"] = card_number
//        }

        mapPostData["city"] = city
        mapPostData["country"] = country
        mapPostData["currency"] = currency
        mapPostData["description"] = description
        mapPostData["email"] = email
        mapPostData["mode"] = mode
        mapPostData["name"] = name.trim()
        mapPostData["order_id"] = order_id
        mapPostData["phone"] = phone
        mapPostData["return_url"] = return_url
        mapPostData["state"] = state
        mapPostData["udf1"] = udf1
        mapPostData["udf2"] = udf2
        mapPostData["udf3"] = udf3
        mapPostData["udf4"] = udf4
        mapPostData["udf5"] = udf5
        mapPostData["zip_code"] = zip_code
        mapPostData["show_saved_cards"] = show_saved_cards

        var hashData = AppConstants.SECRET_KEY
        var postData = ""

        for (key in TreeSet<String>(mapHashData.keys)) {
            if (mapHashData[key] != "") {
                hashData = hashData + "|" + mapHashData[key]

            }
        }

//        hashData = "531553f8d6b906aa3342948a3c535ca301de9d5d|-|-|20|535ee616-a161-4e16-88ed-a338582e841a|Ahmedabad|IND|INR|test|bhargavchauhan1992@gmail.com|TEST|Bhargav|12|7575809733|https://biz.traknpay.in/tnp/return_page_android.php|n|Gujarat|380015"


        // Sort the map by key and create the hashData and postData
        for (key in TreeSet<String>(mapPostData.keys)) {
            if (mapPostData[key] != "") {

                postData = postData + key + "=" + mapPostData[key] + "&"
            }
        }

        val hash = hashData.sha512().toUpperCase()


        // Generate the hash key using hashdata and append the has to postData query string
//        val hash = generateSha512Hash(hashData).toUpperCase()
        postData = postData + "hash=" + hash

//        postData =
//            "address_line_1=-&address_line_2=-&amount=20&api_key=535ee616-a161-4e16-88ed-a338582e841a&city=Ahmedabad&country=IND&currency=INR&description=test&email=bhargavchauhan1992@gmail.com&mode=TEST&name=Bhargav&order_id=12&phone=7575809733&return_url=https://biz.traknpay.in/tnp/return_page_android.php&show_saved_cards=n&state=Gujarat&zip_code=380015&hash=$hash"

        Log.d(TAG, "HashData: $hashData")
        Log.d(TAG, "Hash: $hash")
        Log.d(TAG, "PostData: $postData")

        val webView = findViewById<WebView>(R.id.webView)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        webSettings.domStorageEnabled = true
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDatabasePath("/data/data/" + getPackageName() + "/databases/");a
//        webSettings.setAppCacheMaxSize(1024*1024*8);
//        webSettings.setAppCachePath("/data/data/" + getPackageName() + "/cache/");
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setLightTouchEnabled(true);

        webSettings.builtInZoomControls = true
//        webView.setWebChromeClient(new WebChromeClient());
        webView.webViewClient = WebViewClient()
        webView.postUrl(AppConstants.PAYMENT_REQUEST, postData.toByteArray())

        Log.d("url", "" + webView.postUrl(AppConstants.PAYMENT_REQUEST, postData.toByteArray()))

        webView.addJavascriptInterface(MyJavaScriptInterface(this), "Android")

    }

    /**
     * Generates the SHA-512 hash (same as PHP) for the given string
     *
     * @param toHash string to be hashed
     * @return return hashed string
     */
    fun generateSha512Hash(toHash: String): String {
        var md: MessageDigest? = null
        var hash: ByteArray? = null
        var sb = StringBuilder()
        try {
//            md = MessageDigest.getInstance("SHA-512")
//            hash = md!!.digest(toHash.toByteArray(charset("UTF-8")))

            val md = MessageDigest.getInstance("SHA-512")
            val data = md.digest(toHash.toByteArray())

            for (i in data.indices) {
                sb.append(Integer.toString((data[i] and 0xff.toByte()) + 0x100, 16).substring(1))
            }
            println(sb)


        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return sb.toString()
    }

    /**
     * Converts the given byte[] to a hex string.
     *
     * @param raw the byte[] to convert
     * @return the string the given byte[] represents
     */
    private fun convertToHex(raw: ByteArray): String {
        val sb = StringBuilder()
        for (aRaw in raw) {
            sb.append(Integer.toString((aRaw and 0xff.toByte()) + 0x100, 16).substring(1))
        }
        return sb.toString()
    }

    /**
     * Interface to bind Javascript from WebView with Android
     */
    inner class MyJavaScriptInterface
    /**
     * Instantiate the interface and set the context
     */
    internal constructor(internal var mContext: Context) {

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        fun getPaymentResponse(jsonResponse: String) {
            try {
                val resposeData = JSONObject(jsonResponse)
                Log.d(TAG, "ResponseJson: $resposeData")

                if (resposeData.getString("response_code").equals("0", ignoreCase = true)) {
//                    callAddTestPackageApi()

                    val intent = Intent(applicationContext, PaymentSuccessScreen::class.java)
                    intent.putExtra("transactionId", resposeData.getString("transaction_id"))
                    intent.putExtra("responseCode", resposeData.getString("response_code"))
                    intent.putExtra("amount", resposeData.getString("amount"))
                    intent.putExtra("description", resposeData.getString("description"))
                    intent.putExtra("order_id", resposeData.getString("order_id"))
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

                } else {

                    val intent = Intent(applicationContext, PaymentSuccessScreen::class.java)
                    intent.putExtra("transactionId", resposeData.getString("transaction_id"))
                    intent.putExtra("responseCode", resposeData.getString("response_code"))
                    intent.putExtra("amount", resposeData.getString("amount"))
                    intent.putExtra("description", resposeData.getString("description"))
                    intent.putExtra("order_id", resposeData.getString("order_id"))
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

//                if (responseCode.equals("0")) {
//                    Intent intent = new Intent(getApplicationContext(), PaymentSuccessScreen.class);
//                    intent.putExtra("transactionId", transactionId);
//                    intent.putExtra("responseCode", responseCode);
//                    intent.putExtra("responseMessage", responseMessage);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), TraknpayResponseActivity.class);
//                    intent.putExtra("transactionId", transactionId);
//                    intent.putExtra("responseCode", responseCode);
//                    intent.putExtra("responseMessage", responseMessage);
//                    startActivity(intent);
//                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    fun String.sha512(): String {
        return this.hashWithAlgorithm("SHA-512")
    }

    private fun String.hashWithAlgorithm(algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
        return bytes.fold("", { str, it -> str + "%02x".format(it) })
    }

    fun callAddTestPackageApi() {

        if (!DialogUtils.isNetworkConnected(this@TraknpayRequestActivity)) {
            Utils.ping(this@TraknpayRequestActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@TraknpayRequestActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.addTestPackage(
            Utils.getStringValue(this@TraknpayRequestActivity, AppConstants.USER_ID, "0")!!,
            "1"
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        AppConstants.isFirst = 1

                        val intent =
                            Intent(this@TraknpayRequestActivity, DashboardActivity::class.java)
                        startActivity(intent)
//
                        finish()
//                        Toast.makeText(
//                            this@TraknpayRequestActivity,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        onBackPressed()
                    } else {

                        onBackPressed()

//                        Toast.makeText(
//                            this@TraknpayRequestActivity,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
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

    override fun onBackPressed() {

    }

}
