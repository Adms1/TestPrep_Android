package com.testprep.utils

import java.util.*

class WebRequests {

    companion object{

        fun addSignupParams(
            logintype: String,
            id: String,
            fname: String,
            lname: String,
            email: String,
            pass: String,
            mobile: String,
            status: String
        ): HashMap<String, String> {
            val map = HashMap<String, String>()
            map["LoginTypeID"] = logintype
            map["StudentID"] = id
            map["StudentFirstName"] = fname
            map["StudentLastName"] = lname
            map["StudentEmailAddress"] = email
            map["StudentPassword"] = pass
            map["StudentMobile"] = mobile
            map["StatusID"] = status

            return map
        }

        fun addLoginParams(email: String, pass: String): HashMap<String, String> {
            val map = HashMap<String, String>()

            map["Email"] = email
            map["Password"] = pass

            return map
        }

        fun checkEmailParams(email: String): HashMap<String, String> {
            val map = HashMap<String, String>()

            map["Email"] = email

            return map
        }

    }

}
