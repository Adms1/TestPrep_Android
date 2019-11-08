package com.testprep.models

class TestListModel {

    var Status = ""
    var Msg = ""
    var data: ArrayList<TestData> = ArrayList()

    class TestData {

        var TestName = ""
        var TestID = 0
        var TestDuration = ""
        var TestMarks = ""
        var TestStartTime = ""
        var TestEndTime = ""
        var StatusName = ""
        var StudentTestID = 0
        var IsCompetetive = ""
        var TestPackageName = ""
        var TutorName = ""
        var SubjectName = ""
        var CourseName = ""
        var RemainTime = ""
        var TotalQuestions = ""
        var TestInstruction = ""
        var NumberOfHint = ""
        var NumberOfHintUsed = ""
    }
}
