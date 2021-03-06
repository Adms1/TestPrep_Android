package com.testcraft.testcraft.utils

import java.util.*

class AppConstants {

    // Signup
    // normal signup 1
    // Google 2
    // Fb 3

    companion object {

//        var PAYMENT_REQUEST = "https://biz.traknpay.in/v2/paymentrequest"

//        LIVE

        var BASE_URL = "https://webservice.testcraft.in/WebService.asmx/"
        var IMAGE_BASE_URL = "http://testcraft.in/upload/"
        var EXPHINT_IMAGE_BASE_URL = "http://testcraft.in/"
        var PAYMENT_REQUEST = "http://testcraft.in/MobilePayment.aspx?"
        var INVOICE_URL = "http://testcraft.in/InvoiceDetail.aspx?ID="
        var SUMMARY_REPORT_URL = "https://webservice.testcraft.in/TestSummaryReport.aspx?"
        var SUBJECT_SUMMARY_REPORT_URL =
            "https://webservice.testcraft.in/SubjectSummaryReport.aspx?"
        var ALL_SUMMARY_REPORT_URL = "https://webservice.testcraft.in/AllTestSummaryReport.aspx?"

//          LOCAL

//        var BASE_URL = "http://demowebservice.testcraft.in/webservice.asmx/"
//        var IMAGE_BASE_URL = "http://demo.testcraft.in/upload/"
//        var EXPHINT_IMAGE_BASE_URL = "http://demo.testcraft.in/"
//        var PAYMENT_REQUEST = "http://demo.testcraft.in/MobilePayment.aspx?"
//        var INVOICE_URL = "http://demo.testcraft.in/InvoiceDetail.aspx?ID="
//        var SUMMARY_REPORT_URL = "http://demowebservice.testcraft.in/TestSummaryReport.aspx?"
//        var SUBJECT_SUMMARY_REPORT_URL =
//            "http://demowebservice.testcraft.in/SubjectSummaryReport.aspx?"
//        var ALL_SUMMARY_REPORT_URL = "http://demowebservice.testcraft.in/AllTestSummaryReport.aspx?"

//        USER DATA

        var FIRST_NAME = "firstname"
        var LAST_NAME = "lastname"
        var USER_ID = "userid"
        var USER_EMAIL = "useremail"
        var USER_PASSWORD = "userpassword"
        var USER_MOBILE = "usermobile"
        var USER_ACCOUNT_TYPE = "userlogintype"
        var USER_STATUSID = "userstatusid"
        var OTP = "otp"
        var user_profile = "userprofile"

        var GUEST_FIRSTNAME = "Guest"
        var GUEST_LASTNAME = "User"

        var APP_MODE = "NOMODE"
        var DEEPLINK_MODE = "DEEPLINK"
        var GUEST_MODE = "GUEST"
        var NORMAL_MODE = "NORMAL"

        var isPrefrence = "0"
        var IS_LOGIN = "false"

        var FCM_TOKEN = "FCMTOKEN"

        /* 1 - select dplink lang
         * 2 - dplink test list
         * 3 - dplink result screen
         * 4 - no step */
        var IS_DEEPLINK_STEP = "1"
        var DEEPLINK_CODE = "000"

        var COURSE_FLOW = ""
        var COURSE_FLOW_ARRAY: ArrayList<String> = ArrayList()

//        PAYMENT LOCAL

//        var API_KEY = "535ee616-a161-4e16-88ed-a338582e841a"
//        var SECRET_KEY = "531553f8d6b906aa3342948a3c535ca301de9d5d"
//        var PAYMENT_MODE = "TEST"

//        PAYMENT LIVE

        var API_KEY = "0cda5f4f-d803-4316-8562-c75a72fe99c0"
        var SECRET_KEY = "10ad507bb768b574c3d6ff2bc6694b04386b9c8f"
        var PAYMENT_MODE = "LIVE"

        var QUE_NUMBER = 0
        var QUE_NUMBER1 = 0

        var COURSE_TYPE_ID = "course_type_id"
        var COURSE_ID = "course_id"
        var BOARD_ID = "board_id"
        var STANDARD_ID = "standard_id"
        var SUBJECT_ID = "subject_id"
        var TUTOR_ID = "tutor_id"
        var FROM_RICE = "from_price"
        var TO_PRICE = "to_price"

        var FILTER_COURSE_TYPE_ID = "0"
        var FILTER_COURSE_ID = "0"
        var FILTER_BOARD_ID = "0"
        var FILTER_STANDARD_ID = "0"
        var FILTER_SUBJECT_ID = "0"
        var FILTER_TUTOR_ID = "0"
        var FILTER_FROM_PRICE = "0"
        var FILTER_TO_PRICE = "5000"

        var MIN_PRICE = "0"
        var MAX_PRICE = "0000"

        var isFirst = 0
        var isInstall = "true"

        var PKG_ID = "0"
        var PKG_NAME = "xyz"
        var IS_SELF_TEST = "false"

        var NETWORK_MSG = "The network is not reachable."

        //        ACTION ID(GAME ID)
        var DEFAULT_ACTION_ID = ""
        var DEFAULT_TOKEN_ID = "3371B09E-B7E2-4327-B072-A01559365660"

//        var API_KEY = "487f26cf-15cd-4497-a053-a4c5ac0e9a7f"
//        var SECRET_KEY = "900249336e0680941326c99ff756b23e3e675efd"
    }
}
