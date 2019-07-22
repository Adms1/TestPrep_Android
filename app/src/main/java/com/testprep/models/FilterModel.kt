package com.testprep.models

class FilterModel {

    var Status = ""
    var Msg = ""

    var data: ArrayList<FilterData> = ArrayList()

    class FilterData {

        var TutorID = ""
        var TutorName = ""
        var TutorEmail = ""
        var TutorPhoneNumber = ""
        var InstituteName = ""

        var isSelected = false

        var SubjectID = ""
        var SubjectName = ""
        var StandardID = ""
        var StandardName = ""

        var CourseID = ""
        var CourseName = ""
    }
}
