package com.testcraft.testcraft.models

class AttemptModel {

    var Status = ""
    var Msg = ""
    var data: ArrayList<Attemptdata> = ArrayList()

    class Attemptdata {

        var SectionName = ""
        var SubjectName = ""
        var Answered = ""
        var UnAnswered = ""
        var TotalQue = ""
        var TotalMark = ""
        var ObtainMark = ""
    }

}
