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

        fun checkForgotpassParams(mobile: String): HashMap<String, String> {
            val map = HashMap<String, String>()

            map["Mobile"] = mobile

            return map
        }

        fun changePasswordParams(stu_id: String, new_pass: String): HashMap<String, String> {
            val map = HashMap<String, String>()

            map["StudentID"] = stu_id
            map["NewPassword"] = new_pass

            return map
        }

        fun getPackageParams(
            coursetype: String,
            boardid: String,
            stdid: String,
            subjectid: String,
            stu_id: String
        ): HashMap<String, String> {

            val map = HashMap<String, String>()

            map["StudentID"] = stu_id
            map["CourseTypeID"] = coursetype
            map["BoardID"] = boardid
            map["StandardID"] = stdid
            map["SubjectID"] = subjectid

            return map

        }

        fun getFilterParams(
            coursetype: String,
            courseid: String,
            boardid: String,
            stdid: String,
            subjectid: String,
            tutorid: String,
            fromprice: String,
            toprice: String,
            name: String,
            typeid: String
        ): HashMap<String, String> {

            val map = HashMap<String, String>()

            map["CourseTypeID"] = coursetype
            map["CourseID"] = courseid
            map["BoardID"] = boardid
            map["StandardID"] = stdid
            map["SubjectID"] = subjectid
            map["TutorID"] = tutorid
            map["FromPrice"] = fromprice
            map["ToPrice"] = toprice
            map["Name"] = name
            map["typeid"] = typeid
            return map

        }


        fun getPaymentParams(tansid: String, stuid: String, amount: String): HashMap<String, String> {

            val hashMap = java.util.HashMap<String, String>()
            hashMap["PaymentTransactionID"] = tansid
            hashMap["StudentID"] = stuid
            hashMap["PaymentAmount"] = amount

            return hashMap
        }

        fun getPaymentStatusParams(
            stid: String,
            orderid: String,
            transid: String,
            transtatus: String
        ): HashMap<String, String> {
            val hashMap = java.util.HashMap<String, String>()
            hashMap["StudentID"] = stid
            hashMap["PaymentOrderID"] = orderid
            hashMap["ExternalTransactionID"] = transid
            hashMap["ExternalTransactionStatus"] = transtatus

            return hashMap
        }

        fun submitAnswerParams(
            studenttestid: String,
            testquestionid: Int,
            questionid: Int,
            questiontypeid: Int,
            answer: String,
            usetime: String,
            review: String
        ): HashMap<String, String> {
            val hashMap = java.util.HashMap<String, String>()
            hashMap["StudentTestID"] = studenttestid
            hashMap["TestQuestionID"] = testquestionid.toString()
            hashMap["QuestionID"] = questionid.toString()
            hashMap["QuestionTypeID"] = questiontypeid.toString()
            hashMap["Answer"] = answer
            hashMap["UseTime"] = usetime
            hashMap["Review"] = review

            return hashMap
        }

    }

}
