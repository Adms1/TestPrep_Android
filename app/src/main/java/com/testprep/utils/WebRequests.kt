package com.testprep.utils

class WebRequests {

    companion object{

        fun addSignupParams(
            accounttype: String,
            id: String,
            fname: String,
            lname: String,
            email: String,
            pass: String,
            mobile: String,
            status: String
        ): HashMap<String, String> {
            val map = HashMap<String, String>()
            map["AccountTypeID"] = accounttype
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

        fun getPackageParams(
            coursetype: String,
            boardid: String,
            stdid: String,
            subjectid: String
        ): HashMap<String, String> {

            val map = HashMap<String, String>()

            map["CourseTypeID"] = coursetype
            map["BoardID"] = boardid
            map["StandardID"] = stdid
            map["SubjectID"] = subjectid

            return map

        }

    }

}
