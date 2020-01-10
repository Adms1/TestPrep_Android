package com.testcraft.testcraft.models

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

        var StudentID = ""
        var StudentName = ""
        var Remarks = ""
        var Rating = ""
        var DateTime = ""

        var TutorDescription = ""
        var TotalRateCount = ""
        var TutorStars = ""
    }
}
