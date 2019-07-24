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

    }
}
