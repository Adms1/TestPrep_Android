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
    }
}
