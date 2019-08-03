package com.testprep.models

import java.io.Serializable

class FilterModel {

    var Status = ""
    var Msg = ""

    var data: ArrayList<FilterData> = ArrayList()

    class FilterData(imageResId: Int, TestPackageName: String) : Serializable {

        var imageResId = 0
        var TestPackageName = ""

        init {
            this.imageResId = imageResId
            this.TestPackageName = TestPackageName

        }

        var CourseTypeID = 0
        var CourseTypeName = ""
        var Icon = ""

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
