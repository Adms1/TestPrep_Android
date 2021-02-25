package com.testcraft.testcraft.utils

object SampleAppConstants {
    //API_KEY is given by the Payment Gateway. Please Copy Paste Here.
    val PG_API_KEY: String = AppConstants.API_KEY

    //URL to Accept Payment Response After Payment. This needs to be done at the client's web server.
    const val PG_RETURN_URL = "https://biz.traknpay.in/tnp/return_page_android.php"

    //Enter the Mode of Payment Here . Allowed Values are "LIVE" or "TEST".
    const val PG_MODE = "TEST"

    //PG_CURRENCY is given by the Payment Gateway. Only "INR" Allowed.
    const val PG_CURRENCY = "INR"

    //PG_COUNTRY is given by the Payment Gateway. Only "IND" Allowed.
    const val PG_COUNTRY = "IND"
    const val PG_AMOUNT = ""
    const val PG_EMAIL = "tech@testprep.in"
    val PG_NAME: String = ""
    const val PG_PHONE = "7433988267"
    const val PG_DESCRIPTION = ""
    const val PG_CITY = "Ahmedabad"
    const val PG_STATE = "Gujarat"
    const val PG_ADD_1 = "-"
    const val PG_ADD_2 = "-"
    const val PG_ZIPCODE = "380015"
    const val PG_UDF1 = ""
    const val PG_UDF2 = ""
    const val PG_UDF3 = ""
    const val PG_UDF4 = ""
    const val PG_UDF5 = ""
    var PG_ORDER_ID = ""
}