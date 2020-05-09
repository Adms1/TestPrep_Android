package com.testcraft.testcraft.models

import android.preference.Preference
import com.testcraft.testcraft.utils.AppConstants.Companion.OTP

class VerifyMobileData {

    var Status = ""
    var Msg = ""
    var data: ArrayList<VerifyData> = ArrayList()

    class VerifyData{
        var StudentID =  0
        var StudentFirstName =  ""
        var StudentLastName = ""
        var StudentEmailAddress = ""
        var StudentPassword = ""
        var StudentMobile = ""
        var StatusID = 0
        var AccountTypeID = 0
        var OTP = ""
        var Photo = ""
        var Preference: ArrayList<VerifyPref> = ArrayList()
    }

    class VerifyPref{

        var CourseTypeID = ""
        var BoardID = ""
        var StandardID = ""
        var SubjectID = ""

    }

}