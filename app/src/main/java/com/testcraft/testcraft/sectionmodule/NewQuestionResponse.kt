package com.testcraft.testcraft.sectionmodule

class NewQuestionResponse {

    var Status = ""
    var Msg = ""
    var data: ArrayList<QuestionList> = ArrayList()

    class QuestionList {
        var SectionID = 0
        var SectionName = ""
        var SectionInstruction = ""

        var TestQuestion: ArrayList<TestQuestions> = ArrayList()
    }

    class TestQuestions {

        var QuestionID = 0
        var TestQuestionID = 0
        var QuestionTypeID = 0
        var Marks = ""
        var QuestionImage = ""
        var ObtainMarks = ""
        var QuestionTime = ""
        var QuestionAns = ""
        var Answer = ""
        var Answered = ""
        var Review = ""
        var CorrectAnswer = ""
        var IsCorrect = "False"
        var Hint = ""
        var HintUsed = ""
        var Explanation = ""
        var SystemAnswer = ""
        var YourAnswer = ""
        var StudentTestAnswerMCQ: ArrayList<QuestionDataList> = ArrayList()
        var StudentTestQuestionMCQ: ArrayList<QuestionDataList> = ArrayList()

    }

    class QuestionDataList {
        var MultipleChoiceQuestionAnswerID = ""
        var AnswerImage = ""
        var IsCorrectAnswer = false
        var IsUserSelected = false
        var optiontext = ""
        var isIndex = false

    }

}
