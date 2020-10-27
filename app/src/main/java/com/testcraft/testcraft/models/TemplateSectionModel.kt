package com.testcraft.testcraft.models

class TemplateSectionModel {

    var Status = ""
    var Msg = ""
    var data: ArrayList<TemplateSetionData> = ArrayList()

    class TemplateSetionData {
        var SubjectName = ""
        var SubjectID = ""
        var Sectionname = ""
        var QuestionTypeID = ""
        var QuestionTypeName = ""
        var NoOfQue = ""
        var Marks = ""
        var NegativeMarks = ""
        var SectionInstruction = ""
    }

}