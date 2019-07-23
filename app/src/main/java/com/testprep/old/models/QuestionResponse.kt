package com.testprep.old.models

class QuestionResponse {

    var Status = ""
    var Msg = ""
    var data: ArrayList<QuestionList> = ArrayList()

    class QuestionList{
        var QuestionID = ""
        var QuestionTypeID = ""
        var Marks = ""
        var QuestionImage = ""
        var iscorrect = ""
        var StudentTestQuestionMCQ: ArrayList<QuestionDataList> = ArrayList()

        var isAnswered = 0
        var isView = 0
    }

    class QuestionDataList {
        var MultipleChoiceQuestionAnswerID = ""
        var AnswerImage = ""
        var IsCorrectAnswer = ""
    }

}
