package com.testprep.models

class TutorModel {

    var Status = ""
    var Msg = ""

    var data: ArrayList<TutorData> = ArrayList()

    class TutorData {
        var TutorID = ""
        var TutorName = ""
        var TutorEmail = ""
        var TutorPhoneNumber = ""
        var InstituteName = ""
        var Icon = ""

        var StudentName = ""
        var Remarks = ""
        var Rating = ""
        var DateTime = ""

        var TotalRateCount = ""
        var TutorStars = ""
    }
}
