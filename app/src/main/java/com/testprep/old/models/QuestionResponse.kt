package com.testprep.old.models

class QuestionResponse {

    var status = ""
    var message = ""
    var data: ArrayList<QuestionList> = ArrayList()

    class QuestionList{
        var id = ""
        var title = ""
        var titlehtml = ""
        var titleimg = ""
        var iscorrect = ""
        var mcq: ArrayList<QuestionList> = ArrayList()
    }
}
