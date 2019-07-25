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
        var StudentTestAnswerMCQ: ArrayList<QuestionDataList> = ArrayList()
        var StudentTestQuestionMCQ: ArrayList<QuestionDataList> = ArrayList()

    }

    class QuestionDataList {
        var MultipleChoiceQuestionAnswerID = ""
        var AnswerImage = ""
        var IsCorrectAnswer = false
        var StudentAnswer = false

    }

}
