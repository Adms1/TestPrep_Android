package com.testprep.utils

import java.util.HashMap

class WebRequests {

    companion object{

        fun addSignupParams(id: String, fname: String, lname: String, email: String, pass: String, mobile: String, status: String): HashMap<String, String> {
            val map = HashMap<String, String>()
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

    }

}
