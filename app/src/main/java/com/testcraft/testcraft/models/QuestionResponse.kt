package com.testcraft.testcraft.models

class QuestionResponse {

    var Status = ""
    var Msg = ""
    var data: ArrayList<QuestionList> = ArrayList()

    class QuestionList {
        var QuestionID = 0
        var TestQuestionID = 0
        var QuestionTypeID = 0
        var Marks = ""
        var QuestionImage = ""
        var QuestionAns = ""
        var Answer = ""
        var Answered = ""
        var Review = ""
        var CorrectAnswer = ""
        var IsCorrect = "False"
        var Hint = ""
        var Explanation = ""
        var StudentTestAnswerMCQ: ArrayList<QuestionDataList> = ArrayList()
        var StudentTestQuestionMCQ: ArrayList<QuestionDataList> = ArrayList()

    }

    class QuestionDataList {
        var MultipleChoiceQuestionAnswerID = ""
        var AnswerImage = ""
        var IsCorrectAnswer = false
        var IsUserSelected = false

    }

}
